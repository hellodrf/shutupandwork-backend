package com.cervidae.shutupandwork.util;

import org.springframework.stereotype.Service;

@Service
public interface ICache<K, V> {

    /**
     * Insert into the cache if not already exist, then return true.
     * if already exists, update it.
     * @param key key of the cache
     * @param value value of the cache
     */
    void insert(K key, V value);

    /**
     * Get the cached value according to key
     * @param key key of the cache
     * @return value of the cache, or null if key not found
     */
    V select(K key);

    /**
     * a boolean signifies if the action is successful
     * If key not exist, do nothing and return false
     * @param key key of the cache
     * @param value value of the cache
     * @return a boolean signifies if the action is successful
     */
    boolean update(K key, V value);

    /**
     * Remove the cache, then return true
     * If key not exist, do nothing and return false
     * @param key key of the cache
     * @return a boolean signifies if the action is successful
     */
    boolean remove(K key);

}
