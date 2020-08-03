package com.example.core.common;

/**
 *
 */
public interface IResultCode {
    /**
     * 获取状态码
     *
     * @return 状态码
     */
    String getCode();

    /**
     * 获取返回消息
     *
     * @return 返回消息
     */
    String getMessage();
}
