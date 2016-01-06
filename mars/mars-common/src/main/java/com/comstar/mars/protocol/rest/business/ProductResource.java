/*
 * ProductResource.java 2014-7-10
 */
package com.comstar.mars.protocol.rest.business;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.comstar.mars.entity.FinancialProduct;

/**
 * 理财产品Resource
 * 
 * @author Li,Rong
 * @version 1.0
 */
@Path("/product")
public interface ProductResource {
	/**
	 * 浏览理财产品
	 */
	@GET
	@Produces("application/json")
	List<FinancialProduct> index();

	/**
	 * 查看单只理财产品
	 */
	@Path("/{id}")
	@GET
	@Produces("application/json")
	FinancialProduct viewByKey(@PathParam("id") String id);
	
	/**
	 * 新增或者修改产品
	 */
	@POST
	@Produces("application/json")
	@Consumes("application/json")
	void saveProduct(FinancialProduct product);
}
