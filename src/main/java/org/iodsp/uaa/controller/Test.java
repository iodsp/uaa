package org.iodsp.uaa.controller;

import org.iodsp.uaa.dto.Exception;
import org.iodsp.uaa.dto.ReturnObject;
import org.iodsp.uaa.exceptions.BaseExceptionEnums;
import org.iodsp.uaa.exceptions.ServiceException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Test {

    @RequestMapping("/get")
    public ReturnObject get() {
        ReturnObject returnObject = new ReturnObject();
        Exception test = new Exception();
        test.setUrl("http://www.baidu.com");
        throw new ServiceException(BaseExceptionEnums.EXCEPTION_ENUMS);
        //returnObject.setData(test);
        //return returnObject;
    }
}
