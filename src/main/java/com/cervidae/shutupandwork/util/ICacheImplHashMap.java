package com.cervidae.shutupandwork.util;

import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Scope("prototype")
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
     * @return a boolean signifies if the action is successful
     */
    @Override
    public boolean insert(K key, V value) {
        if (cacheMap.containsKey(key)) {
            return false;
        } else {
            cacheMap.put(key, value);
            return true;
        }
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
     * Update the value if key already exists, then return true.
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
    public boolean drop(K key) {
        return cacheMap.remove(key) != null;
    }

    /**
     * Insert into the cache if not already exist.
     * if already exists, update it.
     * @param key key of the cache
     * @param value value of the cache
     */
    @Override
    public void put(K key, V value) {
        cacheMap.put(key, value);
    }

    /**
     * If the cache contains the key, return true.
     * @param key target key
     * @return whether the key is in cache
     */
    @Override
    public boolean contains(K key) {
        return cacheMap.containsKey(key);
    }
}
