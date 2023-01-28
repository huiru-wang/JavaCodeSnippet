package com.snippet.spring.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author will
 * @since 2023-01-28 07:41:01
 */
@Getter
@Setter
@TableName("t_product")
@ApiModel(value = "Product对象", description = "")
public class Product {

    @ApiModelProperty("商品Id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("商品名称")
    @TableField("productName")
    private String productName;

    @ApiModelProperty("商品单价")
    @TableField("price")
    private Long price;

    @ApiModelProperty("商品描述")
    @TableField("productInfo")
    private String productInfo;


}
