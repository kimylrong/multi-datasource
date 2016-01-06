/*
 * FinancialProductServiceImpl.java 2014-7-10
 */
package com.comstar.mars.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.comstar.mars.entity.FinancialProduct;
import com.comstar.mars.entity.FinancialProductExample;
import com.comstar.mars.repository.FinancialProductMapper;

/**
 * 理财产品操作实现
 * 
 * @author Li,Rong
 * @version 1.0
 */
@Component
@Transactional
public class FinancialProductServiceImpl implements FinancialProductService {
	private static final Logger LOG = LoggerFactory
			.getLogger(FinancialProductServiceImpl.class);
	@Autowired
	private FinancialProductMapper productMapper;

	@Override
	public Integer saveProduct(FinancialProduct financialProduct) {
		if (financialProduct == null) {
			throw new ServiceException("product can't be null");
		}

		Integer id = financialProduct.getFinancialProductId();
		if (id == null) {
			productMapper.insert(financialProduct);
			LOG.info(String.format("创建理财产品(name=%s)",
					financialProduct.getName()));
		} else {
			FinancialProduct temp = productMapper.selectByPrimaryKey(id);
			if (temp == null) {
				throw new ServiceException(String.format(
						"product does not exist:(product_id=%s)", id));
			}
			productMapper.updateByPrimaryKeySelective(financialProduct);
			LOG.info(String.format("修改理财产品(name=%s)",
					financialProduct.getName()));
		}

		return financialProduct.getFinancialProductId();
	}

	@Override
	public FinancialProduct queryProductByKey(Integer key) {
		if (key == null) {
			throw new ServiceException("product id can't be null");
		}

		return productMapper.selectByPrimaryKey(key);
	}

	@Override
	public List<FinancialProduct> queryProduct(FinancialProductExample condition) {
		if (condition == null) {
			throw new ServiceException("product query condition can't be null");
		}
		return productMapper.selectByExample(condition);
	}

}
