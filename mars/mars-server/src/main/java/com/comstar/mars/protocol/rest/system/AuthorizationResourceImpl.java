/*
 * AuthorizationResourceImpl.java 2014-9-4
 */
package com.comstar.mars.protocol.rest.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import com.comstar.mars.entity.Privilege;
import com.comstar.mars.entity.Role;
import com.comstar.mars.entity.User;
import com.comstar.mars.protocol.rest.WebException;
import com.comstar.mars.service.system.AuthorizationService;
import com.comstar.mars.service.system.UserService;
import com.comstar.mars.util.ObjectProcessor;

/**
 * 授权
 * 
 * @author Li Rong
 * @version 1.0
 */
public class AuthorizationResourceImpl implements AuthorizationResource {
	@Autowired
	private UserService userService;
	@Autowired
	private AuthorizationService authorizationService;

	@Override
	public Map<String, Boolean> checkValid(User user) {
		Boolean valid = authorizationService.checkNameAndPassword(user.getName(), user.getPassword());
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		map.put(VALID_KEY, valid);
		return map;
	}

	@Override
	public Map<String, List<String>> getRoleAndPrivilege(String userId) {
		Integer key = null;
		try {
			key = new Integer(userId);
		} catch (NumberFormatException e) {
			throw new WebException("invalid user id: " + userId);
		}

		User user = userService.queryUserByKey(key);
		if (user == null) {
			throw new WebException("用户不存在:" + userId);
		}

		Map<String, List<String>> data = new HashMap<String, List<String>>();

		List<Role> roles = authorizationService.queryRoleByUserId(user.getId());
		List<String> roleNames = ObjectProcessor.getFieldList(roles,
				new ObjectProcessor.FieldValueGetter<Role, String>() {
					@Override
					public String getValue(Role role) {
						return role.getName();
					}
				});
		data.put(ROLE, roleNames);

		if (roles.size() > 0) {
			List<Integer> roleIds = ObjectProcessor.getFieldList(roles,
					new ObjectProcessor.FieldValueGetter<Role, Integer>() {
						@Override
						public Integer getValue(Role role) {
							return role.getId();
						}
					});
			List<Privilege> privileges = authorizationService.queryPrivilegeByRoleId(roleIds);
			List<String> privilegeKeys = ObjectProcessor.getFieldList(privileges,
					new ObjectProcessor.FieldValueGetter<Privilege, String>() {
						@Override
						public String getValue(Privilege item) {
							return item.getCategory() + ":" + item.getCode();
						}
					});
			data.put(PRIVILEGE, privilegeKeys);
		} else {
			data.put(PRIVILEGE, new ArrayList<String>());
		}

		return data;
	}

	@Override
	public void login(User user) {
		Subject subject = SecurityUtils.getSubject();
		subject.login(new UsernamePasswordToken(user.getName(), user.getPassword()));
	}

	@Override
	public void logout() {
		Subject subject = SecurityUtils.getSubject();
		if (subject != null) {
			subject.logout();
		}
	}
}
