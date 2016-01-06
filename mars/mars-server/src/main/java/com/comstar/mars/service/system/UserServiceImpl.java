/*
 * UserServiceImpl.java 2014-8-26
 */
package com.comstar.mars.service.system;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.comstar.mars.entity.User;
import com.comstar.mars.entity.UserExample;
import com.comstar.mars.repository.UserMapper;
import com.comstar.mars.service.ServiceException;

/**
 * 用户管理实现
 * 
 * @author Li Rong
 * @version 1.0
 */
@Component
@Transactional
public class UserServiceImpl implements UserService {
	private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private SecureService secureService;

	@Override
	public Integer saveUser(User user) {
		if (user == null) {
			throw new ServiceException("user can't be null");
		}

		Integer id = user.getId();
		if (id == null) {
			String salt = secureService.generateSalt();
			user.setSalt(salt);
			hashPassword(user);
			userMapper.insert(user);
			LOG.info(String.format("创建用户(name=%s)", user.getName()));
		} else {
			User temp = userMapper.selectByPrimaryKey(id);
			if (temp == null) {
				throw new ServiceException(String.format("user does not exist:(id=%s)", id));
			}

			if (user.getPassword() != null) {
				user.setSalt(temp.getSalt());
				hashPassword(user);
			}
			userMapper.updateByPrimaryKeySelective(user);
			LOG.info(String.format("修改用户(name=%s)", user.getName()));
		}
		return user.getId();
	}

	private void hashPassword(User user) {
		String password = user.getPassword();
		String salt = user.getSalt();
		String hashedPassword = secureService.hash(password, salt);
		user.setPassword(hashedPassword);
	}

	@Override
	public User queryUserByKey(Integer key) {
		if (key == null) {
			throw new ServiceException("user id can't be null");
		}
		return userMapper.selectByPrimaryKey(key);
	}

	@Override
	public List<User> queryByCondition(UserExample condition) {
		if (condition == null) {
			throw new ServiceException("product query condition can't be null");
		}
		return userMapper.selectByExample(condition);
	}

	@Override
	public User queryUserByName(String name) {
		if (StringUtils.isEmpty(name)) {
			throw new ServiceException("name can't be null or empty");
		}

		UserExample example = new UserExample();
		example.createCriteria().andNameEqualTo(name);

		List<User> users = userMapper.selectByExample(example);
		if (users.size() > 0) {
			return users.get(0);
		} else {
			return null;
		}
	}

	@Override
	public void deleteUserById(Integer id) {
		userMapper.deleteByPrimaryKey(id);
	}

	@Override
	public boolean validUser(String userName, String password) {
		User user = queryUserByName(userName);
		if (user == null) {
			return false;
		}

		String salt = user.getSalt();
		String tempEncoded = secureService.hash(password, salt);
		return user.getPassword().equals(tempEncoded);
	}
}
