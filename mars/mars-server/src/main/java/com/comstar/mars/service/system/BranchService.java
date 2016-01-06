/*
 * BranchService.java 2014-9-18
 */
package com.comstar.mars.service.system;

import java.util.List;

import com.comstar.mars.entity.Branch;
import com.comstar.mars.entity.BranchExample;

/**
 * 机构管理服务
 * 
 * @author Li Rong
 * @version 1.0
 */
public interface BranchService {
	/**
	 * 新增或者修改机构信息
	 */
	void saveBranch(Branch branch);
	
	/**
	 * BY主键删除机构
	 */
	void deleteBranch(Integer id);
	
	/**
	 * By代码查询机构
	 */
	Branch queryBranchByCode(String code);
	
	/**
	 * By主键查询机构
	 */
	Branch queryBranchById(Integer id);
	
	/**
	 * By条件查询机构
	 */
	List<Branch> queryBranchByCondition(BranchExample example);
}
