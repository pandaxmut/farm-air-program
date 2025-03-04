package com.example.users.api;

import com.alibaba.fastjson.JSONObject;
import com.example.common.utils.CommonResult;
import com.example.users.service.UsersService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthFeishuRequest;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/oauth")
@Slf4j
public class RestAuthController {

    @Resource
    private UsersService usersService;

//    @RequestMapping("/render")
//    public void renderAuth(HttpServletResponse response) throws IOException {
//        AuthRequest authRequest = getAuthRequest();
//        response.sendRedirect(authRequest.authorize(AuthStateUtils.createState()));
//    }
    @RequestMapping("/render")
    public CommonResult<String> renderAuth(HttpServletResponse response) throws IOException {
        AuthRequest authRequest = getAuthRequest();
        String url = authRequest.authorize(AuthStateUtils.createState());
        System.out.println(url);
        return CommonResult.success(url);
    }

    @RequestMapping("/callback/feishu")
    public Object login(@RequestParam( name = "error",required = false) String error,
                        AuthCallback callback,
                        HttpServletResponse response) throws IOException {
        log.info("进入callback：" + "feishu" + " callback params：" + JSONObject.toJSONString(callback));

        AuthRequest authRequest = getAuthRequest();
        AuthResponse<AuthUser> res = authRequest.login(callback);
        //TODO 找不到用户拒绝授权的返回路径
        //

        log.info("response :\n{}",JSONObject.toJSONString(res));

        if (res.ok()) {
            //TODO 为完善 保存响应数据 （飞书登录的用户信息）
            log.info("当前登录的飞书用户{}，欢迎您", res.getData());
            //TODO 暂时不使用飞书的token，我们自己生成一个双token；
            List<String> token = usersService.feishuLoginVaild(res.getData());

            log.info("RestAuthController token:"+token);
            //重定向到前端的登录页
            response.sendRedirect("http://localhost:3004/callback/feishu?accessToken="+token.get(0)+"&refreshToken="+token.get(1));
        }

        Map<String, Object> map = new HashMap<>(1);
        map.put("errorMsg", res.getMsg());
        return CommonResult.error(res.getCode(),res.getMsg());
    }



    private AuthRequest getAuthRequest() {
        return new AuthFeishuRequest(AuthConfig.builder()
                .clientId("cli_a78ca886a531d00e")
                .clientSecret("sXv0Bfp0CI94XnsFziMGPbHquBdgTgm7")
                .redirectUri("http://localhost:18080/users/oauth/callback/feishu")
                .build());
    }
}
