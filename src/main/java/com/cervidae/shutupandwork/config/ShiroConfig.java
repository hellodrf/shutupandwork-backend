package com.cervidae.shutupandwork.config;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SessionsSecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.springframework.context.annotation.Configuration;

/**
 * @author AaronDu
 */
@Configuration
public class ShiroConfig {

    private final IRealm iRealm;

    @Value("${spring.redis.port}")
    private String redisPort;

    @Value("${spring.redis.host}")
    private String redisHost;

    @Autowired
    public ShiroConfig(IRealm iRealm) {
        this.iRealm = iRealm;
    }

    @Bean(name = "shiroFilterFactoryBean")
    public ShiroFilterFactoryBean iShiroFilter(SessionsSecurityManager iSecurityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(iSecurityManager);
        // Filters
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>() {
            {
                put("/users/register", "anon");
                put("/users/logout", "anon");
                put("/logout", "logout");
                put("/quotes/admin", "roles[admin]");
                put("/quotes", "anon");
                put("/rankings", "anon");
                put("/sessions/gc", "roles[admin]");

                // all else requires authentication
                put("/**", "authc");
            }
        };
        shiroFilterFactoryBean.setLoginUrl("/users/login");
        shiroFilterFactoryBean.setUnauthorizedUrl("/users/403");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    @Bean
    public SessionsSecurityManager iSecurityManager(@Qualifier("hashedCredentialsMatcher")
                                                                HashedCredentialsMatcher matcher) {
        DefaultWebSecurityManager iSecurityManager = new DefaultWebSecurityManager();
        iRealm.setCredentialsMatcher(matcher);
        iSecurityManager.setRealm(iRealm);
        return iSecurityManager;
    }

    @Bean(name="lifecycleBeanPostProcessor")
    public static LifecycleBeanPostProcessor iLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean(name = "hashedCredentialsMatcher")
    public HashedCredentialsMatcher iHashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("MD5");
        hashedCredentialsMatcher.setHashIterations(1024);
        return hashedCredentialsMatcher;
    }

    /**
     * Session Manager (shiro-redis)
     */
    @Bean
    public SessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionDAO(redisSessionDAO());
        return sessionManager;
    }

    /**
     * Cache Manager (shiro-redis)
     */
    @Bean
    public RedisCacheManager redisCacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        redisCacheManager.setExpire(200000);
        redisCacheManager.setPrincipalIdFieldName("id");
        return redisCacheManager;
    }

    /**
     * RedisManager (shiro-redis)
     */
    @Bean
    public RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(redisHost + ":" + redisPort);
        return redisManager;
    }

    /**
     * RedisSessionDAO (shiro-redis)
     */
    @Bean
    public RedisSessionDAO redisSessionDAO() {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager());
        redisSessionDAO.setExpire(2000);
        return redisSessionDAO;
    }
}