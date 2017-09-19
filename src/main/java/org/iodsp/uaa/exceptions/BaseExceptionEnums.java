package org.iodsp.uaa.exceptions;

import lombok.Getter;

@Getter
public enum BaseExceptionEnums implements ExceptionEnums {

    EXCEPTION_ENUMS(99999, "系统异常:");

    private Integer code;

    private String message;

    private BaseExceptionEnums(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
