package com.java1234.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.java1234.constant.SystemConstant;
import com.java1234.entity.Admin;
import com.java1234.entity.R;
import com.java1234.service.IAdminService;
import com.java1234.util.JwtUtils;
import com.java1234.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AdminController {

    @Autowired
    private IAdminService adminService;

    private final static Logger logger= LoggerFactory.getLogger(AdminController.class);

    /**
     * 管理员登录
     * @param admin
     * @return
     */
    @PostMapping("/adminLogin")
    public R adminLogin(@RequestBody Admin admin){
        if(admin==null){
            return R.error();
        }
        if(StringUtil.isEmpty(admin.getUserName())){
            return R.error("用户名不能为空！");
        }
        if(StringUtil.isEmpty(admin.getPassword())){
            return R.error("密码不能为空！");
        }
        Admin resultAdmin = adminService.getOne(new QueryWrapper<Admin>().eq("userName", admin.getUserName()));
        if(resultAdmin==null){
            return R.error("用户名不存在！");
        }
        if(!resultAdmin.getPassword().trim().equals(admin.getPassword())){
            return R.error("用户名或者密码错误！");
        }
        String token = JwtUtils.createJWT("-1", "admin", SystemConstant.JWT_TTL);
        Map<String,Object> resultMap=new HashMap<String,Object>();
        resultMap.put("token",token);
        return R.ok(resultMap);
    }

    /**
     * 修改密码
     * @param admin
     * @return
     */
    @PostMapping("/admin/modifyPassword")
    public R modifyPassword(@RequestBody Admin admin){
        if(StringUtil.isEmpty(admin.getUserName())){
            return R.error("用户名不能为空！");
        }
        if(StringUtil.isEmpty(admin.getNewPassword())){
            return R.error("新密码不能为空！");
        }
        adminService.update(admin);
        return R.ok();
    }

    /**
     * 管理员注册
     * @param admin
     * @return
     */
    @PostMapping("/user/register")
    public R adminRegister(@RequestBody Admin admin) {
        // 日志记录
        logger.info("收到管理员注册请求: {}", admin);
        admin.setNewPassword(admin.getPassword());
        // 基本参数校验
        if (admin == null) {
            return R.error("注册信息不能为空！");
        }
        if (StringUtil.isEmpty(admin.getUserName())) {
            return R.error("用户名不能为空！");
        }
        if (StringUtil.isEmpty(admin.getPassword())) {
            return R.error("密码不能为空！");
        }
        if (StringUtil.isEmpty(admin.getNewPassword())) {
            return R.error("确认密码不能为空！");
        }
        if (!admin.getPassword().equals(admin.getNewPassword())) {
            return R.error("两次输入的密码不一致！");
        }

        // 检查用户名是否已存在
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userName", admin.getUserName());
        Admin existingAdmin = adminService.getOne(queryWrapper);
        if (existingAdmin != null) {
            return R.error("用户名已存在！");
        }

        // 注册新管理员
        admin.setPassword(admin.getPassword().trim()); // 去除密码前后空格
        admin.setNewPassword(null); // 不需要保存确认密码字段
        boolean result = adminService.save(admin);

        if (result) {
            // 注册成功后生成 JWT 并返回给客户端
            String token = JwtUtils.createJWT("-1", "admin", SystemConstant.JWT_TTL);
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("token", token);
            return R.ok(resultMap).put("msg", "注册成功");
        } else {
            return R.error("注册失败，请稍后再试！");
        }
    }

}
