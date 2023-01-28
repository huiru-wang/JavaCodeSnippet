package com.snippet.spring.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

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
@TableName("t_order")
@ApiModel(value = "Order对象", description = "")
public class Order {

    @ApiModelProperty("订单Id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("用户Id")
    @TableField("userId")
    private Long userId;

    @ApiModelProperty("订单总价")
    @TableField("amount")
    private Long amount;

    @ApiModelProperty("订单类型")
    @TableField("type")
    private Integer type;

    @ApiModelProperty("交易渠道")
    @TableField("channel")
    private String channel;

    @ApiModelProperty("交易状态：0-成功；-1：失败；1：已关闭；2：处理中")
    @TableField("status")
    private Integer status;

    @ApiModelProperty("订单备注")
    @TableField("remarks")
    private String remarks;

    @ApiModelProperty("扩展字段")
    @TableField("extInfo")
    private String extInfo;

    @ApiModelProperty("创建时间")
    @TableField("createTime")
    private LocalDateTime createTime;


}
