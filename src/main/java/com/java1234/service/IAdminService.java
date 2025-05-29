package com.java1234.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.java1234.entity.Admin;


/**
 * 管理员Service接口
 * @author java1234_小泽
 * @create 2024-12-13 上午 9:51
 */
public interface IAdminService extends IService<Admin> {

    /**
     * 修改
     * @param admin
     * @return
     */
    public Integer update(Admin admin);

}
