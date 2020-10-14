package com.cervidae.shutupandwork.dao;

import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Set;

/**
 * @author AaronDu
 */
@Service
public interface ICache<V extends Serializable> {

    void setIdentifier(int identifier);

    /**
     * Get the cached value according to key
     * @param key key of the cache
     * @return value of the cache, or null if key not found
     */
    V select(String key);

    /**
     * Insert into the cache if not already exist.
     * if already exists, update it.
     * @param key key of the cache
     * @param value value of the cache
     */
    void put(String key, V value);

    /**
     * Remove the cache, then return true
     * If key not exist, do nothing and return false
     * @param key key of the cache
     * @return a boolean signifies if the action is successful
     */
    boolean drop(String key);


    /**
     * If the cache contains the key, return true.
     * @param key target key
     * @return whether the key is in cache
     */
    boolean contains(String key);


    /**
     * Get all keys
     * @return set of keys
     */
    Set<String> getKeySet();

    void setExpiry(String key, long expiry);

}
