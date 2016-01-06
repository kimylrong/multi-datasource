/*
 * AuthorizationServiceImpl.java 2014-8-27
 */
package com.comstar.mars.service.system;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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
import com.comstar.mars.repository.PrivilegeMapper;
import com.comstar.mars.repository.RoleMapper;
import com.comstar.mars.repository.RolePrivilegeMapper;
import com.comstar.mars.repository.UserRoleMapper;
import com.comstar.mars.service.ServiceException;
import com.comstar.mars.util.ObjectProcessor;

/**
 * 权限管理服务实现
 * 
 * @author Li Rong
 * @version 1.0
 */
@Transactional
@Component
public class AuthorizationServiceImpl implements AuthorizationService {
	@Autowired
	private RoleMapper roleMapper;
	@Autowired
	private PrivilegeMapper privilegeMapper;
	@Autowired
	private UserRoleMapper userRoleMapper;
	@Autowired
	private RolePrivilegeMapper rolePrivilegeMapper;
	@Autowired
	private SecureService secureService;
	@Autowired
	private UserService userService;

	@Override
	public Integer saveRole(Role role) {
		if (role == null) {
			throw new ServiceException("role can't be null");
		}

		Integer id = role.getId();
		if (id == null) {
			roleMapper.insert(role);
		} else {
			Role temp = roleMapper.selectByPrimaryKey(id);
			if (temp == null) {
				throw new ServiceException(String.format(
						"update a unexisted role(roleId=%s)", id));
			}

			roleMapper.updateByPrimaryKeySelective(role);
		}

		return role.getId();
	}

	@Override
	public void deleteRole(Integer id) {
		if (id == null) {
			throw new ServiceException("role id can't be null");
		}
		roleMapper.deleteByPrimaryKey(id);
	}

	@Override
	public Role queryRoleByKey(Integer key) {
		if (key == null) {
			throw new ServiceException("role id can't be null");
		}
		return roleMapper.selectByPrimaryKey(key);
	}

	@Override
	public List<Role> queryRoleByUserId(Integer userId) {
		if (userId == null) {
			throw new ServiceException("user id can't be null");
		}

		UserRoleExample userRoleExample = new UserRoleExample();
		userRoleExample.createCriteria().andUserIdEqualTo(userId);
		List<UserRole> userRoles = userRoleMapper
				.selectByExample(userRoleExample);
		if (userRoles.isEmpty()) {
			return Collections.emptyList();
		}

		List<Integer> roleIds = ObjectProcessor.getFieldList(userRoles,
				new ObjectProcessor.FieldValueGetter<UserRole, Integer>() {
					@Override
					public Integer getValue(UserRole item) {
						return item.getRoleId();
					}
				});

		RoleExample roleExample = new RoleExample();
		roleExample.createCriteria().andIdIn(roleIds);

		return roleMapper.selectByExample(roleExample);
	}

	@Override
	public List<Role> queryRoleByCondition(RoleExample example) {
		if (example == null) {
			throw new ServiceException("condition can't be null");
		}
		return roleMapper.selectByExample(example);
	}

	@Override
	public Integer savePrivilege(Privilege privilege) {
		if (privilege == null) {
			throw new ServiceException("privilege can't be null");
		}

		Integer id = privilege.getId();
		if (id == null) {
			privilegeMapper.insert(privilege);
		} else {
			Privilege temp = privilegeMapper.selectByPrimaryKey(id);
			if (temp == null) {
				throw new ServiceException(String.format(
						"update an unexisted privilege(id=%s)", id));
			}
			privilegeMapper.updateByPrimaryKeySelective(privilege);
		}
		return privilege.getId();
	}

	@Override
	public void deletePrivilege(Integer id) {
		if (id == null) {
			throw new ServiceException("id can't be null");
		}

		privilegeMapper.deleteByPrimaryKey(id);
	}

	@Override
	public Privilege queryPrivilegeByKey(Integer key) {
		if (key == null) {
			throw new ServiceException("key can't be null");
		}
		return privilegeMapper.selectByPrimaryKey(key);
	}

	@Override
	public List<Privilege> queryPrivilegeByRoleId(List<Integer> roleIds) {
		if (roleIds == null) {
			throw new ServiceException("role id list can't be null");
		}

		if (roleIds.isEmpty()) {
			return Collections.emptyList();
		}

		RolePrivilegeExample rolePrivilegeExample = new RolePrivilegeExample();
		rolePrivilegeExample.createCriteria().andRoleIdIn(roleIds);

		List<RolePrivilege> rolePrivileges = rolePrivilegeMapper
				.selectByExample(rolePrivilegeExample);
		if (rolePrivileges.isEmpty()) {
			return Collections.emptyList();
		}

		List<Integer> privilegeIds = ObjectProcessor.getFieldList(
				rolePrivileges,
				new ObjectProcessor.FieldValueGetter<RolePrivilege, Integer>() {
					@Override
					public Integer getValue(RolePrivilege item) {
						return item.getPrivilegeId();
					}
				});

		PrivilegeExample privilegeExample = new PrivilegeExample();
		privilegeExample.createCriteria().andIdIn(privilegeIds);

		return privilegeMapper.selectByExample(privilegeExample);
	}

	@Override
	public List<Privilege> queryPrivilegeByCondition(PrivilegeExample example) {
		if (example == null) {
			throw new ServiceException("condition can't be null");
		}
		return privilegeMapper.selectByExample(example);
	}

	@Override
	public void saveUserRoleRelationship(List<UserRole> userRoles) {
		if (userRoles == null) {
			throw new ServiceException("param can't be null");
		}

		if (userRoles.isEmpty()) {
			return;
		}

		for (UserRole item : userRoles) {
			userRoleMapper.insert(item);
		}
	}

	@Override
	public void deleteUserRoleRelationship(Integer userId) {
		if (userId == null) {
			throw new ServiceException("user id can't be null");
		}

		UserRoleExample example = new UserRoleExample();
		example.createCriteria().andUserIdEqualTo(userId);

		userRoleMapper.deleteByExample(example);
	}

	@Override
	public List<UserRole> queryUserRoleRelationshipByCondition(
			UserRoleExample condition) {
		if (condition == null) {
			throw new ServiceException("condition can't be null");
		}

		return userRoleMapper.selectByExample(condition);
	}

	@Override
	public void saveRolePrivilegeRelationship(List<RolePrivilege> rolePrivileges) {
		if (rolePrivileges == null) {
			throw new ServiceException("param can't be null");
		}

		if (rolePrivileges.isEmpty()) {
			return;
		}

		for (RolePrivilege item : rolePrivileges) {
			rolePrivilegeMapper.insert(item);
		}
	}

	@Override
	public void deleteRolePrivilegeRelationship(Integer roleId) {
		if (roleId == null) {
			throw new ServiceException("role id can't be null");
		}

		RolePrivilegeExample example = new RolePrivilegeExample();
		example.createCriteria().andRoleIdEqualTo(roleId);

		rolePrivilegeMapper.deleteByExample(example);
	}

	@Override
	public List<RolePrivilege> queryRolePrivilegeRelationshipByCondition(
			RolePrivilegeExample condition) {
		if (condition == null) {
			throw new ServiceException("condition can't be null");
		}

		return rolePrivilegeMapper.selectByExample(condition);
	}

	@Override
	public Boolean checkNameAndPassword(String userName, String password) {
		if (userName == null) {
			throw new ServiceException("user name can't be null");
		}

		if (password == null) {
			throw new ServiceException("password name can't be null");
		}

		User user = userService.queryUserByName(userName);
		if (user == null) {
			return false;
		}

		String passwordStored = user.getPassword();
		String salt = user.getSalt();

		String passwordHashed = secureService.hash(password, salt);
		return passwordStored.equals(passwordHashed);
	}
}
