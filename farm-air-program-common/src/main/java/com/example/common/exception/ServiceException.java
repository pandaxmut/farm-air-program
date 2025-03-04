package com.example.common.exception;

import com.example.common.exception.enums.ResponseEnum;

public final class ServiceException extends RuntimeException{

    private Integer code;
    //业务异常
    public ServiceException(ResponseEnum responseEnum){
        super(responseEnum.getMessage());
        this.code= responseEnum.getCode();
    }
    //临时业务异常
    public ServiceException(Integer code,String message){
        super(message);
        this.code=code;
    }

    public Integer getCode() {
        return code;
    }
}
