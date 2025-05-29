package com.java1234.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.java1234.entity.Order;

import java.util.List;
import java.util.Map;


/**
 * 订单主表Mapper接口
 * 系统级静态变量
 * @author java1234_小泽
 * @create 2024-12-13 上午 9:51
 */
public interface OrderMapper extends BaseMapper<Order> {

    /**
     * 根据条件 分页查询订单
     * @param map
     * @return
     */
    public List<Order> list(Map<String,Object> map);

    /**
     * 根据条件，查询订单总记录数
     * @param map
     * @return
     */
    public Long getTotal(Map<String,Object> map);

}
