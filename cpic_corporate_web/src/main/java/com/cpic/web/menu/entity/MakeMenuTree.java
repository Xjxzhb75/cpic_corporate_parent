package com.cpic.web.menu.entity;

import com.cpic.web.user.entity.RouterVO;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author xinjianxun
 * @date 2022/6/14 8:15
 * @param: $
 * @return: $
 * @description: TODO
 */

    public class MakeMenuTree {
        public static List<Menu> makeTree(List<Menu> menuList, Long pid){
            List<Menu> list = new ArrayList<>();
            Optional.ofNullable(menuList).orElse(new ArrayList<>())
                    .stream()
                    .filter(item -> item != null && item.getParentId() == pid)
                    .forEach(item ->{
                        Menu menu = new Menu();
                        BeanUtils.copyProperties(item,menu);
                        //获取每一个item的下级
                        List<Menu> children = makeTree(menuList, item.getId());
                        menu.setChildren(children);
                        list.add(menu);
                    });
            return list;
        }
        public static List<RouterVO> makeRouter(List<Menu> menuList, Long pid){
            List<RouterVO> list = new ArrayList<>();
            Optional.ofNullable(menuList).orElse(new ArrayList<>())
                    .stream()
                    .filter(item -> item != null && item.getParentId() == pid)
                    .forEach(item -> {
                        RouterVO router = new RouterVO();
                        router.setName(item.getName());
                        router.setPath(item.getPath());
                        if(item.getParentId() == 0L){
                            router.setComponent("Layout");
                            router.setAlwaysShow(true);
                        }else{
                            router.setComponent(item.getUrl());
                            router.setAlwaysShow(false);
                        }
                        //设置meta
                        router.setMeta(router.new Meta(
                                item.getLabel(),
                                item.getIcon(),
                                item.getCode().split(",")
                        ));
                        //设置children,每一项的下级
                        List<RouterVO> children = makeRouter(menuList, item.getId());
                        router.setChildren(children);
                        list.add(router);
                    });
            return list;
        }
}
