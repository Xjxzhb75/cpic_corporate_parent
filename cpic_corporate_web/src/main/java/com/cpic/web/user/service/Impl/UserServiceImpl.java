package com.cpic.web.user.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cpic.web.user.entity.User;
import com.cpic.web.user.mapper.UserMapper;
import com.cpic.web.user.service.UserService;
import com.cpic.web.user_role.entity.UserRole;
import com.cpic.web.user_role.mapper.UserRoleMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author xinjianxun
 * @version 1.0
 * @description: TODO
 * @date 2022/6/10 22:22
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    private UserRoleMapper userRoleMapper;
    @Override
    public  User getUserByUsername (String username){
        QueryWrapper<User> query=new QueryWrapper<>();
        query.lambda().eq(User::getUsername,username);
        return this.baseMapper.selectOne(query);
    }

    @Override
    public IPage<User> getUserList(Long deptId, Long currentPage, Long pageSize) {
        //构造查询条件
        QueryWrapper<User> query = new QueryWrapper<>();
        query.lambda().eq(User::getDeptId,deptId);
        //分页对象
        IPage<User> page = new Page<>();
        page.setSize(pageSize);
        page.setCurrent(currentPage);
        return this.baseMapper.selectPage(page,query);
    }

    @Override
    public UserRole getRolebyUserId(Long userId) {
        QueryWrapper<UserRole> query = new QueryWrapper<>();
        query.lambda().eq(UserRole::getUserId,userId);
        return userRoleMapper.selectOne(query);
    }

    @Override
    @Transactional
    public void saveRole(UserRole userRole) {
        //需要先把原来的角色删除
        QueryWrapper<UserRole> query = new QueryWrapper<>();
        query.lambda().eq(UserRole::getUserId,userRole.getUserId());
        userRoleMapper.delete(query);
        //存新的
        userRoleMapper.insert(userRole);
    }
}
