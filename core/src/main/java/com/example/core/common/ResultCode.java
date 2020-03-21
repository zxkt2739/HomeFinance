package com.example.core.common;

import lombok.Getter;

/**
 *
 * @author faker
 */
@Getter
public enum ResultCode implements IResultCode {
    /**
     * 成功
     */
    OK("100200", "成功"),
    /**
     * 失败
     */
    ERROR("100400", "失败");

    /**
     * 返回码
     */
    private String code;

    /**
     * 返回消息
     */
    private String message;

    ResultCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
