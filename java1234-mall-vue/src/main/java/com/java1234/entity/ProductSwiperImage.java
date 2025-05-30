package com.java1234.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 产品轮播图片
 * 系统级静态变量
 * @author java1234_小泽
 * @create 2024-08-13 上午 9:51
 */
@TableName("t_product_swiper_image")
@Data
public class ProductSwiperImage {

    private Integer id; // 编号

    private String image; // 图片名称

    private Integer sort; // 排列序号 从小到大排序

    private Integer productId; // 所属产品


}
