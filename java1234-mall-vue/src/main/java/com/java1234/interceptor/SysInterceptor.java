package com.java1234.interceptor;


import com.java1234.entity.R;
import com.java1234.util.JwtUtils;
import com.java1234.util.StringUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 鉴权拦截器
 * 系统级静态变量
 * @author java1234_小泽
 * @create 2024-08-13 上午 9:51
 */
public class SysInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path=request.getRequestURI();
        System.out.println(path);
        if(path.startsWith("//venue/")){
            System.out.println("放行venue");
            return true;
        }
        if("/user/register".equals(path)){
            System.out.println("放行注册接口:{}");
            return true;
        }
        if(handler instanceof HandlerMethod){
            String token = request.getHeader("token");
            System.out.println("token:"+token);
            if(StringUtil.isEmpty(token)){
                System.out.println("token为空！");
                throw new RuntimeException("签名验证不存在");
            }else{
                Claims claims = JwtUtils.validateJWT(token).getClaims();
                // 管理员 admin开头
                if(path.startsWith("/admin")){
                    if(claims==null||!claims.getSubject().equals("admin")||!claims.getId().equals("-1")){
                        throw new RuntimeException("鉴权失败！");
                    }else{
                        System.out.println("验证成功");
                        return true;
                    }
                }else{ // 普通用户 鉴权
                    if(claims==null){
                        throw new RuntimeException("鉴权失败！");
                    }else{
                        System.out.println("验证成功");
                        return true;
                    }
                }

            }
        }else{
            return true;
        }
    }


}
