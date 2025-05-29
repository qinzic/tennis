package com.java1234.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.util.Date;

@TableName("t_reservations")
@Data
public class Reservation {

    private Integer id; // 编号

    private Integer venueId; // 场地ID

    private  String venueName;//场地名称

    private Integer userId; // 用户ID

    private String nickName;//用户昵称

    private String userAvatar;//用户头像

    private Date date;//日期id

    private String timeSlotId;//时期id

    private Date reservationTime; // 预约日期

    private String startTime; // 开始时间

    private String endTime; // 结束时间

    private Integer status; // 0: 待处理, 1: 已确认, 2: 已取消, 3: 已完成, 4: 已拒绝, 5: 已重新安排

    @JsonSerialize(using=CustomDateTimeSerializer.class)
    private Date createdDate; // 创建时间

    @JsonSerialize(using=CustomDateTimeSerializer.class)
    private Date updateDate; // 更新时间

    @TableField(select = false, exist = false)
    private Venue venue; // 关联的场地信息

    @TableField(select = false, exist = false)
    private WxUserInfo wxUserInfo; // 微信用户信息
}