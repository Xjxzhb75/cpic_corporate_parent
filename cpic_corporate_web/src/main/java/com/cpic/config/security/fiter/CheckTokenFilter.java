package com.cpic.config.security.fiter;

import com.cpic.config.jwt.JwtUtils;
import com.cpic.redis.RedisService;
import com.cpic.config.security.detailservice.CustomerUserDetailsService;
import com.cpic.config.security.execption.CustomerAuthenionException;
import com.cpic.config.security.handler.LoginFailureHandler;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author xinjianxun
 * @date 2022/6/13 7:59
 * @param: null$
 * @return: null$
 * @description: token验证的过滤器
 */
@Data
@Component("checkTokenFilter")
public class CheckTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private CustomerUserDetailsService customerUserDetailsService;
    @Autowired
    private LoginFailureHandler loginFailureHandler;
    @Autowired
    private RedisService redisService;
    @Value("${cpic.loginUrl}")
    private String loginUrl;
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        try{
            //获取请求url
            String url = httpServletRequest.getRequestURI();
            //token 验证，非登录请求才做token验证
            if(!url.equals(loginUrl)){
                //验证码不需要token验证
                validateToken(httpServletRequest);
            }
        }catch(AuthenticationException e){
            loginFailureHandler.onAuthenticationFailure(httpServletRequest,httpServletResponse,e);
            return;
        }
        //继续执行其它
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }
    private  void validateToken(HttpServletRequest request){
        //从头部获取token
        String token= request.getHeader("token");
        //StringUtils.isEmpty(token)是判断某字符串是否为空这里是从头部取的token这个字符串
        //如果StringUtils.isEmpty(token)=true 说明头部不含token,则要继续在参数中去获取token
        if (StringUtils.isEmpty(token)){
            token = request.getParameter("token");
        }
        //头部没有token，参数中如果也没有token，则向前端返回token异常
        if(StringUtils.isEmpty(token)){
            throw new CustomerAuthenionException("您没有携带访问服务器,token异常了");
        }
        String tokenkey = "token_"+token;
        String redisToken = redisService.get(tokenkey);
        //如果redis里面没有token,说明该token失效
        if(StringUtils.isEmpty(redisToken)){
            throw new CustomerAuthenionException("token过期！");
        }
        if(!token.equals(redisToken)){
            throw new CustomerAuthenionException("token验证失败！");
        }
        //解析token
        String username = jwtUtils.getUsernameFromToken(token);
        if(StringUtils.isEmpty(username)){
            throw new CustomerAuthenionException("token解析失败！");
        }
        //获取用户对象
        UserDetails details = customerUserDetailsService.loadUserByUsername(username);
        if(details == null){
            throw new CustomerAuthenionException("token解析失败！");
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(details,null,details.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        //设置到spring security上下文
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}
