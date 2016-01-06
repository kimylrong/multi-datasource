/*
 * Sha1SecureService.java 2014-8-27
 */
package com.comstar.mars.service.system;

import org.apache.shiro.codec.Hex;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha1Hash;
import org.apache.shiro.util.ByteSource;
import org.springframework.stereotype.Component;

import com.comstar.mars.service.ServiceException;

/**
 * 安全接口SHA-1实现
 * 
 * @author Li Rong
 * @version 1.0
 */
@Component
public class Sha1SecureService implements SecureService {
	@Override
	public String generateSalt() {
		RandomNumberGenerator randomGenerator = new SecureRandomNumberGenerator();
		String salt = randomGenerator.nextBytes().toHex();
		return salt;
	}

	@Override
	public String hash(String target, String salt) {
		if (target == null) {
			throw new ServiceException("target can't be null");
		}

		if (salt == null) {
			throw new ServiceException("salt can't be null");
		}

		byte[] saltBytes = Hex.decode(salt);

		Sha1Hash hash = new Sha1Hash(target, ByteSource.Util.bytes(saltBytes),
				getHashIterationCount());
		String result = hash.toHex();
		return result;
	}

	@Override
	public String getHashAlgorithmsName() {
		return "SHA-1";
	}

	@Override
	public int getHashIterationCount() {
		return 1;
	}

}
