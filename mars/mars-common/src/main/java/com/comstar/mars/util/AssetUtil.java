/*
 * AssetUtil.java 2014-8-15
 */
package com.comstar.mars.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.comstar.mars.domain.CashFlow;
import com.comstar.mars.domain.Frequency;
import com.comstar.mars.domain.ScheduledAsset;

/**
 * 资产工具类
 * 
 * @author Li Rong
 * @version 1.0
 */
public class AssetUtil {

	public static List<CashFlow> expendSchedule(ScheduledAsset asset) {
		if (asset == null) {
			throw new IllegalArgumentException("asset can't be null");
		}

		Integer endDate = asset.endDate();
		Integer firstCouponDate = asset.firstCouponDate();
		Frequency frequency = asset.couponFrequency();

		List<CashFlow> cashFlows = new ArrayList<CashFlow>();
		cashFlows.add(new CashFlow(firstCouponDate, new BigDecimal("0.0")));

		Integer processedDate = firstCouponDate;
		while (processedDate < endDate) {
			Integer processDate = addFrequency(processedDate, frequency);
			if (processDate > endDate) {
				// 有畸零天期
				boolean merge = asset.isMergeBrokenDate();
				if (merge) {
					CashFlow lastCashFlow = cashFlows.get(cashFlows.size() - 1);
					lastCashFlow.setDate(endDate);
					lastCashFlow.setAmount(new BigDecimal(0.0));
				} else {
					cashFlows.add(new CashFlow(endDate, new BigDecimal("0.0")));
				}
			} else {
				cashFlows.add(new CashFlow(processDate, new BigDecimal("0.0")));
			}

			processedDate = processDate;
		}

		return cashFlows;
	}

	private static Integer addFrequency(Integer base, Frequency frequency) {
		int quantity = frequency.getQuantity();
		String unit = frequency.getUnit();

		if (Frequency.YEAR.equals(unit)) {
			return DateUtil.yearSkip(base, quantity);
		} else if (Frequency.MONTH.equals(unit)) {
			return DateUtil.monthSkip(base, quantity);
		} else if (Frequency.DAY.equals(unit)) {
			return DateUtil.dateSkip(base, quantity);
		} else {
			throw new IllegalArgumentException(
					"not supported frequency type in asset");
		}
	}

}