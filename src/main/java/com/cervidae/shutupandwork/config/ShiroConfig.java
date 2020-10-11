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

@Configuration
public class ShiroConfig {

    private final IRealm iRealm;

    @Autowired
    public ShiroConfig(IRealm iRealm) {
        this.iRealm = iRealm;
    }

    @Bean(name = "shiroFilterFactoryBean")
    public ShiroFilterFactoryBean shirFilter(SessionsSecurityManager iSecurityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(iSecurityManager);
        // Filters
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>() {
            {
                put("/users/register", "anon");
                put("/quotes", "anon");
                put("/rankings", "anon");
                put("/users/logout", "anon");
                put("/logout", "logout");

                // all else require authentication
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
    public static LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean(name = "hashedCredentialsMatcher")
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("MD5");
        hashedCredentialsMatcher.setHashIterations(1024);
        return hashedCredentialsMatcher;
    }
}