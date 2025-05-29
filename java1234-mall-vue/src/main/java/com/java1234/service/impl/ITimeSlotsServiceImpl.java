package com.java1234.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.java1234.entity.TimeSlots;
import com.java1234.mapper.TimeSlotsMapper;
import com.java1234.service.ITimeSlotsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ITimeSlotsServiceImpl extends ServiceImpl<TimeSlotsMapper, TimeSlots> implements ITimeSlotsService {
    /**
     * 获取指定日期的时间段列表
     * @param venueId 场地ID
     * @param date 日期
     * @return 时间段列表
     */
    @Override
    public List<TimeSlots> getTimeSlotsByDateAndVenue(Long venueId, String date) {
        QueryWrapper<TimeSlots> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("venueId", venueId);
        queryWrapper.eq("date", date);
        queryWrapper.gt("remaining", 0); // 只获取还有剩余名额的时间段

        return list(queryWrapper);
    }

    /**
     * 检查某个时间段是否有剩余名额
     * @param timeSlotId 时间段ID
     * @return 是否有剩余名额
     */
    @Override
    public boolean hasRemainingSlots(Long timeSlotId) {
        TimeSlots timeSlot = getById(timeSlotId);
        return timeSlot != null && timeSlot.getRemaining() > 0;
    }

    /**
     * 更新时间段的剩余名额
     * @param timeSlotId 时间段ID
     * @param remaining 新的剩余名额
     * @return 是否更新成功
     */
    @Override
    public boolean updateRemaining(Long timeSlotId, Integer remaining) {
        TimeSlots timeSlot = new TimeSlots();
        timeSlot.setRemaining(remaining);
        return updateById(timeSlot);
    }

    /**
     * 预订时间段
     * @param timeSlotId 时间段ID
     * @return 是否预订成功
     */
    @Override
    public boolean reserveTimeSlot(Long timeSlotId) {
        TimeSlots timeSlot = getById(timeSlotId);
        if (timeSlot == null || timeSlot.getRemaining() <= 0) {
            return false; // 没有剩余名额或时间段不存在
        }

        // 减少一个名额
        timeSlot.setRemaining(timeSlot.getRemaining() - 1);
        return updateById(timeSlot);
    }
}