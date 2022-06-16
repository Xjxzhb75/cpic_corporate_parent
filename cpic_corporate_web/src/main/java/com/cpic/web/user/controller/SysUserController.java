package com.cpic.web.user.controller;

import com.cpic.config.jwt.JwtUtils;
import com.cpic.redis.RedisService;
import com.cpic.utils.ResultUtils;
import com.cpic.utils.ResultVo;
import com.cpic.web.menu.entity.MakeMenuTree;
import com.cpic.web.menu.entity.Menu;
import com.cpic.web.user.entity.*;
import io.jsonwebtoken.Jwts;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xinjianxun
 * @date 2022/6/15 2:09
 * @param: $
 * @return: $
 * @description: TODO
 */
@RestController
@RequestMapping("/api/sysUser")
public class SysUserController {
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private RedisService redisService;
    @PostMapping("/loginOut")
    public ResultVo loginOut(HttpServletRequest request, HttpServletResponse response){
        //获取用户相关信息
        String token = request.getParameter("token");
        if(StringUtils.isEmpty(token)){
            token = request.getHeader("token");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null){
            //清空用户信息
            new SecurityContextLogoutHandler().logout(request,response,authentication);
            //清空redis里面的token
            String key = "token_"+token;
            redisService.del(key);
        }
        return ResultUtils.success("退出登录成功!");
    }
    @GetMapping("/getPermissionList")
    public ResultVo getPermissionList(){
        //用户相关的信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //获取用户信息
        User user = (User) authentication.getPrincipal();
        //获取用户所有的权限信息
        List<Menu> permissionList = user.getPermissionList();
        UserMenuVo vo  = new UserMenuVo();
        //获取所有的权限字段
        List<String> codeList = permissionList.stream().filter(item -> item != null).map(item -> item.getCode()).collect(Collectors.toList());
        vo.setAuthList(codeList);
        //获取菜单数据
        List<Menu> menuList = permissionList.stream().filter(item -> item != null && !item.getType().equals("2")).collect(Collectors.toList());
        //转换成树的格式
        List<Menu> menus = MakeMenuTree.makeTree(menuList, 0L);
        vo.setMenuList(menus);
        //获路由数据
        List<Menu> routerList = permissionList.stream().filter(item -> item != null && item.getType().equals("1")).collect(Collectors.toList());
        vo.setRouterList(routerList);
        return ResultUtils.success("成功",vo);
    }

    @GetMapping("/getInfo")
    public ResultVo getInfo(){
        //获取用户相关的信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //获取用户信息
        User user = (User) authentication.getPrincipal();
        UserInfo userInfo = new UserInfo();
        userInfo.setId(user.getId());
        userInfo.setName(user.getLoginName());
        //获取权限字段
        List<Menu> permissionList = user.getPermissionList();
        Object[] toArray = permissionList.stream().filter(item -> item != null).map(item -> item.getCode()).toArray();
        userInfo.setRoles(toArray);
        return ResultUtils.success("获取成功",userInfo);
    }


    @GetMapping("/getMenuList")
    public ResultVo getMenuList(){
        //获取用户相关的信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //获取用户信息
        User user = (User) authentication.getPrincipal();
        //获取权限
        List<Menu> permissionList = user.getPermissionList();
        //只需要获取目录和菜单
        List<Menu> collect = permissionList.stream().filter(item -> item != null && !item.getType().equals("2")).collect(Collectors.toList());
        List<RouterVO> routerVOList = MakeMenuTree.makeRouter(collect, 0L);
        return ResultUtils.success("获取成功",routerVOList);
    }


    @PostMapping("/refreshToken")

    public ResultVo refreshToken(HttpServletRequest request) {
        //获取token
        String token = request.getParameter("token");
        if(StringUtils.isEmpty(token)){
            token = request.getHeader("token");
        }
        //获取用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetail = (UserDetails) authentication.getPrincipal();
        //验证token
        Boolean aBoolean = jwtUtils.validateToken(token, userDetail);
        if(!aBoolean){
            return ResultUtils.error("token验证失败");
        }
        //获取刷新token
        String refreshToken = jwtUtils.refreshToken(token);
        //获取新的token过期时间
        Long expireTime =  Jwts.parser()
                .setSigningKey(jwtUtils.getSecret())
                .parseClaimsJws(refreshToken)
                .getBody().getExpiration().getTime();
        TokenVo vo  = new TokenVo();
        vo.setExpireTime(expireTime);
        vo.setToken(refreshToken);
        //把原来的token删除
        String delKey = "token_"+token;
        redisService.del(delKey);
        //把刷新的token存到redis
        String newKey = "token_"+refreshToken;
        redisService.set(newKey,refreshToken,jwtUtils.getExpiration() / 1000);
        return ResultUtils.success("成功",vo);
    }
}
