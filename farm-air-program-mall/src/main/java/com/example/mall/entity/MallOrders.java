package com.example.mall.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author linyunhong
 * @since 2025-03-10
 */
@Getter
@Setter
@TableName("mall_orders")
public class MallOrders implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String userId;

    private String username;

    private String phone;

    private String address;

    private Long productId;

    private String productName;

    private Integer stock;

    /**
     * 1:成功，2：失败
     */
    private Integer status;

    private Integer price;

    private LocalDateTime postTime;

    private LocalDateTime updateTime;
}
