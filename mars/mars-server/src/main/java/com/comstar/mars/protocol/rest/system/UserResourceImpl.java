/*
 * UserResourceImpl.java 2014-8-26
 */
package com.comstar.mars.protocol.rest.system;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.comstar.mars.dto.RoleDto;
import com.comstar.mars.dto.UserDto;
import com.comstar.mars.entity.Role;
import com.comstar.mars.entity.User;
import com.comstar.mars.entity.UserExample;
import com.comstar.mars.entity.UserRole;
import com.comstar.mars.protocol.rest.WebException;
import com.comstar.mars.service.system.AuthorizationService;
import com.comstar.mars.service.system.UserService;
import com.comstar.mars.util.NumberUtil;

/**
 * 用户资源实现
 * 
 * @author Li Rong
 * @version 1.0
 */
public class UserResourceImpl implements UserResource {
	@Autowired
	private UserService userService;
	@Autowired
	private AuthorizationService authorizationService;

	@Override
	public List<UserDto> index() {
		List<UserDto> userDtos = new ArrayList<UserDto>();

		List<User> users = userService.queryByCondition(new UserExample());
		if (users.isEmpty()) {
			return userDtos;
		}

		for (User user : users) {
			UserDto userDto = UserDto.fromUser(user);
			userDtos.add(userDto);
		}

		return userDtos;
	}

	@Override
	public UserDto viewByKey(String id) {
		Integer key = null;
		try {
			key = new Integer(id);
		} catch (NumberFormatException e) {
			throw new WebException("invalid user id: " + id);
		}

		User user = userService.queryUserByKey(key);
		if (user == null) {
			return null;
		}

		UserDto userDto = UserDto.fromUser(user);

		return userDto;
	}

	@Override
	public void saveUser(UserDto userDto) {
		User user = userDto.toUser();
		user.setId(null);
		userService.saveUser(user);
	}

	@Override
	public void updateUser(String id, UserDto userDto) {
		Integer key = null;
		try {
			key = new Integer(id);
		} catch (NumberFormatException e) {
			throw new WebException("invalid role id: " + id);
		}

		User user = userDto.toUser();
		user.setId(key);
		userService.saveUser(user);
	}

	@Override
	public void deleteUserByKey(String id) {
		Integer key = null;
		try {
			key = new Integer(id);
		} catch (NumberFormatException e) {
			throw new WebException("invalid product id: " + id);
		}

		userService.deleteUserById(key);
	}

	@Override
	public List<RoleDto> fetchUserRole(String id) {
		Integer key = null;
		try {
			key = new Integer(id);
		} catch (NumberFormatException e) {
			throw new WebException("invalid user id: " + id);
		}

		List<Role> roles = authorizationService.queryRoleByUserId(key);

		return getRoleDtoList(roles);
	}

	private List<RoleDto> getRoleDtoList(List<Role> roles) {
		List<RoleDto> userDtos = new LinkedList<RoleDto>();
		for (Role role : roles) {
			userDtos.add(RoleDto.fromRole(role));
		}

		return userDtos;
	}

	@Override
	public void updateUserRole(String id, List<RoleDto> roleDtos) {
		Integer key = null;
		try {
			key = new Integer(id);
		} catch (NumberFormatException e) {
			throw new WebException("invalid user id: " + id);
		}

		authorizationService.deleteUserRoleRelationship(key);

		if (roleDtos != null && roleDtos.size() > 0) {
			List<UserRole> userRoles = new ArrayList<UserRole>();
			for (RoleDto roleDto : roleDtos) {
				UserRole userRole = new UserRole();
				userRole.setUserId(key);
				userRole.setRoleId(NumberUtil.tryParseInt(roleDto.getId()));
				userRoles.add(userRole);
			}

			authorizationService.saveUserRoleRelationship(userRoles);
		}
	}
}
