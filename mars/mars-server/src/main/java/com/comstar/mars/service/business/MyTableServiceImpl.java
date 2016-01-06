/*
 * MyTableServiceImpl.java 2015年12月24日
 */
package com.comstar.mars.service.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.comstar.mars.entity.MyTable;
import com.comstar.mars.entity.MyTableExample;
import com.comstar.mars.repository.MyTableMapper;

/**
 * 
 *
 * @author Li Rong
 */
@Component
@Transactional
public class MyTableServiceImpl implements MyTableService {
	@Autowired
	private MyTableMapper myTableMapper;

	@Override
	public List<MyTable> getAllData() {
		return myTableMapper.selectByExample(new MyTableExample());
	}

}
