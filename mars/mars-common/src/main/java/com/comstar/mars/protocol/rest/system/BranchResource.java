/*
 * BranchResource.java 2014-9-18
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

import com.comstar.mars.entity.Branch;

/**
 * 机构管理
 * 
 * @author Li Rong
 * @version 1.0
 */
@Path("branch")
@Produces("application/json")
@Consumes("application/json")
public interface BranchResource {
	/**
	 * 查询所有部门数据
	 */
	@GET
	List<Branch> index();

	/**
	 * By主键查询机构信息
	 */
	@Path("/{id}")
	@GET
	Branch viewByKey(@PathParam("id") String id);

	/**
	 * 新增或者修改机构信息
	 */
	@POST
	void saveBranch(Branch branch);

	/**
	 * 修改机构信息
	 */
	@Path("/{id}")
	@PUT
	void updateBranch(@PathParam("id") String id, Branch branch);

	/**
	 * By主键删除机构
	 */
	@Path("/{id}")
	@DELETE
	void deleteBranch(@PathParam("id") String id);
}
