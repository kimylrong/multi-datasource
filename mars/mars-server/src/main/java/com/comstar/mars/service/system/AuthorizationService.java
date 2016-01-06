/*
 * AuthorizationService.java 2014-8-27
 */
package com.comstar.mars.service.system;

import java.util.List;

import com.comstar.mars.entity.Privilege;
import com.comstar.mars.entity.PrivilegeExample;
import com.comstar.mars.entity.Role;
import com.comstar.mars.entity.RoleExample;
import com.comstar.mars.entity.RolePrivilege;
import com.comstar.mars.entity.RolePrivilegeExample;
import com.comstar.mars.entity.UserRole;
import com.comstar.mars.entity.UserRoleExample;

/**
 * 权限管理服务
 * 
 * @author Li Rong
 * @version 1.0
 */
public interface AuthorizationService {
	/**
	 * 新增或者修改一个角色
	 */
	Integer saveRole(Role role);

	/**
	 * By主键删除角色
	 */
	void deleteRole(Integer id);

	/**
	 * By主键查询角色
	 */
	Role queryRoleByKey(Integer key);

	/**
	 * By用户ID查角色列表
	 * 
	 * @param userId
	 *            用户ID
	 */
	List<Role> queryRoleByUserId(Integer userId);

	/**
	 * By条件查询角色
	 */
	List<Role> queryRoleByCondition(RoleExample example);

	/**
	 * 新增或者修改权限
	 */
	Integer savePrivilege(Privilege privilege);

	/**
	 * By主键删除权限
	 */
	void deletePrivilege(Integer id);

	/**
	 * By主键查询权限
	 */
	Privilege queryPrivilegeByKey(Integer key);

	/**
	 * By角色ID列表查权限列表
	 * 
	 * @param roleIds
	 *            角色ID列表
	 */
	List<Privilege> queryPrivilegeByRoleId(List<Integer> roleIds);

	/**
	 * By条件查询权限
	 */
	List<Privilege> queryPrivilegeByCondition(PrivilegeExample example);

	/**
	 * 保存用户角色的关联关系
	 */
	void saveUserRoleRelationship(List<UserRole> userRoles);

	/**
	 * By用户ID删除用户角色关系
	 */
	void deleteUserRoleRelationship(Integer userId);
	
	/**
	 * By条件查询UserRole
	 */
	List<UserRole> queryUserRoleRelationshipByCondition(UserRoleExample condition);

	/**
	 * 保存角色权限的关联关系
	 */
	void saveRolePrivilegeRelationship(List<RolePrivilege> rolePrivileges);

	/**
	 * By角色ID删除角色权限关系
	 */
	void deleteRolePrivilegeRelationship(Integer roleId);
	
	/**
	 * By条件查询Roleprivilege
	 */
	List<RolePrivilege> queryRolePrivilegeRelationshipByCondition(RolePrivilegeExample condition);
	
	/**
	 * 校验用户名和密码
	 */
	Boolean checkNameAndPassword(String userName, String password);
}
