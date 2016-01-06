/*
 * SecurityServiceTest.java 2014-7-10
 */
package com.comstar.mars.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.comstar.mars.entity.DealSecurity;
import com.comstar.mars.entity.DealSecurityReceivable;
import com.comstar.mars.entity.DealSecurityReceivableExample;
import com.comstar.mars.repository.DealSecurityMapper;
import com.comstar.mars.repository.DealSecurityReceivableMapper;
import com.comstar.mars.util.DealConstant;

/**
 * 债券服务测试
 * 
 * @author Li,Rong
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
@Transactional
public class SecurityServiceTest {
	@Autowired
	private DealSecurityMapper dealSecurityMapper;
	@Autowired
	private SecurityService securityService;
	@Autowired
	private DealSecurityReceivableMapper dealSecurityReceivableMapper;

	@Test(expected = ServiceException.class)
	public void testGenWithNullParam() {
		securityService.generateReceivable(null);
	}

	@Test
	public void testGenNormal() {
		DealSecurity dealSecurity = new DealSecurity();
		dealSecurity.setBranchId(1001);
		dealSecurity.setFinancialProductId(6004);
		dealSecurity.setStatus(DealConstant.STATUS_ADD);
		dealSecurity.setSecurityCode("1280462");
		dealSecurity.setTradeDate(20131211);
		dealSecurity.setValueDate(20131211);
		dealSecurity.setNominal(new BigDecimal("450"));
		dealSecurity.setClearPrice(new BigDecimal("98.30"));
		dealSecurity.setDirtyPrice(new BigDecimal("99.00"));
		dealSecurity.setInterest(new BigDecimal("31500.00"));
		dealSecurity.setSettleAmount(new BigDecimal("4455000.00"));
		dealSecurity.setSettleType(DealConstant.SECURITY_SETTLE_TYPE_1);
		dealSecurity.setYield(new BigDecimal("6.5"));
		dealSecurity.setDirection(DealConstant.DIRECTION_BUY);
		dealSecurity.setModifyDate(new Date());
		dealSecurity.setModifyUser(1);

		dealSecurityMapper.insert(dealSecurity);

		securityService.generateReceivable(20131212);

		DealSecurityReceivableExample example = new DealSecurityReceivableExample();
		example.createCriteria().andFinancialProductIdEqualTo(6004)
				.andSecurityCodeEqualTo("1280462");
		List<DealSecurityReceivable> reveiables = dealSecurityReceivableMapper
				.selectByExample(example);

		assertFalse("reveivable must have value", reveiables.isEmpty());
	}

	@Test(expected = ServiceException.class)
	public void testAccruedNullParam() {
		securityService.getAccruedInterest(null, null, null);
	}

	@Test(expected = ServiceException.class)
	public void testAccruedSmallDate() {
		securityService.getAccruedInterest("1382084", 20130101, new BigDecimal(
				"10000000"));
	}

	@Test(expected = ServiceException.class)
	public void testAccruedBigDate() {
		securityService.getAccruedInterest("1382084", 20160309, new BigDecimal(
				"10000000"));
	}

	@Test
	public void testDiscountBondAccrued() {
		BigDecimal interest = securityService.getAccruedInterest("139905",
				20131222, new BigDecimal("10000000"));
		assertEquals("accrued interest should be 129945.05", new BigDecimal(
				"129945.05"), interest);
	}

	@Test
	public void testAccruedCouponDate() {
		BigDecimal interest = securityService.getAccruedInterest("098095",
				20130601, new BigDecimal("10000000"));
		assertEquals("accrued interest should be 0.0 when coupon date",
				new BigDecimal("0.00"), interest);
	}

	@Test
	public void testAccrued() {
		BigDecimal interest = securityService.getAccruedInterest("1382084",
				20140707, new BigDecimal("10000000"));
		assertEquals("accrued interest should be 210575.34", new BigDecimal(
				"210575.34"), interest);
	}

	@Test
	public void testZeroBond() {
		BigDecimal interest = securityService.getAccruedInterest("071435004",
				20140801, new BigDecimal("10000000"));
		assertEquals("accrued interest should be 22093.15", new BigDecimal(
				"22093.15"), interest);
	}
	
	@Test
	public void testZeroBond2() {
		BigDecimal interest = securityService.getAccruedInterest("041460017",
				20150101, new BigDecimal("10000000"));
		assertEquals("accrued interest should be 477630.14", new BigDecimal(
				"477630.14"), interest);
	}
	
	@Test
	public void testFloatBond1(){
		BigDecimal interest = securityService.getAccruedInterest("1082078",
				20140802, new BigDecimal("10000000"));
		assertEquals("accrued interest should be 133652.05", new BigDecimal(
				"133652.05"), interest);
	}
	
	@Test
	public void testFloatBond2(){
		BigDecimal interest = securityService.getAccruedInterest("1280225",
				20140710, new BigDecimal("10000000"));
		assertEquals("accrued interest should be 646301.37", new BigDecimal(
				"646301.37"), interest);
	}
}
