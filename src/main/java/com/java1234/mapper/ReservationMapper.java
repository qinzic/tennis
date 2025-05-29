package com.java1234.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.java1234.entity.Reservation;

import java.util.List;
import java.util.Map;

public interface ReservationMapper extends BaseMapper<Reservation> {

    /**
     * 根据条件 分页查询预订
     * @param map 包含查询条件及分页参数的Map
     * @return 满足条件的预订列表
     */
    public List<Reservation> list(Map<String, Object> map);

    /**
     * 根据条件，查询预订总记录数
     * @param map 包含查询条件的Map
     * @return 满足条件的预订总数
     */
    public Long getTotal(Map<String, Object> map);

}