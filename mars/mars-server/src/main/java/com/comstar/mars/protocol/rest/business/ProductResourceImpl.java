/*
 * ProductResourceImpl.java 2014-7-10
 */
package com.comstar.mars.protocol.rest.business;

import java.util.Date;
import java.util.List;

import com.comstar.mars.protocol.rest.WebException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import com.comstar.mars.entity.FinancialProduct;
import com.comstar.mars.entity.FinancialProductExample;
import com.comstar.mars.protocol.rest.business.ProductResource;
import com.comstar.mars.service.FinancialProductService;
import com.comstar.mars.service.system.ShiroRealm;

/**
 * 理财产品实现
 * 
 * @author Li,Rong
 * @version 1.0
 */
public class ProductResourceImpl implements ProductResource {
	@Autowired
	private FinancialProductService productService;

	@Override
	public List<FinancialProduct> index() {
		FinancialProductExample example = new FinancialProductExample();
		return productService.queryProduct(example);
	}

	@Override
	public FinancialProduct viewByKey(String id) {
		Integer key = null;
		try {
			key = new Integer(id);
		} catch (NumberFormatException e) {
			throw new WebException("invalid product id: " + id);
		}

		return productService.queryProductByKey(key);
	}

	@Override
	public void saveProduct(FinancialProduct product) {
		// set update user and time
		product.setModifyDate(new Date());
		Subject currentUser = SecurityUtils.getSubject();
		ShiroRealm.ShiroUser shiroUser = (ShiroRealm.ShiroUser) currentUser
				.getPrincipal();
		product.setModifyUser(shiroUser.getId());
		productService.saveProduct(product);
	}
}
