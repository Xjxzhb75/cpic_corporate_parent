package com.cpic.web.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cpic.web.user.entity.User;
import com.cpic.web.user_role.entity.UserRole;

/**
 * @author xinjianxun
 * @version 1.0
 * @description: TODO
 * @date 2022/6/10 8:58
 */
public interface UserService  extends IService<User> {
        //sprint security 认证时候使用 获取一个用户
        User getUserByUsername(String username);
        //获取用户列表
        IPage<User> getUserList(Long deptId, Long currentPage, Long pageSize);
        //根据用户id查角色id
        UserRole getRolebyUserId(Long userId);
        //分配角色保存
        void saveRole(UserRole userRole);
}
