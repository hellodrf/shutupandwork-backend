package com.cervidae.shutupandwork.config;

import com.cervidae.shutupandwork.pojo.User;
import com.cervidae.shutupandwork.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author AaronDu
 */
@Component
public class IRealm extends AuthorizingRealm {

    private final UserService userService;

    @Autowired
    public IRealm(UserService userService) {
        this.userService = userService;
    }

    /**
     * Set roles and permissions for user
     * @param principalCollection user principles
     * @return a AuthorizationInfo
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        String username = (String) principalCollection.getPrimaryPrincipal();
        User user = userService.getByUsername(username);
        if (user != null) {
            String role = user.getRole();
            if (!role.equals("user")) {
                Set<String> setRoles = new HashSet<>();
                setRoles.add("user");
                setRoles.add(role);
                authorizationInfo.setRoles(setRoles);
                authorizationInfo.setStringPermissions(setRoles);
            } else {
                authorizationInfo.setRoles(Collections.singleton("user"));
                authorizationInfo.setStringPermissions(Collections.singleton("user"));
            }
        }
        return authorizationInfo;
    }

    /**
     * Do authentication, return a SimpleAuthenticationInfo
     * @param authenticationToken a UsernamePasswordToken
     * @return a SimpleAuthenticationInfo
     * @throws AuthenticationException thrown when failed to auth
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
            throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String username = token.getUsername();
        User user = userService.getByUsername(username);
        if (user != null) {
            // add salt
            ByteSource salt = ByteSource.Util.bytes(username);
            String realmName = this.getName();
            return new SimpleAuthenticationInfo(username, user.getPassword(),
                    salt, realmName);
        }
        return null;
    }
}

