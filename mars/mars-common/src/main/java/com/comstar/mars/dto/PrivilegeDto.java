/*
 * PrivilegeDto.java 2014-12-11
 */
package com.comstar.mars.dto;

import org.apache.commons.beanutils.BeanUtils;

import com.comstar.mars.entity.Privilege;
import com.comstar.mars.util.NumberUtil;

/**
 * 
 * @author Li Rong
 * @version 1.0
 */
public class PrivilegeDto {
	private String id;
	private String category;
	private String code;
	private String name;
	private String description;

	public Privilege toEntity() {
		Privilege privilege = new Privilege();
		try {
			BeanUtils.copyProperties(privilege, this);
			privilege.setId(NumberUtil.tryParseInt(this.id));
		} catch (Exception e) {
			throw new RuntimeException(
					"error occurs when tranform from PrivilegeDto to Privilege");
		}
		return privilege;
	}

	public static PrivilegeDto fromEntity(Privilege privilege) {
		PrivilegeDto privilegeDto = new PrivilegeDto();
		try {
			BeanUtils.copyProperties(privilegeDto, privilege);
			privilegeDto.setId(NumberUtil.intToString(privilege.getId()));
		} catch (Exception e) {
			throw new RuntimeException(
					"error occurs when tranform from Privilege to PrivilegeDto");
		}
		return privilegeDto;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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

}
