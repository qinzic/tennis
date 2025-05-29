package com.java1234.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.java1234.entity.PageBean;
import com.java1234.entity.R;
import com.java1234.entity.Reservation;
import com.java1234.service.IReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/reservation")
public class AdminReservationController {

    @Autowired
    private IReservationService reservationService;

    /**
     * 根据条件分页查询预订记录
     * @param pageBean 包含查询条件、起始位置和页面大小
     * @return 返回包含预订列表和总数的响应结果
     */
    @PostMapping("/list")
    public R list(@RequestBody PageBean pageBean) {
        // 打印接收到的pageBean对象以供调试
        System.out.println(pageBean);

        // 创建一个HashMap来存放查询参数
        Map<String, Object> map = new HashMap<>();
        if (pageBean.getQuery() != null) {
            map.put("nickName", pageBean.getQuery().trim());
        }
        map.put("start", Math.max(0, pageBean.getStart())); // 确保开始位置非负
        map.put("pageSize", Math.max(1, pageBean.getPageSize())); // 确保页面大小至少为1

        // 查询预订记录列表和总数量
        List<Reservation> reservations = reservationService.list(map);
        Long total = reservationService.getTotal(map);

        // 查询统计数据
        Map<String, Integer> stats = reservationService.getStats();

        // 构建返回的结果Map
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("reservationList", reservations.stream().map(reservation -> {
            return new HashMap<String, Object>() {{
                put("id", reservation.getId());
                put("nickName", reservation.getNickName());
                put("venueName", reservation.getVenueName());

                put("userAvatar", reservation.getUserAvatar()); // 确保实体类中有这个字段
                put("reservationTime", reservation.getReservationTime());
                put("status", reservation.getStatus());
            }};
        }).collect(Collectors.toList()));
        resultMap.put("total", total);
        resultMap.put("stats", stats);

        // 返回封装好的结果
        return R.ok(resultMap);
    }

    /**
     * 批准场地预订
     *
     * @param reservation 包含预订ID的预订实体
     * @return 返回操作结果
     */
    @PostMapping("/approve")
    public R approve(@RequestBody Reservation reservation) {
        Reservation resultReservation = reservationService.getById(reservation.getId());
        if (resultReservation != null) {
            resultReservation.setStatus(1);
            reservationService.saveOrUpdate(resultReservation);
            return R.ok();
        } else {
            return R.error();
        }
    }

    /**
     * 拒绝场地预订
     *
     * @param reservation 包含预订ID的预订实体
     * @return 返回操作结果
     */
    @PostMapping("/reject")
    public R reject(@RequestBody Reservation reservation) {
        Reservation resultReservation = reservationService.getById(reservation.getId());
        if (resultReservation != null) {
            resultReservation.setStatus(2);
            reservationService.saveOrUpdate(resultReservation);
            return R.ok();
        } else {
            return R.error();
        }
    }
}