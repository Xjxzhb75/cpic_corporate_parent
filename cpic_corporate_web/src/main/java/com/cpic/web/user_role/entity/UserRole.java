package com.cpic.web.user_role.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author xinjianxun
 * @date 2022/6/15 9:09
 * @param: $
 * @return: $
 * @description: TODO
 */
@Data
@TableName(value = "fkxx_user_role")
public class UserRole {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long roleId;
}
