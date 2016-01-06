/*
 * RoleResource.java 2014-9-23
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

import com.comstar.mars.dto.PrivilegeDto;
import com.comstar.mars.dto.RoleDto;

/**
 * 角色管理
 * 
 * @author Li Rong
 * @version 1.0
 */
@Consumes("application/json")
@Produces("application/json")
@Path("/role")
public interface RoleResource {
	/**
	 * 查询角色列表
	 */
	@GET
	List<RoleDto> index();

	/**
	 * by主键查询角色
	 */
	@Path("/{id}")
	@GET
	RoleDto viewByKey(@PathParam("id") String id);

	/**
	 * 新增角色
	 */
	@POST
	void saveRole(RoleDto roleDto);

	/**
	 * 修改角色
	 */
	@Path("/{id}")
	@PUT
	void updateRole(@PathParam("id") String id, RoleDto roleDto);

	/**
	 * 删除角色
	 */
	@Path("/{id}")
	@DELETE
	void deleteRole(@PathParam("id") String id);

	/**
	 * by主键查询角色对应的权限
	 */
	@Path("/{id}/privilege")
	@GET
	List<PrivilegeDto> fetchRolePrivilege(@PathParam("id") String id);

	/**
	 * 更改角色对应的权限
	 */
	@Path("/{id}/privilege")
	@POST
	void updateRolePrivilege(@PathParam("id") String id,
			List<PrivilegeDto> privilegeDtos);
}
