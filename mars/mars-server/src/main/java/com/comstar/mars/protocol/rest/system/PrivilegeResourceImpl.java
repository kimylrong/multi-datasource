/*
 * PrivilegeResourceImpl.java 2014-9-15
 */
package com.comstar.mars.protocol.rest.system;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.comstar.mars.dto.PrivilegeDto;
import com.comstar.mars.entity.Privilege;
import com.comstar.mars.entity.PrivilegeExample;
import com.comstar.mars.protocol.rest.WebException;
import com.comstar.mars.service.system.AuthorizationService;

/**
 * 权限 REST API实现
 * 
 * @author Li Rong
 * @version 1.0
 */
public class PrivilegeResourceImpl implements PrivilegeResource {
	@Autowired
	private AuthorizationService authorizationService;

	@Override
	public void savePrivilege(PrivilegeDto privilegeDto) {
		Privilege privilege = privilegeDto.toEntity();
		privilege.setId(null);
		authorizationService.savePrivilege(privilege);
	}

	@Override
	public void updatePrivilege(String id, PrivilegeDto privilegeDto) {
		Integer key = null;
		try {
			key = new Integer(id);
		} catch (NumberFormatException e) {
			throw new WebException("invalid product id: " + id);
		}

		Privilege privilege = privilegeDto.toEntity();
		privilege.setId(key);
		authorizationService.savePrivilege(privilege);
	}

	@Override
	public void deletePrivilege(String id) {
		Integer key = null;
		try {
			key = new Integer(id);
		} catch (NumberFormatException e) {
			throw new WebException("invalid product id: " + id);
		}
		authorizationService.deletePrivilege(key);
	}

	@Override
	public List<PrivilegeDto> viewPrivileges() {
		PrivilegeExample example = new PrivilegeExample();
		List<Privilege> privileges = authorizationService
				.queryPrivilegeByCondition(example);

		List<PrivilegeDto> privilegeDtos = new LinkedList<PrivilegeDto>();
		for (Privilege privilege : privileges) {
			PrivilegeDto privilegeDto = PrivilegeDto.fromEntity(privilege);
			privilegeDtos.add(privilegeDto);
		}

		return privilegeDtos;
	}

	@Override
	public PrivilegeDto viewPrivilegeByKey(String id) {
		Integer key = null;
		try {
			key = new Integer(id);
		} catch (NumberFormatException e) {
			throw new WebException("invalid product id: " + id);
		}

		Privilege privilege = authorizationService.queryPrivilegeByKey(key);
		return PrivilegeDto.fromEntity(privilege);
	}

}
