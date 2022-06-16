package com.cpic.web.user.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author xinjianxun
 * @date 2022/6/15 2:17
 * @param: $
 * @return: $
 * @description: TODO
 */

    @Data
    public class UserInfo  implements Serializable {
        private long id;
        //用户名
        private String name;
        //头像
        private String avatar;
        //介绍
        private String introduction;
        //权限集合
        private Object[] roles;
    }

