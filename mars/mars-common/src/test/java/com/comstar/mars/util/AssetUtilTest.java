/*
 * AssetUtilTest.java 2014-8-15
 */
package com.comstar.mars.util;

import java.util.List;

import org.junit.Test;
import static org.junit.Assert.*;

import com.comstar.mars.domain.CashFlow;
import com.comstar.mars.domain.Frequency;
import com.comstar.mars.domain.ScheduledAsset;

/**
 * 测试
 * 
 * @author Li Rong
 * @version 1.0
 */
public class AssetUtilTest {

	@Test
	public void testBondFixedFreq1Year() {
		ScheduledAsset asset = new ScheduledAsset() {
			@Override
			public String type() {
				return "bond";
			}

			@Override
			public Integer startDate() {
				return 20011127;
			}

			@Override
			public String name() {
				return "010013";
			}

			@Override
			public Integer endDate() {
				return 20061127;
			}

			@Override
			public String code() {
				return "010013";
			}

			@Override
			public boolean isMergeBrokenDate() {
				return false;
			}

			@Override
			public Integer firstCouponDate() {
				return 20021127;
			}

			@Override
			public Frequency couponFrequency() {
				return new Frequency("1Y");
			}
		};

		List<CashFlow> cashes = AssetUtil.expendSchedule(asset);
		assertNotNull(cashes);
		assertEquals(5, cashes.size());
		Integer[] expected = { 20021127, 20031127, 20041127, 20051127, 20061127 };
		Integer[] result = new Integer[cashes.size()];
		for (int i = 0; i < cashes.size(); i++) {
			CashFlow cashFlow = cashes.get(i);
			result[i] = cashFlow.getDate();
		}

		assertArrayEquals(expected, result);
	}

	@Test
	public void testBondFixedFreqHalfYear() {
		ScheduledAsset asset = new ScheduledAsset() {
			@Override
			public String type() {
				return "bond";
			}

			@Override
			public Integer startDate() {
				return 20080215;
			}

			@Override
			public String name() {
				return "080403";
			}

			@Override
			public Integer endDate() {
				return 20110215;
			}

			@Override
			public String code() {
				return "080403";
			}

			@Override
			public boolean isMergeBrokenDate() {
				return false;
			}

			@Override
			public Integer firstCouponDate() {
				return 20080815;
			}

			@Override
			public Frequency couponFrequency() {
				return new Frequency("6M");
			}
		};

		List<CashFlow> cashes = AssetUtil.expendSchedule(asset);
		assertNotNull(cashes);
		assertEquals(6, cashes.size());
		Integer[] expected = { 20080815, 20090215, 20090815, 20100215,
				20100815, 20110215 };
		Integer[] result = new Integer[cashes.size()];
		for (int i = 0; i < cashes.size(); i++) {
			CashFlow cashFlow = cashes.get(i);
			result[i] = cashFlow.getDate();
		}

		assertArrayEquals(expected, result);
	}
}
