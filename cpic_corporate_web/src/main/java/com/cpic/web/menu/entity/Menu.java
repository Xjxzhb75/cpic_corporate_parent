package com.cpic.web.menu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author xinjianxun
 * @date 2022/6/14 8:07
 * @param: $
 * @return: $
 * @description: TODO
 */
@Data
@TableName(value = "fkxx_menu")
public class Menu implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long Id;

    private Long parentId;

    private String parentName;

    private String label;
    //权限标识

    private String code;
    //路由地址

    private String path;
    //路由名称

    private String name;
    //路由页面对应的路径

    private String url;
    //序号

    private Integer orderNum;
    //类型  (0 目录 1菜单，2按钮)

    private String type;
    //菜单图标

    private String icon;
    //备注

    private String remark;
    //创建时间

    private Date createTime;
    //更新时间

    private Date updateTime;
    //菜单的子级
    //不是数据库的字段需要排除
    //实体类与json互转的时候 属性值为null的不参与序列化,也就是不发出去
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @TableField(exist = false)
    private List<Menu> children = new ArrayList<>();
    //用于前端判断是菜单 、目录 、按钮
    @TableField(exist = false)
    private String value;
    //该字段为非表字段
    @TableField(exist = false)
    private Boolean open;
}
