package org.iodsp.uaa.exceptions;

import lombok.Getter;

@Getter
public enum AuthorityExceptionEnums implements ExceptionEnums {

    ADD_EXCEPTION(10031, "添加权限失败"),
    INVALID_EXCEPTION(10032, "权限信息不存在"),
    NOSUCH_EXCEPTION(10033, "权限信息不存在"),
    HAS_EXCEPTION(10034, "已经存在权限失败"),
    PARAM_EXCEPTION(10035, "参数错误");

    private Integer code;

    private String message;

    private AuthorityExceptionEnums(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
