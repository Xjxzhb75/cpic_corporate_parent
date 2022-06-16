package com.cpic.config.security.handler;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.cpic.config.security.execption.CustomerAuthenionException;
import com.cpic.utils.ResultVo;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author xinjianxun
 * @date 2022/6/11 10:35
 * @param: null
 * @return:
 * @description: 认证失败处理器
 *    8设置一些认证失败的返回信息
 */
@Component("LoginFailureHandler")
public class LoginFailureHandler  implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        int code = 500;
        String str = "";
        if (e instanceof AccountExpiredException) {
            str = "账户过期，登录失败!";
        } else if (e instanceof BadCredentialsException) {
            str = "用户名或密码错误，登录失败!";
        } else if (e instanceof CredentialsExpiredException) {
            str = "密码过期，登录失败!";
        } else if (e instanceof DisabledException) {
            str = "账户被禁用，登录失败!";
        } else if (e instanceof LockedException) {
            str = "账户被锁，登录失败!";
        } else if (e instanceof InternalAuthenticationServiceException) {
            str = "账户不存在，登录失败!";
  }
        else if (e instanceof CustomerAuthenionException) {
          code = 600;
          str = e.getMessage();
          }
        else {
            str = "登录失败!";
        }
        String res = JSONObject.toJSONString(new ResultVo(str, code, null), SerializerFeature.DisableCircularReferenceDetect);
        //设置返回格式
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        ServletOutputStream out = httpServletResponse.getOutputStream();
        out.write(res.getBytes("UTF-8"));
        out.flush();
        out.close();
    }
}
