package com.java1234.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.util.Date;

@TableName("t_dates")
@Data
public class Dates {

    private Date date; // 日期

    private String day; // 星期几（如“星期三”）

    private Integer status; // 0: 不可预约, 1: 可预约

    @JsonSerialize(using=CustomDateTimeSerializer.class)
    private Date createdAt; // 创建时间

    @TableField(select = false, exist = false)
    private TimeSlots timeSlots; // 关联的时间段信息（如果需要）
}