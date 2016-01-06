/*
 * BranchServiceImpl.java 2014-9-18
 */
package com.comstar.mars.service.system;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.comstar.mars.entity.Branch;
import com.comstar.mars.entity.BranchExample;
import com.comstar.mars.repository.BranchMapper;
import com.comstar.mars.service.ServiceException;

/**
 * 机构服务实现
 * 
 * @author Li Rong
 * @version 1.0
 */
@Component
@Transactional
public class BranchServiceImpl implements BranchService {
	@Autowired
	private BranchMapper branchMapper;

	@Override
	public void saveBranch(Branch branch) {
		if (branch == null) {
			throw new ServiceException("param branch can't be null");
		}

		Integer id = branch.getId();
		if (id == null) {
			branchMapper.insert(branch);
		} else {
			branchMapper.updateByPrimaryKeySelective(branch);
		}
	}

	@Override
	public void deleteBranch(Integer id) {
		if (id == null) {
			throw new ServiceException("param id can't be null");
		}

		branchMapper.deleteByPrimaryKey(id);
	}

	@Override
	public Branch queryBranchByCode(String code) {
		if (code == null) {
			throw new ServiceException("param code can't be null");
		}

		BranchExample branchExample = new BranchExample();
		branchExample.createCriteria().andCodeEqualTo(code);

		List<Branch> list = branchMapper.selectByExample(branchExample);

		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	@Override
	public Branch queryBranchById(Integer id) {
		if (id == null) {
			throw new ServiceException("param id can't be null");
		}

		return branchMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<Branch> queryBranchByCondition(BranchExample example) {
		if (example == null) {
			throw new ServiceException("param example can't be null");
		}

		return branchMapper.selectByExample(example);
	}

}
