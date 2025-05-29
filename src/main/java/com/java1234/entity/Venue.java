package com.java1234.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.util.Date;

@TableName("t_venues")
@Data
public class Venue {

    private Integer id; // 编号

    private String name; // 场地名称

    private String imageUrl; // 场地图片URL

    private int status;

    private Integer capacity; // 剩余人数（容量）

    private String description; // 场地描述

    @JsonSerialize(using=CustomDateTimeSerializer.class)
    private Date createdDate; // 创建时间

    @JsonSerialize(using=CustomDateTimeSerializer.class)
    private Date updatedDate; // 更新时间
}