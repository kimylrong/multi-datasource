/*
 * RestAccessorDemo.java 2015年12月23日
 */
package com.comstar.mars.protocol.rest;

import java.util.List;

import com.comstar.mars.dto.UserDto;
import com.comstar.mars.protocol.rest.system.UserResource;

/**
 * 
 *
 * @author Li Rong
 */
public class RestAccessorDemo {
	public static void main(String[] args) {
		RestAccessor.setup("https://localhost:8443/api/rest/", "moon", "admin");
		RestAccessor.setSslCertificate("../keystore", "123456");
		
//		System.getProperties().put("javax.net.debug", "all");
		
		UserResource userResource = RestAccessor.buildResource(UserResource.class);
		List<UserDto> users = userResource.index();
		
		System.out.print("MyTable Size:" + users.size());
	}
}
