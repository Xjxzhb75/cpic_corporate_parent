package com.cpic.config.security.execption;

import org.springframework.security.core.AuthenticationException;

/**
 * @author xinjianxun
 * @date 2022/6/13 8:01
 * @param: null$
 * @return: null$
 * @description: token异常的处理，返回异常信息
 */
public class CustomerAuthenionException extends AuthenticationException {
    public CustomerAuthenionException(String msg) {
        super(msg);
    }

}
