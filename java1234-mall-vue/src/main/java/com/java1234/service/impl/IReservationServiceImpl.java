package com.java1234.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.java1234.entity.Reservation;
import com.java1234.mapper.ReservationMapper;
import com.java1234.service.IReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 场地预订服务实现类
 */
@Service("reservationService")
public class IReservationServiceImpl extends ServiceImpl<ReservationMapper, Reservation> implements IReservationService {

    @Autowired
    private ReservationMapper reservationMapper;

    /**
     * 根据条件分页查询场地预订信息
     *
     * @param map 包含查询参数的Map
     * @return 返回预订列表
     */
    @Override
    public List<Reservation> list(Map<String, Object> map) {
        return reservationMapper.list(map);
    }

    /**
     * 获取总记录数
     *
     * @param map 包含查询参数的Map
     * @return 总记录数
     */
    @Override
    public Long getTotal(Map<String, Object> map) {
        return reservationMapper.getTotal(map);
    }

    /**
     * 生成stats
     * @return
     */
    @Override
    public Map<String, Integer> getStats() {
        // 使用 MyBatis-Plus 的聚合查询来获取统计数据
        // 这里我们假设数据库中有相应的字段来表示状态（0: 待审核, 1: 已批准, 2: 已拒绝）

        // 查询总订单数
        Integer totalOrders = baseMapper.selectCount(null);

        // 查询待审核订单数
        Integer pendingOrders = baseMapper.selectCount(new QueryWrapper<Reservation>().eq("status", 0));

        // 查询已批准订单数
        Integer approvedOrders = baseMapper.selectCount(new QueryWrapper<Reservation>().eq("status", 1));

        // 查询已拒绝订单数
        Integer rejectedOrders = baseMapper.selectCount(new QueryWrapper<Reservation>().eq("status", 2));

        // 构建统计数据Map
        Map<String, Integer> stats = new HashMap<>();
        stats.put("totalOrders", totalOrders);
        stats.put("pendingOrders", pendingOrders);
        stats.put("approvedOrders", approvedOrders);
        stats.put("rejectedOrders", rejectedOrders);

        return stats;
    }

    /**
     * 检查指定时间段是否可用
     *
     * @param venueId   场地ID
     * @param date      预约日期
     * @param timeSlot  时间段
     * @return 是否可用
     */
    public boolean isTimeSlotAvailable(Integer venueId, String date, String timeSlot) {
        QueryWrapper<Reservation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("venueId", venueId)
                .eq("dateId", date)
                .eq("timeSlotId", timeSlot);
        return this.count(queryWrapper) == 0;
    }

    /**
     * 创建新的预约记录
     *
     * @param reservation 预约实体
     * @return 是否成功
     */
    @Override
    public boolean createReservation(Reservation reservation) {
        // 检查时间段是否可用
//        if (!isTimeSlotAvailable(reservation.getVenueId(), reservation.getTimeSlotId())) {
//            return false;
//        }

        // 保存预约记录到数据库
        return this.save(reservation);
    }

}