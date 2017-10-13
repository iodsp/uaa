package org.iodsp.uaa.exceptions;

import lombok.Getter;

@Getter
public enum ResourceExceptionEnums implements ExceptionEnums {

    ADD_EXCEPTION(10011, "添加授权资源服务失败"),
    INVALID_EXCEPTION(10022, "资源信息不存在"),
    NOSUCH_EXCEPTION(10033, "资源信息不存在"),
    HAS_EXCEPTION(10034, "资源信息已经存在"),
    PARAM_EXCEPTION(10035, "参数非法");

    private Integer code;

    private String message;

    private ResourceExceptionEnums(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
