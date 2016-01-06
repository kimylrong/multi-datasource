/*
 * MyTableResource.java 2015年12月24日
 */
package com.comstar.mars.protocol.rest.business;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.comstar.mars.entity.MyTable;

/**
 * 
 *
 * @author Li Rong
 */
@Path("/mytable")
@Produces("application/json")
@Consumes("application/json")
public interface MyTableResource {
	@GET
	List<MyTable> getAllData();
}
