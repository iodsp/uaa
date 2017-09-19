package org.iodsp.uaa.configure;

import org.iodsp.uaa.dto.Exception;
import org.iodsp.uaa.dto.ReturnObject;
import org.iodsp.uaa.exceptions.ServiceException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = ServiceException.class)
    @ResponseBody
    public ReturnObject jsonErrorHandler(HttpServletRequest req, ServiceException exception) {
        ReturnObject returnObject = new ReturnObject();
        returnObject.setCode(exception.getExceptionEnums().getCode());
        returnObject.setMessage(exception.getExceptionEnums().getMessage());
        Exception exceptionData = new Exception();
        exceptionData.setUrl(req.getRequestURL().toString());
        returnObject.setData(exceptionData);
        return returnObject;
    }
}
