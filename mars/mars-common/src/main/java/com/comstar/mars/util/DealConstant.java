/*
 * DealConstant.java 2014-7-10
 */
package com.comstar.mars.util;

/**
 * 交易常量
 * 
 * @author Li,Rong
 * @version 1.0
 */
public class DealConstant {
	public static final String STATUS_ADD = "A";
	public static final String STATUS_UPDATE = "U";
	public static final String STATUS_DELETE = "D";

	public static final String DIRECTION_BUY = "B";
	public static final String DIRECTION_SELL = "S";

	public static final String CASH_TYPE_COUPON = "付息";
	public static final String CASH_TYPE_CAPITAL = "还本";

	public static final String SECURITY_SETTLE_TYPE_1 = "券款对付";
	public static final String SECURITY_SETTLE_TYPE_2 = "见款付券";
	public static final String SECURITY_SETTLE_TYPE_3 = "见券付款";

	// 零息债券
	public static final String SECURITY_DISCOUNT_RATE_1 = "1";
	// 付息债券
	public static final String SECURITY_DISCOUNT_RATE_2 = "0";

	// 利率类别
	public static final String SECURITY_RATE_TYPE_0 = "0";
	public static final String SECURITY_RATE_TYPE_1 = "1";
	public static final String SECURITY_RATE_TYPE_2 = "2";
	public static final String SECURITY_RATE_TYPE_3 = "3";
	public static final String SECURITY_RATE_TYPE_4 = "4";
	public static final String SECURITY_RATE_TYPE_5 = "5";
}
