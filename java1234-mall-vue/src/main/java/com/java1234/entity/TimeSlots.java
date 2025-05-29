package com.java1234.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.util.Date;

@TableName("t_time_slots")
@Data
public class TimeSlots {

    @TableId(type = com.baomidou.mybatisplus.annotation.IdType.AUTO)
    private Integer id; // 编号

    private String time; // 时间段（如“09:00-11:00”）

    private Integer remaining; // 剩余名额

    private Integer venueId;

    private Dates date; // 关联的日期信息（如果需要）

    @TableField(select = false, exist = false)
    private Reservation reservations; // 关联的预约信息（如果需要）
}