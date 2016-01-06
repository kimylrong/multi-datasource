/*
 * DateUtil.java 2014-7-10
 */
package com.comstar.mars.util;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.Months;
import org.joda.time.Years;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * 日期处理
 * 
 * @author Li,Rong
 * @version 1.0
 */
public class DateUtil {
	public static DateTimeFormatter formatter = DateTimeFormat
			.forPattern("yyyyMMdd");

	public static Integer dateSkip(Integer base, int gap) {
		if (base == null) {
			throw new IllegalArgumentException("date can't be null");
		}

		LocalDate date = formatter.parseLocalDate(base.toString());
		LocalDate newDate = date.plusDays(gap);
		return Integer.valueOf(newDate.toString(formatter));
	}

	public static int dateGap(Integer date1, Integer date2) {
		if (date1 == null || date2 == null) {
			throw new IllegalArgumentException("date can't be null");
		}

		LocalDate d1 = formatter.parseLocalDate(date1.toString());
		LocalDate d2 = formatter.parseLocalDate(date2.toString());

		return Days.daysBetween(d1, d2).getDays();
	}

	public static int yearGap(Integer date1, Integer date2) {
		if (date1 == null || date2 == null) {
			throw new IllegalArgumentException("date can't be null");
		}

		LocalDate d1 = formatter.parseLocalDate(date1.toString());
		LocalDate d2 = formatter.parseLocalDate(date2.toString());

		return Years.yearsBetween(d1, d2).getYears();
	}

	public static Integer yearSkip(Integer base, int gap) {
		if (base == null) {
			throw new IllegalArgumentException("date can't be null");
		}

		LocalDate date = formatter.parseLocalDate(base.toString());
		LocalDate newDate = date.plusYears(gap);
		return Integer.valueOf(newDate.toString(formatter));
	}

	public static int monthGap(Integer date1, Integer date2) {
		if (date1 == null || date2 == null) {
			throw new IllegalArgumentException("date can't be null");
		}

		LocalDate d1 = formatter.parseLocalDate(date1.toString());
		LocalDate d2 = formatter.parseLocalDate(date2.toString());

		return Months.monthsBetween(d1, d2).getMonths();
	}

	public static Integer monthSkip(Integer base, int gap) {
		if (base == null) {
			throw new IllegalArgumentException("date can't be null");
		}

		LocalDate date = formatter.parseLocalDate(base.toString());
		LocalDate newDate = date.plusMonths(gap);
		return Integer.valueOf(newDate.toString(formatter));
	}
}
