/*
 * AuthorizationServiceTest.java 2014-8-27
 */
package com.comstar.mars.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.comstar.mars.entity.Privilege;
import com.comstar.mars.entity.PrivilegeExample;
import com.comstar.mars.entity.Role;
import com.comstar.mars.entity.RoleExample;
import com.comstar.mars.entity.RolePrivilege;
import com.comstar.mars.entity.RolePrivilegeExample;
import com.comstar.mars.entity.User;
import com.comstar.mars.entity.UserRole;
import com.comstar.mars.entity.UserRoleExample;
import com.comstar.mars.service.system.AuthorizationService;
import com.comstar.mars.service.system.UserService;

/**
 * AuthorizationService单元测试
 * 
 * @author Li Rong
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
@Transactional
public class AuthorizationServiceTest {
	@Autowired
	private AuthorizationService authorizationService;
	@Autowired
	private UserService userService;

	@Test(expected = Exception.class)
	public void testSaveRoleNull() {
		authorizationService.saveRole(null);
	}

	@Test
	public void testSaveRole() {
		Role role = new Role();
		role.setBranchId(1001);
		role.setDescription("role for test");
		role.setName("role_test");

		Integer id = authorizationService.saveRole(role);
		assertNotNull(id);
		assertTrue(id > 0);

		Role upRole = new Role();
		upRole.setId(id);
		upRole.setName("roleTest");
		Integer upId = authorizationService.saveRole(upRole);
		assertNotNull(upId);
		assertEquals(id, upId);

		Role quRole = authorizationService.queryRoleByKey(id);
		assertNotNull(quRole);
		assertEquals("roleTest", quRole.getName());
	}

	@Test(expected = Exception.class)
	public void testUpdateNotExistedRole() {
		Role role = new Role();
		role.setId(-1);
		role.setName("test_role");
		authorizationService.saveRole(role);
	}

	@Test(expected = Exception.class)
	public void testDeleteRoleNull() {
		authorizationService.deleteRole(null);
	}

	public void testDeleteRole() {
		Role role = new Role();
		role.setBranchId(1001);
		role.setDescription("role for test");
		role.setName("role_test");

		Integer id = authorizationService.saveRole(role);
		assertNotNull(id);
		assertTrue(id > 0);

		authorizationService.deleteRole(id);

		Role quRole = authorizationService.queryRoleByKey(id);
		assertNull(quRole);
	}

	@Test(expected = Exception.class)
	public void testQueryRoleByKeyNull() {
		authorizationService.queryRoleByKey(null);
	}

	@Test(expected = Exception.class)
	public void testQueryRoleByUserIdNull() {
		authorizationService.queryRoleByUserId(null);
	}

	@Test
	public void testQueryRoleByUserId() {
		Role role1 = new Role();
		role1.setBranchId(1001);
		role1.setDescription("role for test");
		role1.setName("role_test");

		Role role2 = new Role();
		role2.setBranchId(1001);
		role2.setDescription("role 2 for test");
		role2.setName("role2_test");

		Integer id1 = authorizationService.saveRole(role1);
		Integer id2 = authorizationService.saveRole(role2);

		UserRole userRole1 = new UserRole();
		userRole1.setUserId(8081);
		userRole1.setRoleId(id1);

		UserRole userRole2 = new UserRole();
		userRole2.setUserId(8081);
		userRole2.setRoleId(id2);

		List<UserRole> userRoles = new ArrayList<UserRole>();
		userRoles.add(userRole1);
		userRoles.add(userRole2);

		authorizationService.saveUserRoleRelationship(userRoles);

		List<Role> roles = authorizationService.queryRoleByUserId(8081);
		assertNotNull(roles);
		assertEquals(2, roles.size());
	}

	@Test(expected = Exception.class)
	public void testQueryRoleByConditionNull() {
		authorizationService.queryRoleByCondition(null);
	}

	@Test
	public void testQueryRoleByCondition() {
		Role role1 = new Role();
		role1.setBranchId(1001);
		role1.setDescription("role for test");
		role1.setName("role_test");

		Role role2 = new Role();
		role2.setBranchId(1001);
		role2.setDescription("role 2 for test");
		role2.setName("role2_test");

		authorizationService.saveRole(role1);
		authorizationService.saveRole(role2);

		RoleExample example = new RoleExample();
		example.createCriteria().andBranchIdEqualTo(1001);

		List<Role> roles = authorizationService.queryRoleByCondition(example);
		assertNotNull(roles);
		assertEquals(2, roles.size());
	}

	@Test(expected = Exception.class)
	public void testSavePrivilegeNull() {
		authorizationService.savePrivilege(null);
	}

	@Test
	public void testSavePrivilege() {
		Privilege privilege = new Privilege();
		privilege.setCategory("权限管理");
		privilege.setCode("user:edit");
		privilege.setName("用户修改");
		privilege.setDescription("新增或修改用户信息");

		Integer id = authorizationService.savePrivilege(privilege);
		assertNotNull(id);
		assertTrue(id > 0);

		Privilege upPrivilege = new Privilege();
		upPrivilege.setId(id);
		upPrivilege.setName("用户新增修改");
		authorizationService.savePrivilege(upPrivilege);

		Privilege quPrivilege = authorizationService.queryPrivilegeByKey(id);
		assertNotNull(quPrivilege);
		assertEquals("用户新增修改", quPrivilege.getName());
	}

	@Test(expected = Exception.class)
	public void testUpdateUnexistedPrivilege() {
		Privilege privilege = new Privilege();
		privilege.setId(-1);
		privilege.setName("用户修改");
		authorizationService.savePrivilege(privilege);
	}

	@Test(expected = Exception.class)
	public void testDeletePrivilegeNull() {
		authorizationService.deletePrivilege(null);
	}

	@Test
	public void testDeletePrivilege() {
		Privilege privilege = new Privilege();
		privilege.setCategory("权限管理");
		privilege.setCode("user:edit");
		privilege.setName("用户修改");
		privilege.setDescription("新增或修改用户信息");

		Integer id = authorizationService.savePrivilege(privilege);

		authorizationService.deletePrivilege(id);

		Privilege quPrivilege = authorizationService.queryPrivilegeByKey(id);
		assertNull(quPrivilege);
	}

	@Test(expected = Exception.class)
	public void testQueryPrivilegeByKeyNull() {
		authorizationService.queryPrivilegeByKey(null);
	}

	@Test(expected = Exception.class)
	public void testQueryPrivilegeByConditionNull() {
		authorizationService.queryPrivilegeByCondition(null);
	}

	@Test
	public void testQueryPrivilegeByCondition() {
		Privilege privilege1 = new Privilege();
		privilege1.setCategory("权限管理1");
		privilege1.setCode("user:edit");
		privilege1.setName("用户修改");
		privilege1.setDescription("新增或修改用户信息");

		Privilege privilege2 = new Privilege();
		privilege2.setCategory("权限管理1");
		privilege2.setCode("user:view");
		privilege2.setName("用户查看");
		privilege2.setDescription("查看用户信息");

		authorizationService.savePrivilege(privilege1);
		authorizationService.savePrivilege(privilege2);

		PrivilegeExample example = new PrivilegeExample();
		example.createCriteria().andCategoryEqualTo("权限管理1");

		List<Privilege> privileges = authorizationService
				.queryPrivilegeByCondition(example);
		assertNotNull(privileges);
		assertEquals(2, privileges.size());
	}

	@Test(expected = Exception.class)
	public void testQueryPrivilegeByRoleIdNull() {
		authorizationService.queryPrivilegeByRoleId(null);
	}

	@Test
	public void testQueryPrivilegeByRoleId() {
		Privilege privilege1 = new Privilege();
		privilege1.setCategory("权限管理");
		privilege1.setCode("user:edit");
		privilege1.setName("用户修改");
		privilege1.setDescription("新增或修改用户信息");

		Privilege privilege2 = new Privilege();
		privilege2.setCategory("权限管理");
		privilege2.setCode("user:view");
		privilege2.setName("用户查看");
		privilege2.setDescription("查看用户信息");

		Integer id1 = authorizationService.savePrivilege(privilege1);
		Integer id2 = authorizationService.savePrivilege(privilege2);

		RolePrivilege rolePrivilege1 = new RolePrivilege();
		rolePrivilege1.setRoleId(7569);
		rolePrivilege1.setPrivilegeId(id1);

		RolePrivilege rolePrivilege2 = new RolePrivilege();
		rolePrivilege2.setRoleId(7569);
		rolePrivilege2.setPrivilegeId(id2);

		List<RolePrivilege> rolePrivileges = new ArrayList<RolePrivilege>();
		rolePrivileges.add(rolePrivilege1);
		rolePrivileges.add(rolePrivilege2);

		authorizationService.saveRolePrivilegeRelationship(rolePrivileges);

		List<Integer> roleIds = new ArrayList<Integer>();
		roleIds.add(7569);

		List<Privilege> privileges = authorizationService
				.queryPrivilegeByRoleId(roleIds);
		assertNotNull(privileges);
		assertEquals(2, privileges.size());
		assertEquals("权限管理", privileges.get(0).getCategory());
	}

	@Test(expected = Exception.class)
	public void testSaveUserRoleNull() {
		authorizationService.saveUserRoleRelationship(null);
	}

	public void testSaveUserRoleEmpty() {
		authorizationService.saveUserRoleRelationship(Collections
				.<UserRole> emptyList());
	}

	@Test
	public void testSaveUserRole() {
		List<UserRole> userRoles = new ArrayList<UserRole>();

		UserRole userRole1 = new UserRole();
		userRole1.setUserId(8111);
		userRole1.setRoleId(39);

		UserRole userRole2 = new UserRole();
		userRole2.setUserId(8111);
		userRole2.setRoleId(86);

		userRoles.add(userRole1);
		userRoles.add(userRole2);

		authorizationService.saveUserRoleRelationship(userRoles);

		UserRoleExample example = new UserRoleExample();
		example.createCriteria().andUserIdEqualTo(8111);

		List<UserRole> result = authorizationService
				.queryUserRoleRelationshipByCondition(example);
		assertNotNull(result);
		assertEquals(2, result.size());
	}

	@Test(expected = Exception.class)
	public void testDeleteUserRoleNull() {
		authorizationService.deleteUserRoleRelationship(null);
	}

	@Test
	public void testDeleteUserRole() {
		List<UserRole> userRoles = new ArrayList<UserRole>();

		UserRole userRole1 = new UserRole();
		userRole1.setUserId(8111);
		userRole1.setRoleId(39);

		UserRole userRole2 = new UserRole();
		userRole2.setUserId(8111);
		userRole2.setRoleId(86);

		userRoles.add(userRole1);
		userRoles.add(userRole2);

		authorizationService.saveUserRoleRelationship(userRoles);

		UserRoleExample example = new UserRoleExample();
		example.createCriteria().andUserIdEqualTo(8111);

		List<UserRole> result = authorizationService
				.queryUserRoleRelationshipByCondition(example);
		assertNotNull(result);
		assertEquals(2, result.size());

		authorizationService.deleteUserRoleRelationship(8111);

		List<UserRole> result2 = authorizationService
				.queryUserRoleRelationshipByCondition(example);
		assertNotNull(result2);
		assertEquals(0, result2.size());
	}

	@Test(expected = Exception.class)
	public void testQueryUserRoleNull() {
		authorizationService.queryUserRoleRelationshipByCondition(null);
	}

	@Test(expected = Exception.class)
	public void testSaveRolePrivilegeNull() {
		authorizationService.saveRolePrivilegeRelationship(null);
	}

	@Test
	public void testSaveRolePrivilegeEmpty() {
		authorizationService.saveRolePrivilegeRelationship(Collections
				.<RolePrivilege> emptyList());
	}

	@Test
	public void testSaveRolePrivilege() {
		List<RolePrivilege> list = new ArrayList<RolePrivilege>();

		RolePrivilege rolePrivilege = new RolePrivilege();
		rolePrivilege.setRoleId(13121);
		rolePrivilege.setPrivilegeId(3891);

		list.add(rolePrivilege);

		authorizationService.saveRolePrivilegeRelationship(list);

		RolePrivilegeExample example = new RolePrivilegeExample();
		example.createCriteria().andRoleIdEqualTo(13121);

		List<RolePrivilege> result = authorizationService
				.queryRolePrivilegeRelationshipByCondition(example);
		assertNotNull(result);
		assertEquals(1, result.size());
	}

	@Test(expected = Exception.class)
	public void testDeleteRolePrivilegeNull() {
		authorizationService.deleteRolePrivilegeRelationship(null);
	}

	@Test
	public void testDeleteRoleprivilege() {
		List<RolePrivilege> list = new ArrayList<RolePrivilege>();

		RolePrivilege rolePrivilege = new RolePrivilege();
		rolePrivilege.setRoleId(13121);
		rolePrivilege.setPrivilegeId(3891);

		list.add(rolePrivilege);

		authorizationService.saveRolePrivilegeRelationship(list);

		RolePrivilegeExample example = new RolePrivilegeExample();
		example.createCriteria().andRoleIdEqualTo(13121);

		List<RolePrivilege> result = authorizationService
				.queryRolePrivilegeRelationshipByCondition(example);
		assertNotNull(result);
		assertEquals(1, result.size());

		authorizationService.deleteRolePrivilegeRelationship(13121);

		List<RolePrivilege> result2 = authorizationService
				.queryRolePrivilegeRelationshipByCondition(example);
		assertNotNull(result2);
		assertEquals(0, result2.size());
	}

	@Test(expected = Exception.class)
	public void testQueryRolePrivilegeNull() {
		authorizationService.queryRolePrivilegeRelationshipByCondition(null);
	}

	@Test(expected = Exception.class)
	public void testCheckNameAndPasswordNull() {
		authorizationService.checkNameAndPassword(null, null);
	}

	@Test
	public void testCheckNameAndPassword() {
		User user = new User();
		user.setBranchId(1002);
		user.setName("wiki");
		user.setPassword("71$9ikiw");
		user.setNick("Wiki Free");

		userService.saveUser(user);

		Boolean result = authorizationService.checkNameAndPassword("wiki",
				"71$9ikiw");
		assertNotNull(result);
		assertTrue(result);
	}
}
