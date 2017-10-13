package org.iodsp.uaa.exceptions;

import lombok.Getter;

@Getter
public enum RoleExceptionEnums implements ExceptionEnums {

    ADD_EXCEPTION(10031, "添加角色失败"),
    INVALID_EXCEPTION(10032, "角色信息不存在"),
    NOSUCH_EXCEPTION(10033, "角色信息不存在"),
    HAS_EXCEPTION(10034, "已经存在角色失败"),
    PARAM_EXCEPTION(10035, "参数错误");

    private Integer code;

    private String message;

    private RoleExceptionEnums(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
