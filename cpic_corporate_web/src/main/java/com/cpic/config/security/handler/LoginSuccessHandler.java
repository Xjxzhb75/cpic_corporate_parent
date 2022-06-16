package com.cpic.config.security.handler;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.cpic.config.jwt.JwtUtils;
import com.cpic.redis.RedisService;
import com.cpic.status.StatusCode;
import com.cpic.web.user.entity.User;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author xinjianxun
 * @version 1.0
 * @date 2022/6/11 9:57
 * @param: null
 * @return:
 * @description:
 * 前接CustomerUserDetailsService
 * 7、认证成功处理器 向前端返回Json格式数据
  *
  */
@Component("loginSuccessHandler")
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private RedisService redisService;

    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        //用户信息
        User user = (User) authentication.getPrincipal();
        //生成token
        String token = jwtUtils.generateToken(user);
        //生成token的过期时间
        Long expireTime = Jwts.parser()
                .setSigningKey(jwtUtils.getSecret())
                .parseClaimsJws(token)
                .getBody().getExpiration().getTime();
        //组装返回的数据
        LoginResult result= new LoginResult();
        result.setId(user.getId());
        result.setLoginName(user.getLoginName());
        result.setCode(StatusCode.SUCCESS_CODE);
        result.setExpireTime(expireTime);
        result.setToken(token);

        //返回json
        //SerializerFeature.DisableCircularReferenceDetect 消除循环引用
        String res = JSONObject.toJSONString(result, SerializerFeature.DisableCircularReferenceDetect);
        //设置返回格式
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        ServletOutputStream out = httpServletResponse.getOutputStream();
        out.write(res.getBytes("UTF-8"));
        out.flush();
        out.close();
        //将token存入到redis中
        String tokenKey = "token_"+token;
        redisService.set(tokenKey,token,jwtUtils.getExpiration() / 1000);
    }
}
