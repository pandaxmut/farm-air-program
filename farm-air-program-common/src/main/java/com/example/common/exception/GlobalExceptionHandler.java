package com.example.common.exception;

import com.example.common.exception.enums.ResponseEnum;
import com.example.common.utils.CommonResult;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *全局异常处理器
 * @作用：当业务中出现异常，并且直到controller层还没抛出时，它会为我们做一个兜底，并且返回给前端一个统一的格式.
 *
 * @使用：微服务中，别的服务想要使用他，就得在他的applicaiton中加上@ComponentScan(basePackages={"com.example.users","com.example.common"})
 */
@Slf4j
@ControllerAdvice(basePackages = "com.example") //或者列表的形式加载每个服务包
public class GlobalExceptionHandler {
    @ResponseBody
    @ExceptionHandler(Throwable.class)
    public CommonResult ServiceExceptionHandler(HttpServletRequest request, Throwable ex){
        log.info("[ThrowableHandler]",ex);
        System.out.println("ThrowableHandler:"+ex);
        return CommonResult.error(400,ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public CommonResult ServiceExceptionHandler(HttpServletRequest request, Exception ex){
        log.info("[ExceptionHandler]",ex);
        System.out.println("ExceptionHandler:"+ex.getMessage());
        return CommonResult.error(400,ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(ServiceException.class)
    public CommonResult ServiceExceptionHandler(HttpServletRequest request, ServiceException ex){
        log.debug("[ServiceExceptionHandler]",ex);
        System.out.println("ServiceExceptionHandler:"+ex);
        return CommonResult.error(ex.getCode(),ex.getMessage());
    }
}
