/*
 * AuthorizationResource.java 2014-9-4
 */
package com.comstar.mars.protocol.rest.system;

import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.comstar.mars.entity.User;

/**
 * 权限管理
 * 
 * @author Li Rong
 * @version 1.0
 */
@Path("auth")
@Produces("application/json")
@Consumes("application/json")
public interface AuthorizationResource {
	String VALID_KEY = "valid";
	String ROLE = "role";
	String PRIVILEGE = "privilege";

	/**
	 * 验证用户的有效性。Map的key值为：valid
	 * 之所以要封装到MAP，是由于JSON本身为key-value格式，仅反回Boolean会造成Confuse
	 */
	@Path("check")
	@POST
	Map<String, Boolean> checkValid(User user);

	/**
	 * By用户名获取角色和权限列表 用户不存在，抛异常。
	 */
	@Path("userinfo/{id}")
	@GET
	Map<String, List<String>> getRoleAndPrivilege(@PathParam("id") String userId);
	
	@Path("login")
	@POST
	void login(User user);
	
	@Path("logout")
	@GET
	void logout();
}
