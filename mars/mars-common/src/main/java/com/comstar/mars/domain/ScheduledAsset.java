/*
 * FixIncomeAsset.java 2014-8-15
 */
package com.comstar.mars.domain;

/**
 * 日程确定的资产
 * 
 * @author Li Rong
 * @version 1.0
 */
public interface ScheduledAsset extends Asset {
	/**
	 * 首次付息日
	 * 
	 * @return
	 */
	Integer firstCouponDate();

	/**
	 * 计息频率
	 */
	Frequency couponFrequency();

	/**
	 * 畸零天期是否并入上一期, 一起支付。
	 */
	boolean isMergeBrokenDate();
}