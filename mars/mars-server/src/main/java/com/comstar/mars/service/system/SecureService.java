/*
 * SecureService.java 2014-8-27
 */
package com.comstar.mars.service.system;

/**
 * 安全接口,提供hash以及加密功能
 * 
 * @author Li Rong
 * @version 1.0
 */
public interface SecureService {
	/**
	 * 创建一个salt
	 */
	String generateSalt();

	/**
	 * Hash算法名称
	 */
	String getHashAlgorithmsName();

	/**
	 * Hash迭代次数
	 */
	int getHashIterationCount();

	/**
	 * hash一个字符串
	 * 
	 * @param target
	 *            目标字符串
	 * @param salt
	 */
	String hash(String target, String salt);
}
