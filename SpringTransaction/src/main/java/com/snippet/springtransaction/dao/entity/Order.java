package com.snippet.springtransaction.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author haiah
 * @since 2023-03-03 07:42:30
 */
@Getter
@Setter
@TableName("t_order")
public class Order {


    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;


    @TableField("userId")
    private Long userId;


    @TableField("amount")
    private Long amount;


    @TableField("type")
    private Integer type;


    @TableField("channel")
    private String channel;


    @TableField("status")
    private Integer status;


    @TableField("remarks")
    private String remarks;


    @TableField("extInfo")
    private String extInfo;


    @TableField("createTime")
    private LocalDateTime createTime;


    @TableField("updateTime")
    private LocalDateTime updateTime;


}
