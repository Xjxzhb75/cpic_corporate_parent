package com.cpic.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author xinjianxun
 * @version 1.0
 * @description: TODO
 * @date 2022/6/10 1:26
 */
@Data
@AllArgsConstructor
public class ResultVo <T>{
    private String msg;
    private int code;
    private T data;
}
