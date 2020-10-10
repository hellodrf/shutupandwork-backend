package com.cervidae.shutupandwork.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.Set;

@Service
@Scope("prototype")
public class ICacheImplRedis<V extends Serializable> implements ICache<Serializable> {

    private final RedisTemplate<String, Serializable> serializableRedisTemplate;

    @Autowired
    public ICacheImplRedis(RedisTemplate<String, Serializable> serializableRedisTemplate) {
        this.serializableRedisTemplate = serializableRedisTemplate;
    }

    /**
     * Set the redis database we are using (0-15)
     * Default database (currently 0) see applications.properties
     * @param identifier redis database identifier
     */
    @Override
    public void setIdentifier(int identifier) {
        Assert.isTrue(identifier < 16, "1005");
        LettuceConnectionFactory lettuceConnectionFactory =
                (LettuceConnectionFactory) serializableRedisTemplate.getConnectionFactory();
        Assert.notNull(lettuceConnectionFactory, "1005");
        lettuceConnectionFactory.setDatabase(identifier);
        lettuceConnectionFactory.resetConnection();
        serializableRedisTemplate.setConnectionFactory(lettuceConnectionFactory);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public V select(String key) {
        return (V) serializableRedisTemplate.opsForValue().get(key);
    }

    @Override
    public void put(String key, Serializable value) {
        serializableRedisTemplate.opsForValue().set(key, value);
    }

    @Override
    public boolean drop(String key) {
        return Boolean.TRUE.equals(serializableRedisTemplate.delete(key));
    }

    @Override
    public boolean contains(String key) {
        return Boolean.TRUE.equals(serializableRedisTemplate.hasKey(key));
    }

    @Override
    public Set<String> getKeySet() {
        return serializableRedisTemplate.keys("*");
    }
}
