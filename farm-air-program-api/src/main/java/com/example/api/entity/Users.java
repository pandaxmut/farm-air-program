package com.example.api.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户信息表
 * </p>
 *
 * @author linyunhong
 * @since 2024-11-20
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Users implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @TableId("user_id")
    private String userId;

    /**
     * 飞书id
     */
    private String feishuId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 加密密码
     */
    private String password;

    /**
     * 0:普通用户，1:教授 ，2:农民
     */
    private Integer role;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机电话号码
     */
    private String phone;

    /**
     * 性别：0:女，1:男
     */
    private Integer sex;

    /**
     * 头像url
     */
    private String avatarUrl;

    /**
     * 个人描述
     */
    private String description;

    /**
     * 用户状态
     */
    private Integer status;

    /**
     * 历史积分
     */
    private Integer totalIntegral;

    /**
     * 当前积分
     */
    private Integer currentIntegral;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
