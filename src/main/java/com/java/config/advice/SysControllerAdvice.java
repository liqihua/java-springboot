package com.java.config.advice;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.java.common.constance.ApiConstance;
import com.java.sys.common.basic.controller.BaseController;
import com.java.sys.common.basic.result.BaseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.PrintWriter;
import java.io.StringWriter;


@ControllerAdvice(basePackages = {"com"})
public class SysControllerAdvice extends BaseController{
    private static final Logger LOG = LoggerFactory.getLogger(SysControllerAdvice.class);

    //400错误->缺少参数异常
    @ExceptionHandler({RuntimeException.class})
    @ResponseBody
    public ResponseEntity<BaseResult> runtimeException(RuntimeException ex){
        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw,true));
        LOG.error("ExceptionReport|"+ex.getClass().getName()+sw.toString(), this.getClass());
        return buildFailedInfo("服务器发生异常："+ex.getMessage());
    }

	
	//400错误->缺少参数异常
    @ExceptionHandler({MissingServletRequestParameterException.class})
    @ResponseBody
    public ResponseEntity<BaseResult> requestMissingServletRequest(MissingServletRequestParameterException ex){
        return buildFailedInfo(ApiConstance.PARAM_IS_NULL,"："+ex.getParameterName());
    }
    
    
    
    //400错误->参数类型异常
    @ExceptionHandler({TypeMismatchException.class})
    @ResponseBody
    public ResponseEntity<BaseResult> requestTypeMismatch(TypeMismatchException ex){
    	return buildFailedInfo(ApiConstance.PARAM_TYPE_ERROR," "+ex.getValue()+"："+ex.getRequiredType().getName());
    }


    //400错误->json参数格式有误
    @ExceptionHandler({JsonParseException.class})
    @ResponseBody
    public ResponseEntity<BaseResult> jsonParamError(JsonParseException ex){
        return buildFailedInfo(ApiConstance.PARAM_JSON_ERROR);
    }

    //400错误->参数格式有误
    @ExceptionHandler({InvalidFormatException.class})
    @ResponseBody
    public ResponseEntity<BaseResult> invalidFormatException(InvalidFormatException ex){
        return buildFailedInfo(ApiConstance.PARAM_FORMAT_ERROR);
    }
    
    
}
