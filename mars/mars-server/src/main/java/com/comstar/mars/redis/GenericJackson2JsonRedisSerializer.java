package com.comstar.mars.redis;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
//import org.springframework.data.redis.serializer.SerializationUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * GenericJackson2JsonRedisSerializer
 * This Class is copied from Spring-redis-cache 1.6.0 RELEASE.
 * Ref: http://www.myexception.cn/database/1958643.html
 */
public class GenericJackson2JsonRedisSerializer implements RedisSerializer<Object> {
    private final ObjectMapper mapper;

    public GenericJackson2JsonRedisSerializer() {
        this((String)null);
    }

    public GenericJackson2JsonRedisSerializer(String classPropertyTypeName) {
        this(new ObjectMapper());
        if(StringUtils.hasText(classPropertyTypeName)) {
            this.mapper.enableDefaultTypingAsProperty(DefaultTyping.NON_FINAL, classPropertyTypeName);
        } else {
            this.mapper.enableDefaultTyping(DefaultTyping.NON_FINAL, As.PROPERTY);
        }

    }

    public GenericJackson2JsonRedisSerializer(ObjectMapper mapper) {
        Assert.notNull(mapper, "ObjectMapper must not be null!");
        this.mapper = mapper;
    }

    public byte[] serialize(Object source) throws SerializationException {
        if(source == null) {
            return SerializationUtils.EMPTY_ARRAY;
        } else {
            try {
                return this.mapper.writeValueAsBytes(source);
            } catch (JsonProcessingException var3) {
                throw new SerializationException("Could not write JSON: " + var3.getMessage(), var3);
            }
        }
    }

    public Object deserialize(byte[] source) throws SerializationException {
        return this.deserialize(source, Object.class);
    }

    public <T> T deserialize(byte[] source, Class<T> type) throws SerializationException {
        Assert.notNull(type, "Deserialization type must not be null! Pleaes provide Object.class to make use of Jackson2 default typing.");
        if(SerializationUtils.isEmpty(source)) {
            return null;
        } else {
            try {
                return this.mapper.readValue(source, type);
            } catch (Exception var4) {
                throw new SerializationException("Could not read JSON: " + var4.getMessage(), var4);
            }
        }
    }
}
