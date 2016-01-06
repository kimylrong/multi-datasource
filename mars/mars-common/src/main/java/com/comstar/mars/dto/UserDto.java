/*
 * UserDto.java 2014-9-22
 */
package com.comstar.mars.dto;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.converters.IntegerConverter;

import com.comstar.mars.entity.User;
import com.comstar.mars.util.NumberUtil;

/**
 * 用户信息传输类
 * 
 * @author Li Rong
 * @version 1.0
 */
public class UserDto {
	private String id;
	private String name;
	private String nick;
	private String password;
	private String salt;
	private Integer branchId;
	private String status;

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

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public Integer getBranchId() {
		return branchId;
	}

	public void setBranchId(Integer branchId) {
		this.branchId = branchId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public User toUser() {
		User user = new User();
		try {
			IntegerConverter converter = new IntegerConverter(null); 
			BeanUtilsBean beanUtilsBean = new BeanUtilsBean();
			beanUtilsBean.getConvertUtils().register(converter, Integer.class);
			beanUtilsBean.copyProperties(user, this);
			user.setId(NumberUtil.tryParseInt(this.id));
		} catch (Exception e) {
			throw new RuntimeException(
					"error occurs when tranform from UserDto to User");
		}
		return user;
	}

	public static UserDto fromUser(User user) {
		UserDto userDto = new UserDto();
		try {
			BeanUtils.copyProperties(userDto, user);
			// do not transfer password
			userDto.setPassword("");
			userDto.setId(NumberUtil.intToString(user.getId()));
		} catch (Exception e) {
			throw new RuntimeException(
					"error occurs when tranform from User to UserDto");
		}
		return userDto;
	}

}
