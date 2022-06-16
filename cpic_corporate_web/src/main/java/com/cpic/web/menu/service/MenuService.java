package com.cpic.web.menu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cpic.web.menu.entity.Menu;

import java.util.List;

/**
 * @author xinjianxun
 * @version 1.0
 * @description: TODO
 * @date 2022/6/14 18:07
 */
public interface MenuService extends IService<Menu> {
    List<Menu> getMenuListByUserId(Long userId);
    //获取菜单列表
    List<Menu> getListMenu();
    //获取上级菜单
    List<Menu> getParentList();
    //根据角色id查询所有的权限
    List<Menu> getMenuListByRoleId(Long roleId);
}
