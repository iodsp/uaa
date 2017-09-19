package org.iodsp.uaa.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReturnObject {

    private Integer code = 0;

    // http code
    private Integer status = 200;

    private Object data;

    private String message = "";
}
