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
@TableName("mall_products")
public class MallProducts implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private String description;

    private Integer price;

    private Integer stock;

    private String category;

    private String covor;

    private String status;

    private LocalDateTime postTime;

    private LocalDateTime updateTime;
}
