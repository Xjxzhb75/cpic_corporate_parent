package com.cpic.web.menu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cpic.web.menu.entity.Menu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author xinjianxun
 * @version 1.0
 * @description: TODO
 * @date 2022/6/14 8:24
 */
public interface MenuMapper extends BaseMapper<Menu> {
    //根据用户id查询对应的权限
    List<Menu> getMenuListByUserId(@Param("userId") Long userId);
    //根据角色id查询所有的权限添加权限时候使用RoleMenu的mapper
    List<Menu> getMenuListByRoleId(@Param("roleId") Long roleId);

}
