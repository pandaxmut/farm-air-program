package com.example.users.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.users.entity.Users;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户信息表 Mapper 接口
 * </p>
 *
 * @author linyunhong
 * @since 2024-11-20
 */
@Mapper
public interface UsersMapper extends BaseMapper<Users> {

}
