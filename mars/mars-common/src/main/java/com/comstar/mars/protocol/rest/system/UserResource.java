/*
 * UserResource.java 2014-8-26
 */
package com.comstar.mars.protocol.rest.system;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.comstar.mars.dto.RoleDto;
import com.comstar.mars.dto.UserDto;

/**
 * 用户Resource
 * 
 * @author Li Rong
 * @version 1.0
 */
@Path("/user")
@Consumes("application/json")
@Produces("application/json")
public interface UserResource {
	/**
	 * 查看所有用户
	 */
	@GET
	@Produces("application/json")
	List<UserDto> index();

	/**
	 * By id查询用户
	 */
	@Path("/{id}")
	@GET
	UserDto viewByKey(@PathParam("id") String id);

	/**
	 * 新增用户信息
	 */
	@POST
	void saveUser(UserDto userDto);

	/**
	 * 修改用户信息
	 */
	@Path("/{id}")
	@PUT
	void updateUser(@PathParam("id") String id, UserDto userDto);

	@Path("/{id}")
	@DELETE
	void deleteUserByKey(@PathParam("id") String id);

	/**
	 * by主键查询用户对应的角色
	 */
	@Path("/{id}/role")
	@GET
	List<RoleDto> fetchUserRole(@PathParam("id") String id);

	/**
	 * 更改角色对应的权限
	 */
	@Path("/{id}/role")
	@POST
	void updateUserRole(@PathParam("id") String id, List<RoleDto> roleDtos);
}
