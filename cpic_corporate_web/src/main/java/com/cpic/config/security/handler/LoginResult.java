package com.cpic.config.security.handler;

import lombok.Data;

/**
 * @author xinjianxun
 * @date 2022/6/12 0:59
 * @param: null
 * @return:
 * @description: 封装用户认证成功返回前端json数据
 */
@Data
public class LoginResult {
    private int code;
    private String token;
    //token过期时间
    private Long expireTime;
    private Long Id;
    private String LoginName;
}
