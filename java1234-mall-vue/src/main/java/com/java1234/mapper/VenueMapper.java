package com.java1234.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.java1234.entity.Venue;
import org.apache.ibatis.annotations.Mapper;

/**
 * 场地 Mapper 接口
 */
@Mapper
public interface VenueMapper extends BaseMapper<Venue> {
    // 如果有特殊的查询需求，可以在这里添加自定义的方法
}