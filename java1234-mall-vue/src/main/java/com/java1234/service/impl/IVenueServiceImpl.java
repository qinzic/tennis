package com.java1234.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.java1234.entity.Venue;
import com.java1234.mapper.VenueMapper;
import com.java1234.service.IVenueService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 场地 服务实现类
 */
@Service
public class IVenueServiceImpl extends ServiceImpl<VenueMapper, Venue> implements IVenueService {

    @Override
    public List<Venue> findAvailableVenues() {
        QueryWrapper<Venue> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1); // 假设 status=1 表示可预约
        return list(queryWrapper);
    }

    @Override
    public Venue getVenueById(Integer id) {
        return getById(id);
    }

}