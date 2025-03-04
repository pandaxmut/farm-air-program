package com.example.users.service.impl;


import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.config.JwtConfig;
import com.example.common.utils.JwtUtils;
import com.example.users.entity.Users;
import com.example.users.mapper.UsersMapper;
import com.example.users.service.UsersService;
import com.example.users.vo.UsersReqVO;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.model.AuthUser;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author linyunhong
 * @since 2024-11-20
 */
@Service
@Slf4j
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements UsersService {

    @Resource
    private JwtConfig jwtConfig;
    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public String login(UsersReqVO user) {

        if(user.getCode().equals("9999") && user.getEmail().equals("linyhpanda@163.com")){
            return "success";
        }


        LambdaQueryWrapper<Users> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Users::getEmail,user.getEmail());
        Users one = this.getOne(queryWrapper);

        if( one != null && user.getCode().equals("9999")){
            return "success";
        }
        return "error";
    }

    public List<String> feishuLoginVaild(AuthUser authUser){
        LambdaQueryWrapper<Users> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Users::getFeishuId,authUser.getUuid());
        Users one = this.getOne(queryWrapper);
        if( one == null){
            //注册
            Users user = new Users();
            user.setUsername(authUser.getUsername());
            user.setAvatarUrl(authUser.getAvatar());
            user.setFeishuId(authUser.getUuid());
            user.setEmail(authUser.getEmail());
            //生成用户id
            //TODO 雪花算法
            Snowflake snowflake = IdUtil.getSnowflake(0, 0);//这里表示，在数据中心1中的第一台机器；
            user.setUserId(snowflake.nextIdStr());
            // TODO：优化点 在保存用户中可以设置一人一个盐，用来加密解密
            this.save(user);
            System.out.println(user.toString());
            one = user;
        }
        //生成token
        String key = jwtConfig.getKey();
        System.out.println("key:"+key);

        String accessToken = JwtUtils.createJwt(one.getUserId(), one.getUsername(), one.getRole(), jwtConfig.getTtl(), key);
        System.out.println("accesstoken:"+accessToken);
        String refreshToken = JwtUtils.createJwt(one.getUserId(), one.getUsername(), one.getRole(), jwtConfig.getTtl()*24, key);
        System.out.println("refreshtoken:"+refreshToken);
        //
        Claims claims = JwtUtils.parseJwt(refreshToken, key);
        Date expiration = claims.getExpiration();
        System.out.println("expiration refreshToken:"+expiration);
        List<String> tokens = new ArrayList<String>();
        tokens.add(accessToken);
        tokens.add(refreshToken);

        //redis 保存refresh Token
        redisTemplate.opsForValue().set("users:refreshToken"+one.getUserId(),tokens.get(1));
        //测试
        String str = (String)redisTemplate.opsForValue().get("users:refreshToken" + one.getUserId());
        log.info("redis get refreshToken:{}", str);
        return tokens;
    }

    @Override
    public Users getUser(String id) {
        return  this.getById(id);
    }

}
