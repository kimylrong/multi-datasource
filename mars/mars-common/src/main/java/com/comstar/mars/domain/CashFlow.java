/*
 * CashFlow.java 2014-8-15
 */
package com.comstar.mars.domain;

import java.math.BigDecimal;

/**
 * 现金流
 * 
 * @author Li Rong
 * @version 1.0
 */
public class CashFlow {
	public static final String DIRECTION_IN = "I";
	public static final String DIRECTION_OUT = "O";
	public static final String TYPE_COUPON = "COUPON";
	public static final String TYPE_CAPITY = "CAPITY";
	public static final String TYPE_FEE = "FEE";

	private Integer date;
	private BigDecimal amount;
	private String direction;
	private String type;
	private String source;

	public CashFlow() {
	}

	public CashFlow(Integer date, BigDecimal amount) {
		this(date, amount, DIRECTION_IN, TYPE_COUPON);
	}

	public CashFlow(Integer date, BigDecimal amount, String direction,
			String type) {
		this.date = date;
		this.amount = amount;
		this.direction = direction;
		this.type = type;
	}

	public Integer getDate() {
		return date;
	}

	public void setDate(Integer date) {
		this.date = date;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
}
