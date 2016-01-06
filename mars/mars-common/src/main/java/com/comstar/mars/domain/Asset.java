/*
 * Asset.java 2014-8-15
 */
package com.comstar.mars.domain;

/**
 * 资产抽象
 * 
 * @author Li,Rong
 * @version 1.0
 */
public interface Asset {
	/**
	 * 获取资产名称, 不能为NULL
	 */
	String name();

	/**
	 * 获取资产代码, 不能为NULL
	 */
	String code();

	/**
	 * 获取资产类型, 不能为NULL
	 */
	String type();

	/**
	 * 获取资产起息日, 不能为NULL
	 */
	Integer startDate();

	/**
	 * 获取资产到期日, 没有返回NULL
	 */
	Integer endDate();
}
