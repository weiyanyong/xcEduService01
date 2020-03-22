package com.xuecheng.framework.exception;

import com.google.common.collect.ImmutableMap;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

//统一异常捕获类
@ControllerAdvice//高级控制器
public class ExceptionCatch {
    //有异常就要记日志
    private  static final Logger LOGGER = LoggerFactory.getLogger(ExceptionCatch.class);
//对象常量映射，来保存一些常量映射的键值对。
// 1.ImmutableMap.Builder
// 2.ImmutableMap.Builder.put(X,Y)
// 3.ImmutableMap.Builder.put(X,Y).build

    private  static ImmutableMap<Class<? extends Throwable>,ResultCode> EXCEPTIONS;
    protected static ImmutableMap.Builder<Class<? extends Throwable>,ResultCode> builder = ImmutableMap.builder();

    //捕获CustomException类异常
    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public ResponseResult customException(CustomException customException){
        LOGGER.error("catch exception:{ }",customException.getMessage());
        ResultCode resultCode = customException.getResultCode();
        return  new ResponseResult(resultCode);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseResult Exception(Exception exception){
        LOGGER.error("catch exception:{ }",exception.getMessage());
        if(EXCEPTIONS == null){
            EXCEPTIONS =  builder.build();
        }
        ResultCode resultCode =  EXCEPTIONS.get(exception.getClass());
        if(resultCode != null ){
            return new ResponseResult(resultCode);
        }
        return new ResponseResult(CommonCode.SERVER_ERROR);
    }


    static{
        builder.put(HttpMessageNotReadableException.class,CommonCode.INVALID_PARAM);

    }
}
