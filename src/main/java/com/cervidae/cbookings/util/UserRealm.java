package com.cervidae.cbookings.util;

import com.cervidae.cbookings.pojo.User;
import com.cervidae.cbookings.service.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //获取用户名
        String username = principalCollection.getPrimaryPrincipal().toString();
        SimpleAuthorizationInfo authorizationInfo=new SimpleAuthorizationInfo();
        //获得授权角色
        authorizationInfo.setRoles(userService.getRoles(username));
        //获得授权权限
        authorizationInfo.setStringPermissions(userService.getPermissions(username));
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
            throws AuthenticationException {
        //获取用户名密码
        String username = authenticationToken.getPrincipal().toString();
        User tbUser = userService.getByUsername(username);
        if (tbUser != null) {
            return new SimpleAuthenticationInfo(tbUser.getUsername(),tbUser.getPassword(),
                    tbUser.getUsername());
        } else {
            return null ;
        }
    }
}
