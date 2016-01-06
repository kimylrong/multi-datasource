/*
 * BranchResourceImpl.java 2014-9-18
 */
package com.comstar.mars.protocol.rest.system;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.comstar.mars.entity.Branch;
import com.comstar.mars.entity.BranchExample;
import com.comstar.mars.protocol.rest.WebException;
import com.comstar.mars.service.system.BranchService;

/**
 * 机构服务实现
 * 
 * @author Li Rong
 * @version 1.0
 */
public class BranchResourceImpl implements BranchResource {
	@Autowired
	private BranchService branchService;

	@Override
	public List<Branch> index() {
		BranchExample example = new BranchExample();
		return branchService.queryBranchByCondition(example);
	}

	@Override
	public Branch viewByKey(String id) {
		Integer key = null;
		try {
			key = new Integer(id);
		} catch (NumberFormatException e) {
			throw new WebException("invalid branch id: " + id);
		}

		return branchService.queryBranchById(key);
	}

	@Override
	public void saveBranch(Branch branch) {
		branch.setId(null);
		branchService.saveBranch(branch);
	}

	@Override
	public void updateBranch(String id, Branch branch) {
		Integer key = null;
		try {
			key = new Integer(id);
		} catch (NumberFormatException e) {
			throw new WebException("invalid product id: " + id);
		}

		branch.setId(key);
		branchService.saveBranch(branch);
	}

	@Override
	public void deleteBranch(String id) {
		Integer key = null;
		try {
			key = new Integer(id);
		} catch (NumberFormatException e) {
			throw new WebException("invalid branch id: " + id);
		}

		branchService.deleteBranch(key);
	}

}
