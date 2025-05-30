package com.java1234.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.java1234.entity.Dates;
import com.java1234.mapper.DatesMapper;
import com.java1234.service.IDatesService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IDatesServiceImpl extends ServiceImpl<DatesMapper, Dates> implements IDatesService {
    /**
     * 获取可预约的日期列表
     * @param venueId 场地ID（可选）
     * @return 可预约的日期列表
     */
    @Override
    public List<Dates> getAvailableDates(Long venueId) {
        QueryWrapper<Dates> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1); // 只获取可预约的日期

        if (venueId != null) {
            queryWrapper.eq("venueId", venueId); // 如果提供了场地ID，则按场地过滤
        }

        return list(queryWrapper);
    }
}