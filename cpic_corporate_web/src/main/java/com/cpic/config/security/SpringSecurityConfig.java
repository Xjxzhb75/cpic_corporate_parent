package com.cpic.config.security;

import com.cpic.config.security.detailservice.CustomerUserDetailsService;
import com.cpic.config.security.fiter.CheckTokenFilter;
import com.cpic.config.security.handler.CustomAccessDeineHandler;
import com.cpic.config.security.handler.CustomizeAuthenticationEntryPoint;
import com.cpic.config.security.handler.LoginFailureHandler;
import com.cpic.config.security.handler.LoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author xinjianxun
 * @date 2022/6/11 10:46
 * @param: null
 * @return:
 * @description: SpringSecurity配置类访问控制相关配置
 * 配置成功，失败，匿名和无权限访问的控制器
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfig  extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomerUserDetailsService customerUserDetailsService;
    @Autowired
    private LoginSuccessHandler loginSuccessHandler;
    @Autowired
    private LoginFailureHandler loginFailureHandler;
    @Autowired
    private CustomAccessDeineHandler customAccessDeineHandler;
    @Autowired
    private CustomizeAuthenticationEntryPoint customizeAuthenticationEntryPoint;
    @Autowired
    private CheckTokenFilter checkTokenFilter;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().headers().frameOptions().disable();//配置支持跨域
        //添加自定义认证过滤器
        http.addFilterBefore(checkTokenFilter, UsernamePasswordAuthenticationFilter.class);
        //配置登录请求 具体规则用.and()分割
        http.formLogin()
                //请求地址
                //登录成功请求处理器和失败请求处理器
                .loginProcessingUrl("/api/user/login")
                .successHandler(loginSuccessHandler).failureHandler(loginFailureHandler)
                //csrf跨站请求伪造  这里配置是关掉这个配置
                //访问会基于token方式访问，这里就要将记录session关闭，否则服务器会溢出
                //security的session生成策略改为security不主动创建session
                .and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                //authorizeRequests()主要是对url进行访问权限控制，通过这个方法来实现url授权操作。
                //antMatchers()表示匹配所有满足表达式的请求,
                //permitAll()表示所匹配的URL任何人都允许访问
                //anyRequest()表示所有请求,authenticated()表示所匹配的URL都需要被认证才能访问
                //这段代码的意思是登录放行，其它的都需要认证才能访问。
                .and()
                .authorizeRequests()
                .antMatchers("/api/user/login").permitAll()
                .anyRequest().authenticated()
                //异常访问的处理加载匿名访问处理器及无权限访问处理器
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(customizeAuthenticationEntryPoint)
                .accessDeniedHandler(customAccessDeineHandler);

    }
    //加载用户认证处理器对用户进行认证
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customerUserDetailsService);
    }
}
