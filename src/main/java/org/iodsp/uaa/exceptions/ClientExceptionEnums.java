package org.iodsp.uaa.exceptions;

import lombok.Getter;

@Getter
public enum ClientExceptionEnums implements ExceptionEnums {

    ADD_EXCEPTION(10001, "添加授权客户端失败"),
    INVALID_CLIENT_EXCEPTION(10002, "客户端信息不存在"),
    NOSUCH_CLIENT_EXCEPTION(10003, "客户端信息不存在");

    private Integer code;

    private String message;

    private ClientExceptionEnums(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
