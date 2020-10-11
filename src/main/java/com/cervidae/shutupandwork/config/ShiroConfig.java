package com.cervidae.shutupandwork.config;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SessionsSecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
                put("/session/gc", "roles[admin]");

                // all else requires authentication
                put("/**", "authc");
            }
        };
        shiroFilterFactoryBean.setLoginUrl("/users/login");
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
}