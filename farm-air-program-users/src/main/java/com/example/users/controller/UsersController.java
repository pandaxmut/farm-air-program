package com.example.users.controller;

import com.example.api.controller.ArticlesApiController;
import com.example.api.entity.Articles;
import com.example.common.config.JwtConfig;
import com.example.common.exception.enums.ResponseEnum;
import com.example.common.utils.CommonResult;
import com.example.common.utils.JwtUtils;
import com.example.common.utils.UserContext;
import com.example.users.entity.Users;
import com.example.users.message.Provider;
import com.example.users.service.UsersService;
import com.example.users.vo.UsersReqVO;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.catalina.User;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户信息表 前端控制器
 * </p>
 *
 * @author linyunhong
 * @since 2024-11-20
 */
@RestController
@RequestMapping("/users")
public class UsersController {
    @Resource
    private Provider provider;

    @Resource
    private UsersService usersService;

    @Resource
    private JwtConfig jwtConfig;

    @Resource
    private ArticlesApiController articlesApiController;


    //登录
    @PostMapping("login")
    public CommonResult<String> login(@RequestBody UsersReqVO user){
         usersService.login(user);
        return CommonResult.success();
    }

    @RequestMapping("sendEmailCode")
    public String hello(){
        try {
            //TODO:这里需要把code设置生存期并且保存到redis中。
            provider.send();

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return "200";
    }

    //获取当前用户信息
    @GetMapping()
    public CommonResult getUserInfo(){
        //
        String userId = UserContext.getUserId();
        if (userId == null || "".equals(userId)){
            return CommonResult.error(ResponseEnum.TOKENEXPIRED);
        }
        Users user = this.usersService.getUser(userId);
        return CommonResult.success(user);
    }

    //更新当前用户信息
    @PutMapping()
    public CommonResult updateUserInfo(@RequestBody Users user){
        //
        String userId = UserContext.getUserId();
        if (userId == null || "".equals(userId)){
            return CommonResult.error(ResponseEnum.TOKENEXPIRED);
        }
        user.setUserId(userId);
        usersService.updateById(user);
        return CommonResult.success();
    }

    //更新用户密码
    @PutMapping("password")
    public CommonResult updatePassword(@RequestBody Map<String, Object> requestBody){
        // 从 Map 中提取请求参数
        String password = String.valueOf(requestBody.get("password"));
        String newPassword = String.valueOf(requestBody.get("newPassword"));
        String userId = UserContext.getUserId();
        if (userId == null || "".equals(userId)){
            return CommonResult.error(ResponseEnum.TOKENEXPIRED);
        }
        Users user = usersService.getById(userId);
        if (user.getPassword() != null  &&  !password.equals(user.getPassword())){
            return CommonResult.error(ResponseEnum.PASSWORDERROR);
        }
        user.setPassword(newPassword);
        usersService.updateById(user);
        return CommonResult.success("ok");
    }

    @PutMapping("role")
    public CommonResult updateRole(@RequestBody Map<String, Object> requestBody){
        // 从 Map 中提取请求参数
        String password = String.valueOf(requestBody.get("password"));
        Integer role = Integer.valueOf( (String) requestBody.get("role"));
        String userId = UserContext.getUserId();
        if (userId == null || "".equals(userId)){
            return CommonResult.error(ResponseEnum.TOKENEXPIRED);
        }
        Users user = usersService.getById(userId);
        if ( !password.equals(user.getPassword())){
            return CommonResult.error(ResponseEnum.PASSWORDERROR);
        }
        user.setRole(role);
        usersService.updateById(user);
        return CommonResult.success("ok");
    }


    //使用refreshToken请求accessToken,
    //TODO 如何需要校验用户是否登录、强制下线用户的话，就需要将token保存到redis中。
    @PostMapping("refreshToken")
    public CommonResult<String> refreshToken(HttpServletRequest request){
        //全局拦截器已经将header中的authorization用户信息提取了
        String authorization = request.getHeader("authorization");
        if(authorization == null || "".equals(authorization)){
            return CommonResult.error(ResponseEnum.TOKENERROR);
        }
        String refreshToken = authorization.replace("Bearer ","");
        //refreshtoken过期、错误
        if (!JwtUtils.validateToken(refreshToken, jwtConfig.getKey())){
            return CommonResult.error(ResponseEnum.TOKENEXPIRED);
        }
        Claims claims = JwtUtils.parseJwt(refreshToken, jwtConfig.getKey());
        String userId = (String)claims.get("userId");
        //生成accessToken
        String accessToken = JwtUtils.createJwt(userId, claims, jwtConfig.getTtl(), jwtConfig.getKey());
        return CommonResult.success(accessToken);
    }
    @GetMapping("getUserArticles")
    public CommonResult<List<Articles>> getUserArticles(){
        System.out.println("测试，进入GetUserArticles，即将远程调用feign articles, 判断是否有处理请求头");
        return articlesApiController.getUserArticles();
    }

}
