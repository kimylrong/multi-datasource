/*
 * SecurityService.java 2014-7-10
 */
package com.comstar.mars.service;

import java.math.BigDecimal;

/**
 * 现券交易操作接口
 * 
 * @author Li,Rong
 * @version 1.0
 */
public interface SecurityService {
	/**
	 * 产生应收数据
	 * 
	 * @param dayend
	 */
	void generateReceivable(Integer dayend);

	/**
	 * 计算应计利息
	 * 
	 * @return
	 */
	BigDecimal getAccruedInterest(String securityCode, Integer settleDate,
			BigDecimal position);

	/**
	 * 计算每百元应计利息
	 * 
	 * @return
	 */
	BigDecimal getAccruedInterestPerHundred(String securityCode,
			Integer settleDate);
}
