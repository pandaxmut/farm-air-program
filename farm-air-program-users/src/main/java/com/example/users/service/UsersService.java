package com.example.users.service;

import com.example.users.entity.Users;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.users.vo.UsersReqVO;
import me.zhyd.oauth.model.AuthUser;

import java.util.List;

/**
 * <p>
 * 用户信息表 服务类
 * </p>
 *
 * @author linyunhong
 * @since 2024-11-20
 */
public interface UsersService extends IService<Users> {
    String login(UsersReqVO user);

    List<String> feishuLoginVaild(AuthUser authUser);

    Users getUser(String id);
}
