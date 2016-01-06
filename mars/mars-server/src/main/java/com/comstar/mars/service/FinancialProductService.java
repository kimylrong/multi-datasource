/*
 * FinancialProductService.java 2014-7-10
 */
package com.comstar.mars.service;

import java.util.List;

import com.comstar.mars.entity.FinancialProduct;
import com.comstar.mars.entity.FinancialProductExample;

/**
 * 理财产品处理接口
 * 
 * @author Li,Rong
 * @version 1.0
 */
public interface FinancialProductService {
	/**
	 * 保存一个理财产品信息。如果id为空，insert一条记录，否则更新产品信息。 financialProduct为null，
	 * 或者id不为空但记录不存在，都抛异常。
	 * 
	 * @param financialProduct
	 *            产品信息
	 * @return 自动产生的ID
	 */
	Integer saveProduct(FinancialProduct financialProduct);

	/**
	 * 通过ID查询理财产品。如果key为null，抛异常。
	 * 
	 * @param key
	 *            ID
	 * @return
	 */
	FinancialProduct queryProductByKey(Integer key);

	/**
	 * 通过既定条件查询理财产品如果key为null，抛异常。
	 * 
	 * @param conditon
	 * @return
	 */
	List<FinancialProduct> queryProduct(FinancialProductExample condition);
}
