package com.cpic.web.user.entity;

import com.cpic.web.menu.entity.Menu;
import lombok.Data;

import java.util.List;

/**
 * @author xinjianxun
 * @date 2022/6/15 2:14
 * @param: $
 * @return: $
 * @description: 用户权限等，SysUserController使用
 */
@Data
public class UserMenuVo {
    //菜单数据
    private List<Menu> menuList;
    //权限字段
    private List<String> authList;
    //路由数据
    private List<Menu> routerList;
}
