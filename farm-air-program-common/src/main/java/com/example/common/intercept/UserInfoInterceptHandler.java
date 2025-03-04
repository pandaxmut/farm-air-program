package com.example.common.intercept;


import cn.hutool.core.util.StrUtil;
import com.example.common.exception.ServiceException;
import com.example.common.exception.enums.ResponseEnum;
import com.example.common.utils.UserContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Enumeration;

public class UserInfoInterceptHandler implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取请求头中的userinfo，把他保存到threadlocal中

        String userinfo = request.getHeader("userinfo");
        System.out.println("进入UserInfoInterceptHandler:userinfo="+userinfo);
        if (StrUtil.isNotBlank(userinfo)){
            UserContext.setUserId(userinfo);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
       UserContext.remove();
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
