package com.cervidae.shutupandwork.config;

import com.cervidae.shutupandwork.pojo.User;
import com.cervidae.shutupandwork.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class UserRealm extends AuthorizingRealm {
    private final UserService userService;

    @Autowired
    public UserRealm(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo authorizationInfo=new SimpleAuthorizationInfo();
        authorizationInfo.setRoles(Collections.singleton("user"));
        authorizationInfo.setStringPermissions(Collections.singleton("user"));
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
            throws AuthenticationException {
        String username = (String) authenticationToken.getPrincipal();
        User user = userService.getByUsername(username);
        if (user != null) {
            return new SimpleAuthenticationInfo(user.getUsername(), user.getPassword(),
                    user.getUsername());
        } else {
            return null;
        }
    }
}

