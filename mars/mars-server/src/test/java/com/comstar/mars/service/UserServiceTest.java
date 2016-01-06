/*
 * UserServiceTest.java 2014-7-10
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

import com.comstar.mars.entity.User;
import com.comstar.mars.entity.UserExample;
import com.comstar.mars.service.system.UserService;
import com.comstar.mars.util.ManageConstant;

/**
 * UserService单元测试
 * 
 * @author Li,Rong
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
@Transactional
public class UserServiceTest {
	@Autowired
	private UserService userService;

	@Test(expected = ServiceException.class)
	public void testSaveNullParam() {
		userService.saveUser(null);
	}

	@Test
	public void testSaveNew() {
		User user = new User();
		user.setName("kimy");
		user.setNick("An hacker");
		user.setPassword("mars");
		user.setBranchId(1001);
		user.setStatus(ManageConstant.ACTIVE);

		Integer id = userService.saveUser(user);
		assertNotNull("id can't be null", id);
		assertTrue(id > 0);
	}

	@Test(expected = ServiceException.class)
	public void testUpdateUnexisted() {
		User user = new User();
		user.setName("kimy");
		user.setNick("An hacker");
		user.setPassword("mars");
		user.setBranchId(1001);
		user.setStatus(ManageConstant.ACTIVE);
		user.setId(-1);
		userService.saveUser(user);
	}

	@Test
	public void testSaveQueryUpdateNormal() {
		User user = new User();
		user.setName("kimy");
		user.setNick("An hacker");
		user.setPassword("mars");
		user.setBranchId(1001);
		user.setStatus(ManageConstant.ACTIVE);
		Integer id = userService.saveUser(user);
		assertNotNull("id can't be null", id);
		assertTrue("id must bigger than 0", id > 0);

		User queryResult = userService.queryUserByKey(id);
		assertNotNull("can't find user by id", queryResult);
		assertEquals("should have same name", user.getName(),
				queryResult.getName());

		User temp = new User();
		temp.setId(id);
		temp.setNick("An coder");
		Integer updateId = userService.saveUser(temp);
		assertEquals("id can't change after update", id, updateId);

		// check update result
		User queryResult2 = userService.queryUserByKey(id);
		assertEquals("name can't be change", user.getName(),
				queryResult2.getName());
		assertEquals("password can't be change", user.getPassword(),
				queryResult2.getPassword());
		assertEquals("nick should be updated", temp.getNick(),
				queryResult2.getNick());
	}

	@Test(expected = ServiceException.class)
	public void testQueryByKeyNull() {
		userService.queryUserByKey(null);
	}

	public void testQueryByKeyUnexisted() {
		User user = userService.queryUserByKey(-1);
		assertNull("id can't be -1", user);
	}

	@Test(expected = ServiceException.class)
	public void testQueryNull() {
		userService.queryByCondition(null);
	}

	@Test
	public void testQueryNormal() {
		User user = new User();
		user.setName("kimy8");
		user.setNick("An hacker");
		user.setPassword("mars");
		user.setBranchId(1001);
		user.setStatus(ManageConstant.ACTIVE);
		Integer id = userService.saveUser(user);
		assertNotNull("id can't be null", id);
		assertTrue("id must bigger than 0", id > 0);

		UserExample example = new UserExample();
		example.createCriteria().andNameEqualTo("kimy8");

		List<User> users = userService.queryByCondition(example);
		assertNotNull("result should not be null", users);
		assertEquals("just one and must one user matched", users.size(), 1);
		assertEquals("id shoud match", users.get(0).getId(), id);
	}

	@Test
	public void testPasswordUpdate() {
		User user = new User();
		user.setName("kimy");
		user.setNick("An hacker");
		user.setPassword("mars");
		user.setBranchId(1001);
		user.setStatus(ManageConstant.ACTIVE);
		Integer id = userService.saveUser(user);
		assertNotNull("id can't be null", id);
		assertTrue("id must bigger than 0", id > 0);

		User temp = new User();
		temp.setId(id);
		temp.setPassword("wbs");
		Integer updateId = userService.saveUser(temp);
		assertEquals("id can't change after update", id, updateId);

		// check update result
		User queryResult2 = userService.queryUserByKey(id);
		assertEquals("salt can't be change", user.getSalt(),
				queryResult2.getSalt());
		assertNotNull(queryResult2.getPassword());
		assertTrue(!queryResult2.getPassword().equals(user.getPassword()));
	}

	@Test(expected = Exception.class)
	public void testQueryByNameNullParam() {
		userService.queryUserByName(null);
	}

	@Test
	public void testQueryByName() {
		User user = new User();
		user.setName("specialnamehaha");
		user.setNick("An hacker");
		user.setPassword("mars");
		user.setBranchId(1001);
		user.setStatus(ManageConstant.ACTIVE);
		Integer id = userService.saveUser(user);
		assertNotNull("id can't be null", id);
		assertTrue("id must bigger than 0", id > 0);

		User result = userService.queryUserByName("specialnamehaha");
		assertNotNull(result);
		assertEquals(id, result.getId());
	}

	@Test(expected = ServiceException.class)
	public void testDeleteNull() {
		userService.deleteUserById(null);
	}

	@Test
	public void testDelete() {
		User user = new User();
		user.setName("specialnamehaha");
		user.setNick("An hacker");
		user.setPassword("mars");
		user.setBranchId(1001);
		user.setStatus(ManageConstant.ACTIVE);
		Integer id = userService.saveUser(user);

		User beforeDelete = userService.queryUserByKey(id);
		assertNotNull(beforeDelete);
		userService.deleteUserById(id);
		User afterDelete = userService.queryUserByKey(id);
		assertNull(afterDelete);
	}
}
