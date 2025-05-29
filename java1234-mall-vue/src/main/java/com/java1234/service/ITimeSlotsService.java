package com.java1234.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.java1234.entity.TimeSlots;

import java.util.List;

public interface ITimeSlotsService extends IService<TimeSlots> {
    /**
     * 获取指定日期的时间段列表
     * @param venueId 场地ID
     * @param date 日期
     * @return 时间段列表
     */
    List<TimeSlots> getTimeSlotsByDateAndVenue(Long venueId, String date);

    /**
     * 检查某个时间段是否有剩余名额
     * @param timeSlotId 时间段ID
     * @return 是否有剩余名额
     */
    boolean hasRemainingSlots(Long timeSlotId);

    /**
     * 更新时间段的剩余名额
     * @param timeSlotId 时间段ID
     * @param remaining 新的剩余名额
     * @return 是否更新成功
     */
    boolean updateRemaining(Long timeSlotId, Integer remaining);

    /**
     * 预订时间段
     * @param timeSlotId 时间段ID
     * @return 是否预订成功
     */
    boolean reserveTimeSlot(Long timeSlotId);
}