package com.cpic.web.user.entity;

import lombok.Data;

/**
 * @author xinjianxun
 * @date 2022/6/15 2:19
 * @param: $
 * @return: $
 * @description: TODO
 */
@Data
public class TokenVo {
    private  Long expireTime;
    private String token;
}
