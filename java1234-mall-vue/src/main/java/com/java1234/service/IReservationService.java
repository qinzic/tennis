package com.java1234.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.java1234.entity.Reservation;

import java.util.List;
import java.util.Map;

/**
 * 场地预订服务接口
 */
public interface IReservationService extends IService<Reservation> {

    /**
     * 根据条件分页查询场地预订信息
     *
     * @param map 包含查询参数的Map
     * @return 返回预订列表
     */
    List<Reservation> list(Map<String, Object> map);

    /**
     * 获取总记录数
     *
     * @param map 包含查询参数的Map
     * @return 总记录数
     */
    Long getTotal(Map<String, Object> map);

    /**
     * 获取状态信息
     * @return
     */
    Map<String, Integer> getStats();


    /**
     * 创建新的预约记录
     *
     * @param reservation 预约实体
     * @return 是否成功
     */
    boolean createReservation(Reservation reservation);

    /**
     * 能不能用
     * @param venueId
     * @param date
     * @param timeSlot
     * @return
     */
    public boolean isTimeSlotAvailable(Integer venueId, String date, String timeSlot);
}