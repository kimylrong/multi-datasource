/*
 * NumberUtil.java 2014-7-10
 */
package com.comstar.mars.util;

import java.math.BigDecimal;

/**
 * 数学计算
 * 
 * @author Li,Rong
 * @version 1.0
 */
public class NumberUtil {
	public static final BigDecimal ZERO = new BigDecimal("0");
	public static final BigDecimal ONE = new BigDecimal("1");
	public static final BigDecimal HUNDRED = new BigDecimal("100");

	public static BigDecimal round(BigDecimal oriNumber, int scale) {
		if (oriNumber == null) {
			throw new IllegalArgumentException("null param not allowed");
		}

		if (scale < 0) {
			throw new IllegalArgumentException("scale should not less than 0");
		}

		return oriNumber.divide(ONE, scale, BigDecimal.ROUND_HALF_UP);
	}

	public static Integer tryParseInt(String target) {
		Integer result = null;
		try {
			result = Integer.parseInt(target);
		} catch (Exception e) {
		}

		return result;
	}

	public static String intToString(Integer target) {
		if (target == null) {
			return null;
		} else {
			return target.toString();
		}
	}
}
