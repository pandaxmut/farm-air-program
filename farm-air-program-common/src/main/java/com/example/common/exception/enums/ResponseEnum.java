package com.example.common.exception.enums;

public enum ResponseEnum {
    //ok
    SUCCESS(200,"success"),
    REFRESH_TOKEN(210,"refresh token"),
    //服务端错误
    FAIL(500,"fail"),
    ERROR(400,"error"),
    LOGIN_FAIL(401,"login fail"),
    //
    UNAUTHORIZED(401,"unauthorized"),
    PASSWORDERROR(40002,"password error"),
    //token过期
    TOKENEXPIRED(402,"token过期"),
    TOKENERROR(403,"refresh token错误"),
    NOT_FOUND(404,"not found"),
    METHOD_NOT_ALLOWED(405,"method not allowed"),

    //业务异常
    MULTIPLE(50001,"重复插入" );

    private final Integer code;
    private final String message;

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    ResponseEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
