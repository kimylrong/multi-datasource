/*
 * PrivilegeResource.java 2014-9-15
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

/**
 * 权限资源
 * 
 * @author Li Rong
 * @version 1.0
 */
@Path("/privilege")
public interface PrivilegeResource {
	/**
	 * 保存权限定义
	 */
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	void savePrivilege(PrivilegeDto privilegeDto);

	/**
	 * 修改权限
	 */
	@Path("/{id}")
	@PUT
	@Consumes("application/json")
	@Produces("application/json")
	void updatePrivilege(@PathParam("id") String id, PrivilegeDto privilegeDto);

	/**
	 * 删除权限
	 */
	@Path("/{id}")
	@DELETE
	@Consumes("application/json")
	@Produces("application/json")
	void deletePrivilege(@PathParam("id") String id);

	/**
	 * 查询所有权限
	 */
	@GET
	@Produces("application/json")
	List<PrivilegeDto> viewPrivileges();

	/**
	 * By主键查询权限
	 */
	@Path("/{id}")
	@GET
	@Produces("application/json")
	PrivilegeDto viewPrivilegeByKey(@PathParam("id") String id);
}
