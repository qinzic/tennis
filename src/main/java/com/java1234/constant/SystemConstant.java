package com.java1234.constant;

/**
 * 系统级静态变量
 * @author java1234_小锋
 * @site www.java1234.com
 * @company Java知识分享网
 * @create 2024-08-13 上午 9:51
 */
public class SystemConstant {

    /**
     * token
     */
    public static final int JWT_ERRCODE_NULL = 4000;			//Token不存在
    public static final int JWT_ERRCODE_EXPIRE = 4001;			//Token过期
    public static final int JWT_ERRCODE_FAIL = 4002;			//验证不通过

    /**
     * JWT
     */
    public static final String JWT_SECERT = "56e9646a3ee5ba4d3b595e3fe48a0364";			//密匙
    public static final long JWT_TTL = 24*60 * 60 * 1000;									//token有效时间
}
