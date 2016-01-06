/*
 * SecurityServiceImpl.java 2014-7-10
 */
package com.comstar.mars.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.comstar.mars.entity.DealSecurity;
import com.comstar.mars.entity.DealSecurityExample;
import com.comstar.mars.entity.DealSecurityReceivable;
import com.comstar.mars.entity.Security;
import com.comstar.mars.entity.SecurityPaymentSchedule;
import com.comstar.mars.entity.SecurityPaymentScheduleExample;
import com.comstar.mars.repository.DealSecurityMapper;
import com.comstar.mars.repository.DealSecurityReceivableMapper;
import com.comstar.mars.repository.SecurityMapper;
import com.comstar.mars.repository.SecurityPaymentScheduleMapper;
import com.comstar.mars.util.DateUtil;
import com.comstar.mars.util.DealConstant;
import com.comstar.mars.util.NumberUtil;
import com.comstar.mars.util.ObjectProcessor;

/**
 * 现券交易操作实现
 * 
 * @author Li,Rong
 * @version 1.0
 */
@Component
@Transactional
public class SecurityServiceImpl implements SecurityService {
	private final static Logger LOG = LoggerFactory
			.getLogger(SecurityServiceImpl.class);
	@Autowired
	private DealSecurityMapper dealSecurityMapper;
	@Autowired
	private SecurityPaymentScheduleMapper securityPaymentScheduleMapper;
	@Autowired
	private DealSecurityReceivableMapper dealSecurityReceivableMapper;
	@Autowired
	private SecurityMapper securityMapper;

	@Override
	public void generateReceivable(Integer dayend) {
		if (dayend == null) {
			throw new ServiceException("日终日不能为空");
		}

		if (dayend < 10000101 || dayend > 30000101) {
			throw new ServiceException(String.format("日终日不合法: %s", dayend));
		}

		LOG.info(String.format("开始生成现券应收(dayend=%s)", dayend));

		// T日的日终,生成T+1日的应收数据
		Integer processDay = DateUtil.dateSkip(dayend, 1);

		// 先load当日有还本或者付息的债券
		SecurityPaymentScheduleExample scheduleExample = new SecurityPaymentScheduleExample();
		scheduleExample.createCriteria().andPaymentDateEqualTo(
				processDay.toString());
		List<SecurityPaymentSchedule> schedules = securityPaymentScheduleMapper
				.selectByExample(scheduleExample);

		// 当日无付息或者还本，结束任务
		if (schedules.isEmpty()) {
			LOG.info(String.format("今日无现券应收(dayend=%s), 结束任务", dayend));
			return;
		}

		// 从交易表查看持仓情况。特别注意,T+1日的应收,以T日日终前交割的为准
		List<String> securityCodes = ObjectProcessor
				.getFieldList(
						schedules,
						new ObjectProcessor.FieldValueGetter<SecurityPaymentSchedule, String>() {
							@Override
							public String getValue(SecurityPaymentSchedule obj) {
								return obj.getSecurityCode();
							}
						});
		DealSecurityExample dealExample = new DealSecurityExample();
		dealExample.createCriteria().andSecurityCodeIn(securityCodes)
				.andStatusEqualTo(DealConstant.STATUS_ADD)
				.andValueDateLessThanOrEqualTo(dayend);
		List<DealSecurity> deals = dealSecurityMapper
				.selectByExample(dealExample);
		if (deals.isEmpty()) {
			LOG.info(String.format("今日无现券应收(dayend=%s), 结束任务", dayend));
			return;
		}

		// group by financial product
		Map<Integer, List<DealSecurity>> dealsMap = ObjectProcessor
				.groupByField(
						deals,
						new ObjectProcessor.FieldValueGetter<DealSecurity, Integer>() {
							@Override
							public Integer getValue(DealSecurity obj) {
								return obj.getFinancialProductId();
							}
						});
		for (Integer productId : dealsMap.keySet()) {
			List<DealSecurity> productDeals = dealsMap.get(productId);

			// group by security code
			Map<String, List<DealSecurity>> securityMap = ObjectProcessor
					.groupByField(
							productDeals,
							new ObjectProcessor.FieldValueGetter<DealSecurity, String>() {
								@Override
								public String getValue(DealSecurity obj) {
									return obj.getSecurityCode();
								}
							});
			for (String securityCode : securityMap.keySet()) {
				List<DealSecurity> securityDeals = securityMap
						.get(securityCode);
				// 计算仓位
				BigDecimal position = new BigDecimal(0);
				for (DealSecurity dealSecurity : securityDeals) {
					String direction = dealSecurity.getDirection();
					if (DealConstant.DIRECTION_BUY.equals(direction)) {
						position = position.add(dealSecurity.getNominal());
					} else {
						position = position.subtract(dealSecurity.getNominal());
					}
				}

				// 仓位>0才处理
				if (position.compareTo(NumberUtil.ZERO) > 0) {
					SecurityPaymentSchedule schedule = ObjectProcessor
							.searchByField(
									schedules,
									new ObjectProcessor.FieldValueGetter<SecurityPaymentSchedule, String>() {
										@Override
										public String getValue(
												SecurityPaymentSchedule obj) {
											return obj.getSecurityCode();
										}
									}, securityCode);
					if (schedule.getCouponAmt().compareTo(NumberUtil.ZERO) > 0) {
						DealSecurityReceivable coupon = new DealSecurityReceivable();
						coupon.setBranchId(securityDeals.get(0).getBranchId());
						coupon.setFinancialProductId(productId);
						coupon.setSecurityCode(securityCode);
						coupon.setReceiveDate(processDay);
						coupon.setReceiveType(DealConstant.CASH_TYPE_COUPON);
						coupon.setAmount(schedule.getCouponAmt().multiply(
								position));
						coupon.setStatus(DealConstant.STATUS_ADD);
						coupon.setModifyDate(new Date());
						dealSecurityReceivableMapper.insert(coupon);
						LOG.info(String
								.format("应收利息(productId=%s,securityCode=%s,dayend=%s,position=%s,amount=%s)",
										productId, securityCode, dayend,
										position, coupon.getAmount()));
					}

					if (schedule.getBackAmt().compareTo(NumberUtil.ZERO) > 0) {
						DealSecurityReceivable capital = new DealSecurityReceivable();
						capital.setBranchId(securityDeals.get(0).getBranchId());
						capital.setFinancialProductId(productId);
						capital.setSecurityCode(securityCode);
						capital.setReceiveDate(processDay);
						capital.setReceiveType(DealConstant.CASH_TYPE_CAPITAL);
						capital.setAmount(schedule.getBackAmt().multiply(
								position));
						capital.setStatus(DealConstant.STATUS_ADD);
						capital.setModifyDate(new Date());
						dealSecurityReceivableMapper.insert(capital);
						LOG.info(String
								.format("应收还本(productId=%s,securityCode=%s,dayend=%s,position=%s,amount=%s)",
										productId, securityCode, dayend,
										position, capital.getAmount()));
					}
				} else {
					LOG.info(String
							.format("仓位不大于0，无需出应收(productId=%s,securityCode=%s,dayend=%s,position=%s)",
									productId, securityCode, dayend, position));
				}
			}
		}

		LOG.info(String.format("应收任务处理成功(dayend=%s)", dayend));
	}

	@Override
	public BigDecimal getAccruedInterestPerHundred(String securityCode,
			Integer settleDate) {
		// check param
		if (securityCode == null || settleDate == null) {
			throw new ServiceException("Illegal param, null is not allowed");
		}

		// query security information
		Security security = securityMapper.selectByPrimaryKey(securityCode);
		if (security == null) {
			throw new ServiceException(String.format(
					"security not found(securityCode=%s)", securityCode));
		}
		Integer startCouponDate = Integer
				.valueOf(security.getStartCouponDate());
		Integer maturityDate = Integer.valueOf(security.getMaturityDate());
		if (settleDate.compareTo(startCouponDate) < 0) {
			throw new ServiceException(
					String.format(
							"settle date less than security start coupon date(securityCode=%s,startCouponDate=%s,settleDate=%s)",
							securityCode, startCouponDate, settleDate));
		}

		if (settleDate.compareTo(maturityDate) > 0) {
			throw new ServiceException(
					String.format(
							"settle date later than security maturity date(securityCode=%s,maturityDate=%s,settleDate=%s)",
							securityCode, maturityDate, settleDate));
		}

		String rateType = security.getRateType();
		if (DealConstant.SECURITY_DISCOUNT_RATE_1.equals(security
				.getDiscountRate())) {
			if (NumberUtil.ZERO.equals(security.getFixedRate())) {
				// discountRate = 1 and fixed_rate=0 贴现债券
				BigDecimal autionPrice = security.getAutionPrice();
				int interestDuration = DateUtil.dateGap(startCouponDate,
						maturityDate);
				int interestPassed = DateUtil.dateGap(startCouponDate,
						settleDate);
				BigDecimal interest = (NumberUtil.HUNDRED.subtract(autionPrice))
						.multiply(new BigDecimal(interestPassed).divide(
								new BigDecimal(interestDuration), 60,
								BigDecimal.ROUND_HALF_UP));
				return NumberUtil.round(interest, 16);
			} else {
				// discountRate = 1 and fixed_rate>0 零息债券
				// 目前bond库里面没有此类债券, not support
				throw new ServiceException(
						String.format(
								"Not supported bond type, discount and fixrate>0:(securityCode=%s,discountRate=%s,fixRate=%s)",
								securityCode, security.getDiscountRate(),
								security.getFixedRate()));
			}
		} else if (DealConstant.SECURITY_RATE_TYPE_3.equals(rateType)) {
			// rate_type:3 一次性还本付息债券, 固定利率
			int year = DateUtil.yearGap(startCouponDate, settleDate);
			BigDecimal fixedRate = security.getFixedRate();

			Integer startDateOfCurrent = DateUtil.yearSkip(startCouponDate,
					year);
			Integer endDateOfCurrent = DateUtil.yearSkip(startCouponDate,
					year + 1);
			Integer passedOfCurrent = DateUtil.dateGap(startDateOfCurrent,
					settleDate);
			Integer daysOfCurrent = DateUtil.dateGap(startDateOfCurrent,
					endDateOfCurrent);

			BigDecimal interest = fixedRate.multiply(new BigDecimal(year)).add(
					fixedRate.multiply(new BigDecimal(passedOfCurrent)).divide(
							new BigDecimal(daysOfCurrent), 60,
							BigDecimal.ROUND_HALF_UP));
			return NumberUtil.round(interest, 16);
		} else if (DealConstant.SECURITY_RATE_TYPE_4.equals(rateType)
				|| DealConstant.SECURITY_RATE_TYPE_5.equals(rateType)) {
			// rate_type:4,5 一次性还本付息债券, 浮动或者变动利率
			// 目前bond库里面没有此类债券, not support
			throw new ServiceException(
					String.format(
							"Not supported bond type, rateType is 4 or 5, zero bond and float:(securityCode=%s,rateRate=%s)",
							securityCode, rateType));
		} else {
			SecurityPaymentScheduleExample scheduleExample = new SecurityPaymentScheduleExample();
			scheduleExample.createCriteria().andSecurityCodeEqualTo(
					securityCode);
			scheduleExample.setOrderByClause("PAYMENT_DATE ASC");
			List<SecurityPaymentSchedule> schedules = securityPaymentScheduleMapper
					.selectByExample(scheduleExample);
			if (schedules.isEmpty()) {
				throw new ServiceException(String.format(
						"security payment schedule not found(securityCode=%s)",
						securityCode));
			}

			// find last and next schedule of settle date. if settle date equals
			// payment_date, accrue is zero
			SecurityPaymentSchedule last = null;
			SecurityPaymentSchedule next = null;
			for (SecurityPaymentSchedule schedule : schedules) {
				Integer paymentDate = Integer.parseInt(schedule
						.getPaymentDate());
				// 没有付过息
				if (last == null) {
					if (settleDate.compareTo(paymentDate) < 0) {
						next = schedule;
						break;
					} else if (settleDate.compareTo(paymentDate) == 0) {
						return NumberUtil.ZERO;
					} else {
						last = schedule;
						continue;
					}
				}

				if (settleDate.compareTo(paymentDate) < 0) {
					next = schedule;
					break;
				} else if (settleDate.compareTo(paymentDate) == 0) {
					return NumberUtil.ZERO;
				} else {
					last = schedule;
					continue;
				}
			}

			Integer startDate;
			Integer lastDate;
			if (last == null) {
				startDate = startCouponDate;
			} else {
				startDate = Integer.valueOf(last.getPaymentDate());
			}
			lastDate = Integer.valueOf(next.getPaymentDate());

			int interestDuration = DateUtil.dateGap(startDate, lastDate);
			int interestPassed = DateUtil.dateGap(startDate, settleDate);

			BigDecimal interest = security
					.getFixedRate()
					.multiply(new BigDecimal(interestPassed))
					.divide(new BigDecimal(interestDuration), 60,
							BigDecimal.ROUND_HALF_UP);

			return NumberUtil.round(interest, 16);
		}
	}

	@Override
	public BigDecimal getAccruedInterest(String securityCode,
			Integer settleDate, BigDecimal position) {
		// check param
		if (securityCode == null || settleDate == null || position == null) {
			throw new ServiceException("Illegal param, null is not allowed");
		}
		BigDecimal interestPerHundred = getAccruedInterestPerHundred(
				securityCode, settleDate);
		BigDecimal interest = interestPerHundred.multiply(position).divide(
				NumberUtil.HUNDRED, 60, BigDecimal.ROUND_HALF_UP);
		return NumberUtil.round(interest, 2);
	}
}
