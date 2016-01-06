package com.comstar.mars.redis;

import java.nio.charset.Charset;

/**
 * Created by arnes on 2015/10/15.
 *
 * Redis Key Serializer. add system name as prefix to redis key.
 *
 */
public class StringRedisSerializer extends org.springframework.data.redis.serializer.StringRedisSerializer {
	private final static String SYSTEM_NAME = "TEMPLATE||";

	public StringRedisSerializer() {
		super();
	}

	public StringRedisSerializer(Charset charset) {
		super(charset);
	}

	public String deserialize(byte[] bytes) {
		String string = super.deserialize(bytes);
		if (string.startsWith(SYSTEM_NAME)) {
			string = string.substring(SYSTEM_NAME.length());
		} else {
			return null;
		}
		return SYSTEM_NAME + string;
	}

	public byte[] serialize(String string) {
		return super.serialize(SYSTEM_NAME + string);
	}

}
