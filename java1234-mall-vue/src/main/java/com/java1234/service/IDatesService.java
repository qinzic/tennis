package com.java1234.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.java1234.entity.Dates;

import java.util.List;

public interface IDatesService extends IService<Dates> {
    /**
     * 获取可预约的日期列表
     * @param venueId 场地ID（可选）
     * @return 可预约的日期列表
     */
    List<Dates> getAvailableDates(Long venueId);
}