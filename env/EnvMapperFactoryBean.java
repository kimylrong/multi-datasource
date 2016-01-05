/*
 *    Copyright 2010-2012 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.comstar.mars.env;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.shiro.SecurityUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.comstar.mars.service.system.ShiroRealm.ShiroUser;

/**
 * BeanFactory that enables injection of MyBatis mapper interfaces. It can be
 * set up with a SqlSessionFactory or a pre-configured SqlSessionTemplate.
 * <p>
 * Sample configuration:
 *
 * <pre class="code">
 * {@code
 *   <bean id="baseMapper" class="org.mybatis.spring.mapper.MapperFactoryBean" abstract="true" lazy-init="true">
 *     <property name="sqlSessionFactory" ref="sqlSessionFactory" />
 *   </bean>
 *
 *   <bean id="oneMapper" parent="baseMapper">
 *     <property name="mapperInterface" value="my.package.MyMapperInterface" />
 *   </bean>
 *
 *   <bean id="anotherMapper" parent="baseMapper">
 *     <property name="mapperInterface" value="my.package.MyAnotherMapperInterface" />
 *   </bean>
 * }
 * </pre>
 * <p>
 * Note that this factory can only inject <em>interfaces</em>, not concrete
 * classes.
 *
 * @author Eduardo Macarron
 * 
 * @see SqlSessionTemplate
 * @version $Id$
 */
@SuppressWarnings("rawtypes")
public class EnvMapperFactoryBean<T> implements FactoryBean<T>, ApplicationContextAware {
	private static final Logger log = LoggerFactory.getLogger(EnvMapperFactoryBean.class);

	private Class<T> mapperInterface;
	private ApplicationContext context;

	private static Map<String, Map<String, Object>> envMappers = new ConcurrentHashMap<>();

	/**
	 * Sets the mapper interface of the MyBatis mapper
	 *
	 * @param mapperInterface
	 *            class of the interface
	 */
	public void setMapperInterface(Class<T> mapperInterface) {
		this.mapperInterface = mapperInterface;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public T getObject() throws Exception {
		installEnv();

		return (T) Proxy.newProxyInstance(EnvMapperFactoryBean.class.getClassLoader(),
				new Class<?>[] { mapperInterface }, new MapperProxy());
	}

	public Class<T> getObjectType() {
		return this.mapperInterface;
	}

	public boolean isSingleton() {
		return true;
	}

	private static Object getRealMapper(String env, Class mapperClazz) {
		Map<String, Object> mappers = envMappers.get(env);
		if (mappers.isEmpty()) {
			return null;
		}

		return mappers.get(mapperClazz.getName());
	}

	public static Map<String, Object> getAllMappers(Class mapperClazz) {
		Map<String, Object> result = new HashMap<>();
		String clazzName = mapperClazz.getName();

		for (String env : envMappers.keySet()) {
			Map<String, Object> mappers = envMappers.get(env);
			if (mappers.containsKey(clazzName)) {
				result.put(env, mappers.get(clazzName));
			}
		}

		return result;
	}

	@SuppressWarnings("resource")
	private void installEnv() {
		String[] sqlSessionFactoryNames = context.getBeanNamesForType(SqlSessionFactory.class);
		if (sqlSessionFactoryNames == null || sqlSessionFactoryNames.length == 0) {
			throw new RuntimeException("找不到SqlSessionFactory的配置信息");
		}

		for (String env : sqlSessionFactoryNames) {
			SqlSessionFactory sqlSessionFactory = context.getBean(env, SqlSessionFactory.class);
			SqlSession sqlSession = new SqlSessionTemplate(sqlSessionFactory);
			T mapper = sqlSession.getMapper(mapperInterface);

			if (!envMappers.containsKey(env)) {
				envMappers.put(env, new ConcurrentHashMap<>());
			}

			envMappers.get(env).put(mapperInterface.getName(), mapper);
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		context = applicationContext;
	}

	private class MapperProxy implements InvocationHandler {
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			Object object = null;
			try {
				ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
				if (shiroUser == null) {
					throw new RuntimeException("用户没有登陆，无法确认环境");
				}

				String env = shiroUser.getEnv();

				Object mapper = getRealMapper(env, method.getDeclaringClass());
				if (mapper == null) {
					throw new RuntimeException("找不到对应的mapper");
				}
				object = method.invoke(mapper, args);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				throw e;
			}
			return object;
		}
	}
}
