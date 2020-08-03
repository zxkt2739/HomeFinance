package com.example.core.enumeration;

import lombok.Getter;

/**
 * 错误码枚举
 *
 * @date 2019/04/08
 */
@Getter
public enum ErrorCodeEnum {
    SUCCESS_CODE("100200", "成功"),
    LOGIN_GET_TOKEN_ERROR("100400", "登录异常，获取token失败"),
    LOGIN_MATCHS_TOKEN_ERROR("100400","登录密码错误"),
    LOGIN_TOKEN_CHECK_ERROR("100400", "token验证失败"),
    LOGIN_GET_HEADER_INFO_ERROR("100400", "获取Header信息异常"),
    LOGIN_CHECK_HEADER_DATE_ERROR("100400", "Header日期校验异常"),
    USER_NOT_EXIST_ERROR("100400", "用户不存在"),
    INSERT_FAILED_ERROR("100400", "创建失败"),
    UPDATE_FAILED_ERROR("100400", "更新失败"),
    DELETE_FAILED_ERROR("100400", "删除失败"),
    LOGIN__ERROR("100402","登录失败"),
    HTTP_SEND_ERROR("100400", "HTTP请求失败"),
    INTERCEPTOR__ERROR("100401","拦截器异常"),
    FREEZE__ERROR("100406","账号已被冻结"),
    FAIL_CODE("100400", "Operate unsuccessfully"),

    FAIL_CODE_ERROR("400", "调取分期付系统kyc失败"),
    APP_SIGN_ERROR("400", "加密验证失败"),
    APP_TOKEN_ERROR("401", "token验证失败"),

    LAT_PAY_FAILED("100400", "Transaction Abandoned"),
    LAT_PAY_TIE_ON_CARD_FAILED("100400", "Tie on card failed"),
    LAT_PAY_DOUBLE("100300", "交易可疑");

    private final String code;

    private final String message;

    ErrorCodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String toString() {
        return "code:" + this.code + "| message:" + message;
    }
}
