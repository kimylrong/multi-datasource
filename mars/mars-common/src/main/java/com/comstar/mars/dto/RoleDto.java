/*
 * RoleDto.java 2014-9-23
 */
package com.comstar.mars.dto;

import org.apache.commons.beanutils.BeanUtils;

import com.comstar.mars.entity.Role;
import com.comstar.mars.util.NumberUtil;

/**
 * 角色信息传输类
 * 
 * @author Li Rong
 * @version 1.0
 */
public class RoleDto {
	private String id;
	private String name;
	private String description;
	private Integer branchId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getBranchId() {
		return branchId;
	}

	public void setBranchId(Integer branchId) {
		this.branchId = branchId;
	}

	public Role toRole() {
		Role role = new Role();
		try {
			BeanUtils.copyProperties(role, this);
			role.setId(NumberUtil.tryParseInt(this.id));
		} catch (Exception e) {
			throw new RuntimeException(
					"error occurs when tranform from RoleDto to Role");
		}
		return role;
	}

	public static RoleDto fromRole(Role role) {
		RoleDto roleDto = new RoleDto();
		try {
			BeanUtils.copyProperties(roleDto, role);
			roleDto.setId(NumberUtil.intToString(role.getId()));
		} catch (Exception e) {
			throw new RuntimeException(
					"error occurs when tranform from Role to RoleDto");
		}
		return roleDto;
	}

}
