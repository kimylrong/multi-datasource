/*
 * UserService.java 2014-8-26
 */
package com.comstar.mars.service.system;

import java.util.List;

import com.comstar.mars.entity.User;
import com.comstar.mars.entity.UserExample;

/**
 * 用户管理接口
 * 
 * @author Li Rong
 * @version 1.0
 */
public interface UserService {
	/**
	 * 新增或者修改用户信息
	 * 
	 * @return 用户ID
	 */
	Integer saveUser(User user);

	/**
	 * By主键查询用户
	 */
	User queryUserByKey(Integer key);

	/**
	 * By 用户名查询用户
	 */
	User queryUserByName(String name);

	/**
	 * By查询条件查询用户
	 */
	List<User> queryByCondition(UserExample condition);

	/**
	 * By主键删除用户
	 */
	void deleteUserById(Integer id);

	/**
	 * 是否是合法的用户
	 * 
	 * @param userName
	 * @param password
	 * @return
	 */
	boolean validUser(String userName, String password);
}
