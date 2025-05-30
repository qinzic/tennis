package com.java1234.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.java1234.entity.Dates;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DatesMapper extends BaseMapper<Dates> {
    // 可以在这里添加自定义的查询方法
}