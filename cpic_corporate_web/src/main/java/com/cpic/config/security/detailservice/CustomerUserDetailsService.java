package com.cpic.config.security.detailservice;

import com.cpic.web.menu.entity.Menu;
import com.cpic.web.menu.service.MenuService;
import com.cpic.web.user.entity.User;
import com.cpic.web.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xinjianxun
 * @version 1.0
 * @date 2022/6/11 1:14
 * @description:
 * 实现SpringSecurityConfig的 UserDetailsService
 * 1、引入相关依赖  2、创建实体类  3、实体类添加@Data
 * 4创建serveice，实现类中调Mapper层
 * 5、创建并调用LoadUserByUsername方法 查询用户，如果等于空表示没查到用户那么，直接抛异常，
 * 6、然后编写处理成功\失败\匿名\无权限访问处理器 handler/LoginSuccessHandler
 */
@Component("CustomerUserDetailsService")
public class CustomerUserDetailsService implements UserDetailsService {
    @Autowired
    private UserService userService;
    @Autowired
    private MenuService menuService;
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        //认证功能，看用户是否存在，不存在通过抛异常返回数据给前端
        User user = userService.getUserByUsername(s);
        if(user == null){
            throw  new UsernameNotFoundException("用户名或密码错误，请核对用户名和密码，！");
        }
        //授权
        List<Menu> menuList = menuService.getMenuListByUserId(user.getId());
        //去掉为null
        List<String> collect = menuList.stream().filter(item -> item != null).map(item -> item.getCode()).filter(item -> item != null).collect(Collectors.toList());
        String[] strings = collect.toArray(new String[collect.size()]);
        List<GrantedAuthority> authorityList = AuthorityUtils.createAuthorityList(strings);
        user.setAuthorities(authorityList);
        user.setPermissionList(menuList);
        return user;
    }
}
