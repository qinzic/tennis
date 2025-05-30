package com.java1234.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.beans.Transient;
import java.io.Serializable;
import java.util.Date;

/**
 * 微信用户信息实体
 * 系统级静态变量
 * @author java1234_小泽
 * @create 2024-08-13 上午 9:51
 */
@TableName("t_wxUserInfo")
@Data
public class WxUserInfo implements Serializable {

    private Integer id; // 用户编号

    private String openid; // 用户唯一标识

    private String nickName; // 用户昵称

    private String avatarUrl; // 用户头像图片的 URL

    @JsonSerialize(using=CustomDateTimeSerializer.class)
    private Date registerDate; // 注册日期

    @JsonSerialize(using=CustomDateTimeSerializer.class)
    private Date lastLoginDate; // 最后登录日期

    @TableField(select = false,exist = false)
    private String code; // 微信用户code 前端传来的


}
