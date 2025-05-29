package com.java1234.entity;

import io.jsonwebtoken.Claims;

/**
 * jwt验证信息
 * 系统级静态变量
 * @author java1234_小锋
 * @create 2024-08-13 上午 9:51
 */
public class CheckResult {

    private int errCode;

    private boolean success;

    private Claims claims;

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Claims getClaims() {
        return claims;
    }

    public void setClaims(Claims claims) {
        this.claims = claims;
    }

}
