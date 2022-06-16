package com.cpic.web.menu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cpic.web.menu.entity.MakeMenuTree;
import com.cpic.web.menu.entity.Menu;
import com.cpic.web.menu.mapper.MenuMapper;
import com.cpic.web.menu.service.MenuService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @author xinjianxun
 * @date 2022/6/14 18:08
 * @param: $
 * @return: $
 * @description: TODO
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {


    @Override
    public List<Menu> getMenuListByUserId(Long userId) {
        return this.baseMapper.getMenuListByUserId(userId);
    }

    @Override
    public List<Menu> getListMenu() {
        QueryWrapper<Menu> query = new QueryWrapper<>();
        query.lambda().orderByAsc(Menu::getOrderNum);
        List<Menu> menus = this.baseMapper.selectList(query);
        //组装树格式数据
        List<Menu> menuList = MakeMenuTree.makeTree(menus, 0L);
        return menuList;
    }

    @Override
    public List<Menu> getParentList() {
        //只需要查询类型为 目录和菜单的就可以
        String[] type = {"0","1"};
        List<String> strings = Arrays.asList(type);
        //构造查询条件
        QueryWrapper<Menu> query = new QueryWrapper<>();
        query.lambda().in(Menu::getType, strings).orderByAsc(Menu::getOrderNum);
        List<Menu> menus = this.baseMapper.selectList(query);
        //构造顶级菜单
        Menu menu = new Menu();
        menu.setId(0L);
        menu.setParentId(-1L);
        menu.setLabel("顶级菜单");
        menus.add(menu);
        //构造树格式数据
        List<Menu> menuList = MakeMenuTree.makeTree(menus, -1L);
        return menuList;
    }

    @Override
    public List<Menu> getMenuListByRoleId(Long roleId) {
            return this.baseMapper.getMenuListByRoleId(roleId);
        }
    }

