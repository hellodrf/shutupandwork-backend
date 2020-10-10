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
@Primary
@Scope("prototype")
public class ICacheImplRedis<V extends Serializable> implements ICache<Serializable> {

    private final RedisTemplate<String, Serializable> serializableRedisTemplate;

    @Autowired
    public ICacheImplRedis(RedisTemplate<String, Serializable> serializableRedisTemplate) {
        this.serializableRedisTemplate = serializableRedisTemplate;
    }

    private int prefix;

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
        this.prefix = identifier;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public V select(String key) {
        return (V) serializableRedisTemplate.opsForValue().get(addPrefix(key));
    }

    @Override
    public void put(String key, Serializable value) {
        serializableRedisTemplate.opsForValue().set(addPrefix(key), value);
    }

    @Override
    public boolean drop(String key) {
        return Boolean.TRUE.equals(serializableRedisTemplate.delete(addPrefix(key)));
    }

    @Override
    public boolean contains(String key) {
        return Boolean.TRUE.equals(serializableRedisTemplate.hasKey(addPrefix(key)));
    }

    @Override
    public Set<String> getKeySet() {
        return serializableRedisTemplate.keys(prefix +"#.*");
    }

    private String addPrefix(String key) {
        return prefix + "#" + key;
    }

    private String removePrefix(String prefixedKey) {
        String pf = prefixedKey.substring(0, prefix/10+1);
        if (!pf.equals(prefix + "#")) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder(prefixedKey);
        stringBuilder.delete(0, prefix/10+1);
        return stringBuilder.toString();
    }
}
