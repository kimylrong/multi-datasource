/*
 * FinancialProductServiceTest.java 2014-7-10
 */
package com.comstar.mars.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.comstar.mars.entity.FinancialProduct;
import com.comstar.mars.entity.FinancialProductExample;

/**
 * FinancialProductService单元测试
 * 
 * @author Li,Rong
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
@Transactional
public class FinancialProductServiceTest {
	@Autowired
	private FinancialProductService productService;

	@Test(expected = ServiceException.class)
	public void testSaveNullParam() {
		productService.saveProduct(null);
	}

	@Test
	public void testSaveNew() {
		FinancialProduct product = new FinancialProduct();
		product.setName("活期宝");

		Integer id = productService.saveProduct(product);
		assertNotNull("id can't be null", id);
		assertTrue(id > 0);
	}

	@Test(expected = ServiceException.class)
	public void testUpdateUnexisted() {
		FinancialProduct product = new FinancialProduct();
		product.setFinancialProductId(-1);
		product.setName("活期宝");
		productService.saveProduct(product);
	}

	@Test
	public void testSaveQueryUpdateNormal() {
		FinancialProduct product = new FinancialProduct();
		product.setName("活期宝");
		Integer id = productService.saveProduct(product);
		assertNotNull("id can't be null", id);
		assertTrue("id must bigger than 0", id > 0);

		FinancialProduct queryResult = productService.queryProductByKey(id);
		assertNotNull("can't find product by id", queryResult);
		assertEquals("should have same name", product.getName(),
				queryResult.getName());

		FinancialProduct updateProduct = new FinancialProduct();
		updateProduct.setFinancialProductId(id);
		updateProduct.setBranchId(10010);
		Integer updateId = productService.saveProduct(updateProduct);
		assertEquals("id can't change after update", id, updateId);

		// check update result
		FinancialProduct queryResult2 = productService.queryProductByKey(id);
		assertEquals("name can't be change", product.getName(),
				queryResult2.getName());
		assertEquals("branch id should be updated",
				updateProduct.getBranchId(), queryResult2.getBranchId());
	}

	@Test(expected = ServiceException.class)
	public void testQueryByKeyNull() {
		productService.queryProductByKey(null);
	}

	public void testQueryByKeyUnexisted() {
		FinancialProduct product = productService.queryProductByKey(-1);
		assertNull("id can't be -1", product);
	}

	@Test(expected = ServiceException.class)
	public void testQueryNull() {
		productService.queryProduct(null);
	}

	@Test
	public void testQueryNormal() {
		FinancialProduct product = new FinancialProduct();
		product.setName("活期宝");
		Integer id = productService.saveProduct(product);

		FinancialProductExample example = new FinancialProductExample();
		example.createCriteria().andNameEqualTo(product.getName());

		List<FinancialProduct> list = productService.queryProduct(example);
		assertNotNull("result should not be null", list);
		assertEquals("just one and must one product matched", list.size(), 1);
		assertEquals("id shoud match", list.get(0).getFinancialProductId(), id);
	}
}
