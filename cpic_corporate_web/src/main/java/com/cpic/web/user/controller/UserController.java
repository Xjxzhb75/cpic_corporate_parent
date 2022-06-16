package com.cpic.web.user.controller;

import com.cpic.redis.RedisService;
import com.cpic.utils.ResultVo;
import com.cpic.web.user.entity.User;
import com.cpic.web.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author xinjianxun
 * @version 1.0
 * @description: TODO
 * @date 2022/6/10 8:36
 */
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/getAllUser")
    public ResultVo  getAllguser(){
        List<User> list = userService.list();

        return new ResultVo("成功",200,list);
    }
}
