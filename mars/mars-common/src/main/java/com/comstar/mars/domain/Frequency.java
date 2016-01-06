/*
 * Frequency.java 2014-8-15
 */
package com.comstar.mars.domain;

import org.apache.commons.lang3.StringUtils;

/**
 * 频率
 * 
 * @author Li Rong
 * @version 1.0
 */
public class Frequency {
	public static final String YEAR = "Y";
	public static final String MONTH = "M";
	public static final String DAY = "D";
	public static final String HOUR = "H";
	public static final String MINUTE = "m";
	public static final String SECOND = "S";

	public static final String[] UNIT_ARRAY = { YEAR, MONTH, DAY, HOUR, MINUTE,
			SECOND };

	private String unit;
	private Integer quantity;

	public Frequency(String desc) {
		if (StringUtils.isEmpty(desc)) {
			throw new IllegalArgumentException(
					"frequency description can't be empty");
		}

		unit = desc.substring(desc.length() - 1);
		if (!validUnit(unit)) {
			throw new IllegalArgumentException("unsupported frequency unit: "
					+ unit);
		}

		String quantityDesc = desc.substring(0, desc.length() - 1);
		try {
			quantity = Integer.valueOf(quantityDesc);
			if (quantity <= 0) {
				throw new IllegalArgumentException(
						"frequency quantity should be a positive integer number: "
								+ quantityDesc);
			}
		} catch (Exception e) {
			throw new IllegalArgumentException(
					"frequency quantity should be a positive integer number: "
							+ quantityDesc);
		}
	}

	public boolean validUnit(String unit) {
		if (StringUtils.isEmpty(unit)) {
			return false;
		}

		for (String supportUnit : UNIT_ARRAY) {
			if (supportUnit.equals(unit)) {
				return true;
			}
		}

		return false;
	}

	public String getUnit() {
		return unit;
	}

	public Integer getQuantity() {
		return quantity;
	}

	@Override
	public String toString() {
		return quantity + unit;
	}
}
