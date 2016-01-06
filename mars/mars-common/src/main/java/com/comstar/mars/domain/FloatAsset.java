/*
 * FloatAsset.java 2014-8-15
 */
package com.comstar.mars.domain;

import java.math.BigDecimal;

/**
 * 无日程，由市价决定的资产
 * 
 * @author Li Rong
 * @version 1.0
 */
public interface FloatAsset extends Asset {
	/**
	 * 获取某日市价, 如果当日没有数据，理论上应该用最近一天的。
	 * 
	 * @param date
	 */
	BigDecimal price(Integer date);
}
