package com.java1234.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.java1234.entity.Admin;

/**
 * 管理员Mapper接口
 * 系统级静态变量
 * @author java1234_小泽
 * @create 2024-12-13 上午 9:51
 */
public interface AdminMapper extends BaseMapper<Admin> {

    /**
     * 修改
     * @param admin
     * @return
     */
    public Integer update(Admin admin);

}
