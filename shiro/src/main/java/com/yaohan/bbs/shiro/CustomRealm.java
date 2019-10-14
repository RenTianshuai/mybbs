package com.yaohan.bbs.shiro;

import com.yaohan.bbs.dao.entity.User;
import com.yaohan.bbs.service.RoleService;
import com.yaohan.bbs.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

@Slf4j
public class CustomRealm extends AuthorizingRealm {

    @Autowired
    UserService userService;
    @Autowired
    RoleService roleService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        log.info("授权");
        String username = (String) principalCollection.getPrimaryPrincipal();
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        // 根据用户名查询当前用户拥有的角色
        User user = userService.getByUserName(username);
        Set<String> roleNames = new HashSet<>();
        roleNames.add(user.getRoleId());
        // 将角色名称提供给info
        authorizationInfo.setRoles(roleNames);
        // 根据用户名查询当前用户权限
//        Set<Permission> permissions = userService.findPermissions(username);
        Set<String> permissionNames = new HashSet<>();
//        for (Permission permission : permissions) {
//            permissionNames.add(permission.getPermission());
//        }
        // 将权限名称提供给info
        authorizationInfo.setStringPermissions(permissionNames);

        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        log.info("认证");
        CustomPasswordToken token = (CustomPasswordToken) authenticationToken;

        //验证码
        Session session = SecurityUtils.getSubject().getSession();
        String code = (String)session.getAttribute("validateCode");
        if (token.getVercode() == null || !token.getVercode().toUpperCase().equals(code)){
            throw new AuthenticationException("验证码错误,请重试");
        }

        String username = (String) token.getPrincipal();
        User user = null;
        if (StringUtils.isNotEmpty(username)){
            user = userService.getByUserName(username);
        }else {
            String email = token.getEmail();
            user = userService.getByEmail(email);
        }
        if (user == null) {
            throw new UnknownAccountException("用户不存在");
        }
        if ("1".equals(user.getDelFlag())) {
            throw new LockedAccountException("该账户已删除");
        }
        //设置角色名
        if (StringUtils.isNotEmpty(user.getRoleId())){
            user.setRoleName(roleService.get(user.getRoleId()).getName());
        }
        session.setAttribute("user", user);
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user.getUsername(),
                user.getPassword(), ByteSource.Util.bytes(user.getSalt()), getName());
        return authenticationInfo;
    }
}
