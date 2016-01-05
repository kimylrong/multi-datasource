/*
 * ShiroRealm.java 2014-8-27
 */
package com.comstar.mars.service.system;

import java.io.Serializable;
import java.security.Principal;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.AllowAllCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.codec.Hex;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.comstar.mars.entity.Privilege;
import com.comstar.mars.entity.Role;
import com.comstar.mars.entity.User;
import com.comstar.mars.entity.UserExample;
import com.comstar.mars.env.EnvMapperFactoryBean;
import com.comstar.mars.repository.UserMapper;
import com.comstar.mars.util.ObjectProcessor;

/**
 * Shiro的安全实现，基于数据库。
 * 
 * @author Li Rong
 * @version 1.0
 */
@Transactional(readOnly = true)
public class ShiroRealm extends AuthorizingRealm {
	@Autowired
	private UserService userService;
	@Autowired
	private SecureService secureService;
	@Autowired
	private AuthorizationService authorizationService;

	@PostConstruct
	public void initAlgorithms() {
		AllowAllCredentialsMatcher credentialsMatcher = new AllowAllCredentialsMatcher();
		setCredentialsMatcher(credentialsMatcher);
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		UsernamePasswordToken pairToken = (UsernamePasswordToken) token;
		String userName = pairToken.getUsername();
		String password = new String(pairToken.getPassword());

		// determine env
		Map<String, Object> envMappers = EnvMapperFactoryBean.getAllMappers(UserMapper.class);
		for (String env : envMappers.keySet()) {
			User user = validUser((UserMapper) envMappers.get(env), userName, password);
			if (user != null) {
				ShiroUser shiroUser = new ShiroUser(user.getId(), userName, user.getBranchId(), env);

				String salt = user.getSalt();
				byte[] saltBytes = Hex.decode(salt);

				return new SimpleAuthenticationInfo(shiroUser, user.getPassword(), ByteSource.Util.bytes(saltBytes),
						getName());
			}
		}

		return null;
	}

	private User validUser(UserMapper userMapper, String userName, String password) {
		UserExample example = new UserExample();
		example.createCriteria().andNameEqualTo(userName);

		List<User> users = userMapper.selectByExample(example);
		if (users.isEmpty()) {
			return null;
		}

		User user = users.get(0);
		String salt = user.getSalt();
		String tempEncoded = secureService.hash(password, salt);
		if (user.getPassword().equals(tempEncoded)) {
			return user;
		} else {
			return null;
		}
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();
		User user = userService.queryUserByName(shiroUser.getName());

		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

		List<Role> roles = authorizationService.queryRoleByUserId(user.getId());
		if (roles.size() > 0) {
			List<String> roleNames = ObjectProcessor.getFieldList(roles,
					new ObjectProcessor.FieldValueGetter<Role, String>() {
						@Override
						public String getValue(Role role) {
							return role.getName();
						}
					});
			info.addRoles(roleNames);

			List<Integer> roleIds = ObjectProcessor.getFieldList(roles,
					new ObjectProcessor.FieldValueGetter<Role, Integer>() {
						@Override
						public Integer getValue(Role role) {
							return role.getId();
						}
					});
			List<Privilege> privileges = authorizationService.queryPrivilegeByRoleId(roleIds);
			if (privileges.size() > 0) {
				List<String> privilegeKeys = ObjectProcessor.getFieldList(privileges,
						new ObjectProcessor.FieldValueGetter<Privilege, String>() {
							@Override
							public String getValue(Privilege item) {
								return item.getCategory() + ":" + item.getCode();
							}
						});
				info.addStringPermissions(privilegeKeys);
			}
		}

		return info;
	}

	public static class ShiroUser implements Serializable, Principal {
		private static final long serialVersionUID = 3316911162161110480L;

		private Integer id;
		private String name;
		private Integer branchId;
		private String env;

		public ShiroUser(Integer id, String name, Integer branchId, String env) {
			this.id = id;
			this.name = name;
			this.branchId = branchId;
			this.env = env;
		}

		public Integer getId() {
			return id;
		}

		public String getName() {
			return name;
		}

		public Integer getBranchId() {
			return branchId;
		}

		public String getEnv() {
			return env;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			ShiroUser other = (ShiroUser) obj;
			if (name == null) {
				if (other.name != null) {
					return false;
				}
			} else if (!name.equals(other.name)) {
				return false;
			}
			return true;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			return result;
		}

		@Override
		public String toString() {
			return name;
		}
	}

}
