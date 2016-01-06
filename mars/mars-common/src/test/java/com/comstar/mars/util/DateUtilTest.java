/*
 * DateUtilTest.java 2014-7-10
 */
package com.comstar.mars.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * DateUtil测试类
 * 
 * @author Li,Rong
 * @version 1.0
 */
public class DateUtilTest {
	@Test(expected = IllegalArgumentException.class)
	public void testDateSkipNullParam() {
		DateUtil.dateSkip(null, 1);
	}

	@Test
	public void testDateSkipZero() {
		int date = DateUtil.dateSkip(20130228, 0);
		assertEquals("same date after 0", 20130228, date);
	}

	@Test
	public void testDateSkipPositive() {
		int date = DateUtil.dateSkip(20130228, 2);
		assertEquals("same date after 0", 20130302, date);
	}

	@Test
	public void testDateSkipNegative() {
		int date = DateUtil.dateSkip(20130228, -3);
		assertEquals("same date after 0", 20130225, date);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDayGapNullParam1() {
		DateUtil.dateGap(null, 20130101);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDayGapNullParam2() {
		DateUtil.dateGap(20130101, null);
	}

	@Test(expected = Exception.class)
	public void testDayGapIllegalParam() {
		// 20130229 is not a valid date
		DateUtil.dateGap(20130101, 20130229);
	}

	@Test
	public void testDayGapSameDate() {
		int gap = DateUtil.dateGap(20130101, 20130101);
		assertEquals("date gap shoud be 0 days", 0, gap);
	}

	@Test
	public void testDayGapInMonth() {
		int gap = DateUtil.dateGap(20130101, 20130105);
		assertEquals("date gap shoud be 4 days", 4, gap);
	}

	@Test
	public void testDayGapOverMonth() {
		int gap = DateUtil.dateGap(20130101, 20130203);
		assertEquals("date gap shoud be 33 days", 33, gap);
	}

	@Test
	public void testDayGapInMonthNegative() {
		int gap = DateUtil.dateGap(20130101, 20121227);
		assertEquals("date gap shoud be -5 days", -5, gap);
	}

	@Test(expected = Exception.class)
	public void testYearGapNullParam() {
		DateUtil.yearGap(20140301, null);
	}

	@Test
	public void testYearGapInYear() {
		int year = DateUtil.yearGap(20140301, 20140701);
		assertEquals("should be 0", 0, year);
	}

	@Test
	public void testYearGapOverYear() {
		int year = DateUtil.yearGap(20140301, 20170701);
		assertEquals("should be 0", 3, year);
	}

	@Test(expected = Exception.class)
	public void testYearSkipNullParam() {
		DateUtil.yearSkip(null, 1);
	}

	@Test
	public void testYearSkipZero() {
		int newDate = DateUtil.yearSkip(20130505, 0);
		assertEquals("should not affected after skip 0 year", 20130505, newDate);
	}

	@Test
	public void testYearSkipPositive() {
		int newDate = DateUtil.yearSkip(20130505, 3);
		assertEquals("should skip 3 years", 20160505, newDate);
	}

	@Test
	public void testMonthGap() {
		int gap = DateUtil.monthGap(20140103, 20140201);
		assertEquals(0, gap);
	}
	
	@Test
	public void testMonthGapOver() {
		int gap = DateUtil.monthGap(20140103, 20140203);
		assertEquals(1, gap);
	}
}
