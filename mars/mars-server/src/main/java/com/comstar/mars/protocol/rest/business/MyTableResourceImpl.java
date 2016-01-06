/*
 * MyTableResourceImpl.java 2015年12月24日
 */
package com.comstar.mars.protocol.rest.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.comstar.mars.entity.MyTable;
import com.comstar.mars.service.business.MyTableService;

/**
 * 
 *
 * @author Li Rong
 */
public class MyTableResourceImpl implements MyTableResource {
	@Autowired
	private MyTableService myTableService;
	
	@Override
	public List<MyTable> getAllData() {
		return myTableService.getAllData();
	}

}
