package com.example.common.utils;

public class UserContext {
    private static final ThreadLocal<String> tl = new ThreadLocal<>();
    //保存当前用户到threadlocal
    public static void setUserId(String id){
        tl.set(id);
    }
    public static String getUserId(){
        return tl.get();
    }
    public static void remove(){
        tl.remove();
    }
}
