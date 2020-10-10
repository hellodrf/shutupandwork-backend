package com.cervidae.shutupandwork.util;

import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author AaronDu
 */
@Service
@Scope("prototype")
public class ICacheImplHashMap<V extends Serializable> implements ICache<V>{

    private final Map<String, V> cacheMap;

    public ICacheImplHashMap() {
        this.cacheMap = new ConcurrentHashMap<>();
    }

    /**
     * Do nothing, hashmap don't require this since we are in prototype scope.
     * @param identifier data prefix
     */
    @Override
    public void setIdentifier(int identifier) {}

    /**
     * Get the cached value according to key
     * @param key key of the cache
     * @return value of the cache, or null if key not found
     */
    @Override
    public V select(String key) {
        return cacheMap.get(key);
    }

    /**
     * Insert into the cache if not already exist.
     * if already exists, update it.
     * @param key key of the cache
     * @param value value of the cache
     */
    @Override
    public void put(String key, V value) {
        cacheMap.put(key, value);
    }

    /**
     * Remove the cache, then return true
     * If key not exist, do nothing and return false
     * @param key key of the cache
     * @return a boolean signifies if the action is successful
     */
    @Override
    public boolean drop(String key) {
        return cacheMap.remove(key) != null;
    }

    /**
     * If the cache contains the key, return true.
     * @param key target key
     * @return whether the key is in cache
     */
    @Override
    public boolean contains(String key) {
        return cacheMap.containsKey(key);
    }

    @Override
    public Set<String> getKeySet() {
        return cacheMap.keySet();
    }
}
