package com.java1234.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.java1234.entity.*;
import com.java1234.service.IDatesService;
import com.java1234.service.IReservationService;
import com.java1234.service.ITimeSlotsService;
import com.java1234.service.IVenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 场地控制器
 * 系统级静态变量
 * @author java1234_小锋
 * @create 2024-08-13 上午 9:51
 */
@RestController
@RequestMapping("/venue")
public class VenueController {

    @Autowired
    private IVenueService venueService;
    @Autowired
    private IDatesService datesService;
    @Autowired
    private ITimeSlotsService timeSlotsService;
    @Autowired
    private IReservationService reservationService;

    /**
     * 获取所有场地列表
     * @return
     */
    @GetMapping("/list")
    public R list() {
        List<Venue> venues = venueService.list();
        Map<String, Object> result = new HashMap<>();
        result.put("data", venues);
        return R.ok(result);
    }

    /**
     * 获取可预约的日期列表
     * @param venueId 可选参数，用于按场地过滤
     * @return
     */
    @GetMapping("/dates")
    public R getDateList(@RequestParam(required = false) Long venueId) {
        QueryWrapper<Dates> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1); // 只获取可预约的日期

        if (venueId != null) {
            queryWrapper.eq("venueId", venueId); // 如果提供了场地ID，则按场地过滤
        }

        List<Dates> dateList = datesService.list(queryWrapper);

//        // 格式化日期
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("data", dateList);
//        resultMap.put("data", dateList.stream()
//                .map(date -> {
//                    Map<String, Object> dateMap = new HashMap<>();
//                    dateMap.put("date", dateFormat.format(date.getDate()));
//                    return dateMap;
//                })
//                .toList());

        return R.ok(resultMap);
    }

    /**
     * 获取指定日期的时间段
     * @param venueId 场地ID
     * @param date 预约日期
     * @return
     */
//    @GetMapping("/timeSlots")
//    public R getTimeSlots(@RequestParam Long venueId, @RequestParam String date) {
//        QueryWrapper<TimeSlots> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("venueId", venueId);
//        queryWrapper.eq("date", date);
//        queryWrapper.gt("remaining", 0); // 只获取还有剩余名额的时间段
//
//        List<TimeSlots> timeSlots = timeSlotsService.list(queryWrapper);
//        Map<String, Object> resultMap = new HashMap<>();
//        resultMap.put("data", timeSlots);
////        resultMap.put("data", timeSlots.stream()
////                .map(slot -> {
////                    Map<String, Object> slotMap = new HashMap<>();
////                    slotMap.put("id", slot.getId());
////                    slotMap.put("time", slot.getTime());
////                    slotMap.put("remaining", slot.getRemaining());
////                    return slotMap;
////                })
////                .toList());
//
//        return R.ok(resultMap);
//    }

    @PostMapping("/reserve")
    public R reserve( // userId 变为可选参数，设置默认值
            @RequestParam Integer venueId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date
    ) {
        // 创建新的预约记录
        Venue venue = venueService.getVenueById(venueId);
        String venueName = venue.getName();
        Reservation reservation = new Reservation();
        reservation.setUserId(1);  // 使用提供的 userId 或默认值
        reservation.setNickName("zcyoo");
        reservation.setVenueId(venueId);
        reservation.setDate(date);  // 设置预约日期
        reservation.setReservationTime(new Date());  // 当前时间作为预约时间
        reservation.setStatus(0);  // 默认状态为待处理
        reservation.setStartTime("09:00:00");
        reservation.setEndTime("23:59:59");
        reservation.setVenueName(venueName);
        reservation.setCreatedDate(new Date());
        reservation.setUpdateDate(new Date());
        reservation.setUserAvatar("https://thirdwx.qlogo.cn/mmopen/vi_32/GJeHhQSiczuph0tg4JyleLeDUnIghqXlQBiaUAJMvia8GRSjibYdPmFQrvPic6sAhyTz4C8Ivu1aWah3KpFMBdsPibEw/132");
        reservation.setTimeSlotId("1");

        try {
            // 调用服务层方法创建预约
            if (reservationService.createReservation(reservation)) {
                // 使用 HashMap 封装简化的预约信息
                Map<String, Object> data = new HashMap<>();
                data.put("id", reservation.getId());
                data.put("venueId", reservation.getVenueId());
                data.put("userId", reservation.getUserId());
                data.put("date", reservation.getDate());
                data.put("reservationTime", reservation.getReservationTime());
                data.put("status", reservation.getStatus());
                data.put("createdDate", reservation.getCreatedDate());
                return R.ok(data);
            } else {
                return R.error("该日期已约满");
            }
        } catch (Exception e) {
            e.printStackTrace();  // 打印堆栈跟踪以便调试
            return R.error("预约失败: " + e.getMessage());
        }
    }




    /**
     * 获取海报
     * @return
     */
    @GetMapping("/getPoster")
    public R getPoster() {
        // 这里可以添加业务逻辑，例如生成或获取海报图片URL
        String posterUrl = "poster.jpg"; // 示例海报URL
        Map<String, Object> result = new HashMap<>();
        result.put("posterUrl", posterUrl);
        return R.ok(result);
    }

    /**
     * 获取订单凭证
     * @return
     */
    @GetMapping("/getTicket")
    public R getTicket() {
        // 这里可以添加业务逻辑，例如生成或获取订单凭证图片URL
        String ticketUrl = "ticket.jpg"; // 示例凭证URL
        Map<String, Object> result = new HashMap<>();
        result.put("ticketUrl", ticketUrl);
        return R.ok(result);
    }
}