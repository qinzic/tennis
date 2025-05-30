package com.java1234.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.java1234.entity.Venue;

import java.util.List;

/**
 * 场地 服务类
 */
public interface IVenueService extends IService<Venue> {

    /**
     * 获取所有可预约的场地列表
     * @return 场地列表
     */
    List<Venue> findAvailableVenues();

    /**
     * 根据ID获取场地详情
     * @param id 场地ID
     * @return 场地详情
     */
    Venue getVenueById(Integer id);

    // 可以根据业务需求添加其他方法
}