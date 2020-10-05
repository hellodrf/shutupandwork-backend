package com.cervidae.shutupandwork.util;

import org.springframework.stereotype.Service;

@Service
public interface ICache<K, V> {

    /**
     * Insert into the cache if not already exist, then return true.
     * if already exists, update it.
     * @param key key of the cache
     * @param value value of the cache
     * @return a boolean signifies if the action is successful
     */
    boolean insert(K key, V value);

    /**
     * Get the cached value according to key
     * @param key key of the cache
     * @return value of the cache, or null if key not found
     */
    V select(K key);

    /**
     * Update the value if key already exists, then return true.
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
    boolean drop(K key);

    /**
     * Insert into the cache if not already exist.
     * if already exists, update it.
     * @param key key of the cache
     * @param value value of the cache
     */
    void insertOrUpdate(K key, V value);

    /**
     * If the cache contains the key, return true.
     * @param key target key
     * @return whether the key is in cache
     */
    boolean contains(K key);

}
