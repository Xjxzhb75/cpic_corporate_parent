package com.cpic.web.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cpic.web.menu.entity.Menu;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.io.Serializable;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @author xinjianxun
 * @version 1.0
 * @description: TODO
 * @date 2022/6/10 8:37
 */
@Data
@TableName(value = "fkxx_user")
public class User implements Serializable, UserDetails {
    //主键自动增长
    @TableId(type = IdType.AUTO)
    private long id;
    //登录名
    private String username;
    //用户名
    private String loginName;

    private String password;
    //帐户是否过期(1 未过期，0已过期)
    private boolean isAccountNonExpired = true;
    //帐户是否被锁定(1 未锁定，0已锁定)
    private boolean isAccountNonLocked = true;
    //密码是否过期(1 未过期，0已过期)
    private boolean isCredentialsNonExpired = true;
    //帐户是否可用(1 可用，0 删除用户)
    private boolean isEnabled = true;
    //由于authorities不是数据库里面的字段，所以要排除他，不然mybatis-plus找不到该字段会报错
    @TableField(exist = false)
    Collection<? extends GrantedAuthority> authorities;
    //昵称
    private String nickName;
    //手机号
    private String mobile;
    //邮箱
    private String email;
    //部门id
    private Long deptId;
    //部门名称
    private String deptName;
    //创建时间

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;
    //更新时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;
    //是否是管理员 1：是 0 ：不是
    private String isAdmin;
    //0:男 1：女
    private String sex;
    //岗位id
    private Integer postId;
    //岗位名称
    private String postName;
    //用户权限列表,不属于用户表字段，需要排除
    @TableField(exist = false)
    List<Menu> permissionList;
}
