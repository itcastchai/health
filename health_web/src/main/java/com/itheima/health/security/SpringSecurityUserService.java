package com.itheima.health.security;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.pojo.Permission;
import com.itheima.health.pojo.Role;
import com.itheima.health.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class SpringSecurityUserService implements UserDetailsService {
    @Reference
    private UserService userService;

    @Override
    //根据用户名查询用户信息
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       com.itheima.health.pojo.User user=userService.findByUsername(username);
          //远程调用用户服务，根据用户名查询用户信息
        if (user==null) {
            return null;
        }

        List<GrantedAuthority> list=new ArrayList<>();
        //根据用户获取角色信息
        Set<Role> roles = user.getRoles();
        if (roles!=null&&roles.size()>0) {
            for (Role role : roles) {
                //根据角色获取权限信息
                Set<Permission> permissions = role.getPermissions();
                if (permissions!=null&&permissions.size()>0) {
                    for (Permission permission : permissions) {
                        String keyword = permission.getKeyword();//获取权限
                        //添加权限
                        list.add(new SimpleGrantedAuthority(keyword));
                    }
                }
            }
        }
        /**
         * User()
         * 1：指定用户名
         * 2：指定密码（SpringSecurity会自动对密码进行校验）
         * 3：传递授予的角色和权限
         */

        return new org.springframework.security.core.userdetails.User(username,user.getPassword(),list);
    }
}
