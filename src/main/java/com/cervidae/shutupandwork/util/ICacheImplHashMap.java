package com.cervidae.shutupandwork.util;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Primary
public class ICacheImplHashMap<K, V> implements ICache<K, V>{

    private final Map<K, V> cacheMap;

    public ICacheImplHashMap() {
        this.cacheMap = new ConcurrentHashMap<>();
    }

    /**
     * Insert into the cache if not already exist, then return true.
     * if already exists, update it.
     * @param key key of the cache
     * @param value value of the cache
     */
    @Override
    public void insert(K key, V value) {
        cacheMap.put(key, value);
    }

    /**
     * Get the cached value according to key
     * @param key key of the cache
     * @return value of the cache, or null if key not found
     */
    @Override
    public V select(K key) {
        return cacheMap.get(key);
    }

    /**
     * a boolean signifies if the action is successful
     * If key not exist, do nothing and return false
     * @param key key of the cache
     * @param value value of the cache
     * @return a boolean signifies if the action is successful
     */
    @Override
    public boolean update(K key, V value) {
        if (cacheMap.containsKey(key)) {
            cacheMap.put(key, value);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Remove the cache, then return true
     * If key not exist, do nothing and return false
     * @param key key of the cache
     * @return a boolean signifies if the action is successful
     */
    @Override
    public boolean remove(K key) {
        return cacheMap.remove(key) != null;
    }
}
