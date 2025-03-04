package com.example.common.utils;

import com.example.common.exception.enums.ResponseEnum;
import lombok.Data;

/**
 * 参数：success,code,message,data
 * @param <T>
 */
@Data
public  class CommonResult<T> {
    private static final long serialVersionUID = 1L;


    private Boolean success;
    private Integer code;
    private String message;
    private T data;


    //成功 业务成功
    public static <T> CommonResult<T> success(T data, ResponseEnum responseEnum){
        CommonResult<T> result = new CommonResult<>();
        result.success = true;
        result.code = responseEnum.getCode();
        result.message = responseEnum.getMessage();
        result.data = data;
        return result;
    }

    //成功
    public static <T> CommonResult<T> success(T data){
        return success(data, ResponseEnum.SUCCESS);
    }
    //不返回data
    public static <T> CommonResult<T> success(){
        return success(null, ResponseEnum.SUCCESS);
    }

    //错误
    public static <T> CommonResult<T> error(ResponseEnum responseEnum){
        CommonResult<T> result = new CommonResult<>();
        result.success = false;
        result.code = responseEnum.getCode();
        result.message = responseEnum.getMessage();
        return result;
    }

    public static <T> CommonResult<T> error(Integer code, String message){
        CommonResult<T> result = new CommonResult<>();
        result.success = false;
        result.code = code;
        result.message = message;
        return result;
    }




}