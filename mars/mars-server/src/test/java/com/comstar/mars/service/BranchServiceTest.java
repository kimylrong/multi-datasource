/*
 * BranchServiceTest.java 2014-9-18
 */
package com.comstar.mars.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.comstar.mars.entity.Branch;
import com.comstar.mars.entity.BranchExample;
import com.comstar.mars.service.system.BranchService;

/**
 * 机构服务测试
 * 
 * @author Li Rong
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
@Transactional
public class BranchServiceTest {
	@Autowired
	private BranchService branchService;

	@Test(expected = Exception.class)
	public void testSaveBranchNull() {
		branchService.saveBranch(null);
	}

	@Test
	public void testSaveBranch() {
		Branch branch = getTempBranch();
		branchService.saveBranch(branch);

		Branch queryResult = branchService.queryBranchByCode(branch.getCode());
		assertNotNull(queryResult);
		assertEquals(branch.getName(), queryResult.getName());

		Branch updateBranch = new Branch();
		updateBranch.setId(queryResult.getId());
		updateBranch.setName("UpdateName");

		branchService.saveBranch(updateBranch);

		Branch queryResult2 = branchService.queryBranchByCode(branch.getCode());
		assertNotNull(queryResult2);
		assertEquals("UpdateName", queryResult2.getName());
		assertEquals(branch.getAddress(), queryResult2.getAddress());
	}

	@Test(expected = Exception.class)
	public void testDeleteBranchNull() {
		branchService.deleteBranch(null);
	}

	@Test
	public void testDeleteBranch() {
		Branch branch = getTempBranch();
		branchService.saveBranch(branch);

		Branch queryResult = branchService.queryBranchByCode(branch.getCode());
		assertNotNull(queryResult);

		branchService.deleteBranch(queryResult.getId());
		Branch queryResult2 = branchService.queryBranchByCode(branch.getCode());
		assertNull(queryResult2);
	}

	@Test(expected = Exception.class)
	public void testQueryBranchByCodeNull() {
		branchService.queryBranchByCode(null);
	}

	@Test(expected = Exception.class)
	public void testQueryByConditionNull() {
		branchService.queryBranchByCondition(null);
	}

	@Test(expected = Exception.class)
	public void testQueryByIdNull() {
		branchService.queryBranchById(null);
	}

	@Test
	public void testQueryById() {
		Branch branch = getTempBranch();
		branchService.saveBranch(branch);

		Branch queryResult = branchService.queryBranchByCode(branch.getCode());
		assertNotNull(queryResult);
		assertEquals(branch.getName(), queryResult.getName());

		Branch queryResult2 = branchService
				.queryBranchById(queryResult.getId());
		assertNotNull(queryResult2);
		assertEquals(queryResult.getName(), queryResult2.getName());
		assertEquals(branch.getAddress(), queryResult2.getAddress());
	}

	@Test
	public void testQueryByCondition() {
		Branch branch1 = getTempBranch();
		Branch branch2 = getTempBranch();
		branch2.setName("Another Name");
		branch2.setCode("poor code");

		branchService.saveBranch(branch1);
		branchService.saveBranch(branch2);

		BranchExample branchExample = new BranchExample();
		branchExample.createCriteria().andContactPersonEqualTo(
				branch1.getContactPerson());

		List<Branch> list = branchService.queryBranchByCondition(branchExample);
		assertNotNull(list);
		assertEquals(2, list.size());
	}

	private Branch getTempBranch() {
		Branch branch = new Branch();
		branch.setCode("test1001");
		branch.setName("浦发总行");
		branch.setProvince("上海");
		branch.setCity("上海");
		branch.setAddress("南京东路1388号");
		branch.setContactPerson("test张总");
		branch.setTelephone("021-68888989");
		branch.setFaxNumber("021-68888989");

		return branch;
	}

}
