/*
 * NumberUtilTest.java 2014-7-30
 */
package com.comstar.mars.util;

import java.math.BigDecimal;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * 
 * 
 * @author Li,Rong
 * @version 1.0
 */
public class NumberUtilTest {
	@Test(expected = IllegalArgumentException.class)
	public void testRoundNullParam() {
		NumberUtil.round(null, 2);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRoundNegativeScale() {
		NumberUtil.round(new BigDecimal(33.7684), -2);
	}

	@Test
	public void testRoundZeroScale() {
		BigDecimal newNumber = NumberUtil.round(new BigDecimal(33.7684), 0);
		assertEquals("", new BigDecimal("34"), newNumber);
	}

	@Test
	public void testRoundPositiveScale() {
		BigDecimal newNumber = NumberUtil.round(new BigDecimal(33.7684), 3);
		assertEquals("", new BigDecimal("33.768"), newNumber);
	}
}
