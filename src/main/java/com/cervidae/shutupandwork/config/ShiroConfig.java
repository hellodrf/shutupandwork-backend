package com.cervidae.shutupandwork.config;

import org.apache.shiro.mgt.SessionsSecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class ShiroConfig {

    private final UserRealm userRealm;

    @Autowired
    public ShiroConfig(UserRealm userRealm) {
        this.userRealm = userRealm;
    }

    @Bean(name = "shiroFilterFactoryBean")
    public ShiroFilterFactoryBean shirFilter(SessionsSecurityManager iSecurityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(iSecurityManager);
        // Filters
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>() {
            {
                put("/users/logout", "anon");
                put("/afterlogout", "anon");
                put("/afterlogin", "anon");
                put("/**", "authc");
            }
        };
        shiroFilterFactoryBean.setLoginUrl("/users/login");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    @Bean
    public SessionsSecurityManager iSecurityManager() {
        DefaultWebSecurityManager iSecurityManager = new DefaultWebSecurityManager();
        iSecurityManager.setRealm(userRealm);
        return iSecurityManager;
    }

    @Bean(name="lifecycleBeanPostProcessor")
    public static LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }
}