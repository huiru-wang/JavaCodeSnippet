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
@TableName("t_orderDetail")
@ApiModel(value = "OrderDetail对象", description = "")
public class OrderDetail {

    @ApiModelProperty("订单单品")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("订单Id")
    @TableField("orderId")
    private Long orderId;

    @ApiModelProperty("商品Id")
    @TableField("productId")
    private Long productId;

    @ApiModelProperty("商品数量")
    @TableField("quantity")
    private Long quantity;

    @ApiModelProperty("商品实际总付款")
    @TableField("payment")
    private Long payment;

    @ApiModelProperty("单品备注")
    @TableField("remarks")
    private String remarks;


}
