package org.iodsp.uaa.exceptions;

import lombok.Getter;

@Getter
public enum ScopeExceptionEnums implements ExceptionEnums {

    ADD_EXCEPTION(10021, "添加授权作用域服务失败"),
    INVALID_EXCEPTION(10022, "作用域信息不存在"),
    NOSUCH_EXCEPTION(10023, "作用域信息不存在"),
    HAS_EXCEPTION(10024, "作用域信息已经存在"),
    PARAM_EXCEPTION(10025, "参数非法");

    private Integer code;

    private String message;

    private ScopeExceptionEnums(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
