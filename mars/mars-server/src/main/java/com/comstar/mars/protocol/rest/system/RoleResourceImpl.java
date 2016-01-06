/*
 * RoleResourceImpl.java 2014-9-23
 */
package com.comstar.mars.protocol.rest.system;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.comstar.mars.dto.PrivilegeDto;
import com.comstar.mars.dto.RoleDto;
import com.comstar.mars.entity.Privilege;
import com.comstar.mars.entity.Role;
import com.comstar.mars.entity.RoleExample;
import com.comstar.mars.entity.RolePrivilege;
import com.comstar.mars.protocol.rest.WebException;
import com.comstar.mars.service.system.AuthorizationService;

/**
 * 角色处理实现
 * 
 * @author Li Rong
 * @version 1.0
 */
public class RoleResourceImpl implements RoleResource {
	@Autowired
	private AuthorizationService authorizationService;

	@Override
	public List<RoleDto> index() {
		List<RoleDto> roleDtos = new ArrayList<RoleDto>();

		List<Role> roles = authorizationService
				.queryRoleByCondition(new RoleExample());
		if (roles.isEmpty()) {
			return roleDtos;
		}

		for (Role role : roles) {
			RoleDto roleDto = RoleDto.fromRole(role);
			roleDtos.add(roleDto);
		}

		return roleDtos;
	}

	@Override
	public RoleDto viewByKey(String id) {
		Integer key = null;
		try {
			key = new Integer(id);
		} catch (NumberFormatException e) {
			throw new WebException("invalid role id: " + id);
		}

		Role role = authorizationService.queryRoleByKey(key);
		if (role == null) {
			return null;
		}

		RoleDto roleDto = RoleDto.fromRole(role);

		return roleDto;
	}

	@Override
	public void saveRole(RoleDto roleDto) {
		Role role = roleDto.toRole();
		role.setId(null);
		authorizationService.saveRole(role);
	}

	@Override
	public void updateRole(String id, RoleDto roleDto) {
		Integer key = null;
		try {
			key = new Integer(id);
		} catch (NumberFormatException e) {
			throw new WebException("invalid role id: " + id);
		}

		Role role = roleDto.toRole();
		role.setId(key);
		authorizationService.saveRole(role);
	}

	@Override
	public void deleteRole(String id) {
		Integer key = null;
		try {
			key = new Integer(id);
		} catch (NumberFormatException e) {
			throw new WebException("invalid role id: " + id);
		}

		authorizationService.deleteRole(key);
	}

	@Override
	public List<PrivilegeDto> fetchRolePrivilege(String id) {
		Integer key = null;
		try {
			key = new Integer(id);
		} catch (NumberFormatException e) {
			throw new WebException("invalid id: " + id);
		}

		List<Integer> roleIds = new ArrayList<Integer>();
		roleIds.add(key);
		List<Privilege> privileges = authorizationService
				.queryPrivilegeByRoleId(roleIds);

		List<PrivilegeDto> privilegeDtos = new LinkedList<PrivilegeDto>();
		for (Privilege privilege : privileges) {
			PrivilegeDto privilegeDto = PrivilegeDto.fromEntity(privilege);
			privilegeDtos.add(privilegeDto);
		}

		return privilegeDtos;
	}

	@Override
	public void updateRolePrivilege(String id, List<PrivilegeDto> privilegeDtos) {
		Integer key = null;
		try {
			key = new Integer(id);
		} catch (NumberFormatException e) {
			throw new WebException("invalid id: " + id);
		}

		// 关系都是先删后增，全量处理
		authorizationService.deleteRolePrivilegeRelationship(key);

		if (privilegeDtos != null && privilegeDtos.size() > 0) {
			List<RolePrivilege> rolePrivileges = new LinkedList<RolePrivilege>();
			for (PrivilegeDto privilegeDto : privilegeDtos) {
				RolePrivilege rolePrivilege = new RolePrivilege();
				rolePrivilege.setRoleId(key);
				rolePrivilege.setPrivilegeId(Integer.parseInt(privilegeDto
						.getId()));
				rolePrivileges.add(rolePrivilege);
			}

			authorizationService.saveRolePrivilegeRelationship(rolePrivileges);
		}
	}
}
