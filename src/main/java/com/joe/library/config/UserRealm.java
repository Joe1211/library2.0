package com.joe.library.config;

import com.joe.library.mapper.PermissionMapper;
import com.joe.library.mapper.RoleMapper;
import com.joe.library.pojo.Permission;
import com.joe.library.pojo.Role;
import com.joe.library.pojo.User;
import com.joe.library.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @authr 乔
 * @Date 2019/9/7
 */
public class UserRealm extends AuthorizingRealm {

    @Autowired
    UserService userService;

    @Autowired
    RoleMapper roleMapper;

    @Autowired
    PermissionMapper permissionMapper;

    /**
     * 执行授权逻辑
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //给资源授权
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        //获取前端输入的用户名
        String username = (String)principals.getPrimaryPrincipal();
        //根据用户名查询数据库中对应的记录
        User user = userService.selectByUsername(username);
        if(user != null){
            List<Role> roles = roleMapper.selectByUserId(user.getId());
            for (Role role : roles ) {
                info.addRole(role.getRoleName());
                List<Permission> permissions = permissionMapper.selectByRoleId(role.getId());
                for (Permission permission : permissions) {
                    info.addStringPermission(permission.getPermissionName());
                }
            }
        }
        return info;
    }

    /**
     * 提供账户信息，返回认证信息
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        UsernamePasswordToken token1 = (UsernamePasswordToken)token;

        //获取数据库用户
        User user = userService.selectByUsername(token1.getUsername());

        if (user == null){
            //用户不存在
            return null;
        }
        //判断密码
        ByteSource Salt = ByteSource.Util.bytes(user.getUserName());
        SimpleAuthenticationInfo info = null;
        info = new SimpleAuthenticationInfo(user.getUserName(),user.getPassword(),Salt,getName());

        return info;
    }
}
