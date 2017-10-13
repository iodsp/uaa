package org.iodsp.uaa.exceptions;

import lombok.Getter;

@Getter
public enum UserExceptionEnums implements ExceptionEnums {

    ADD_EXCEPTION(10031, "添加用户失败"),
    INVALID_USER_EXCEPTION(10032, "用户信息不存在"),
    NOSUCH_USER_EXCEPTION(10033, "用户信息不存在"),
    HAS_USER_EXCEPTION(10034, "已经存在用户失败");

    private Integer code;

    private String message;

    private UserExceptionEnums(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
