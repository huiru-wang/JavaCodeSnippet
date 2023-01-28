CREATE TABLE `t_user`
(
    `id`         varchar(255) NOT NULL,
    `userName`   varchar(32)  NOT NULL,
    `passWord`   varchar(64)  NOT NULL,
    `phone`      varchar(32) DEFAULT NULL,
    `updateTime` datetime     NOT NULL ON UPDATE CURRENT_TIMESTAMP,
    `createTime` datetime     NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `t_orderDetail`
(
    `id`        bigint NOT NULL COMMENT '订单单品',
    `orderId`   bigint NOT NULL COMMENT '订单Id',
    `productId` bigint       DEFAULT NULL COMMENT '商品Id',
    `quantity`  bigint       DEFAULT NULL COMMENT '商品数量',
    `payment`   bigint       DEFAULT NULL COMMENT '商品实际总付款',
    `remarks`   varchar(255) DEFAULT NULL COMMENT '单品备注',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `t_order`
(
    `id`         bigint      NOT NULL COMMENT '订单Id',
    `userId`     bigint      NOT NULL COMMENT '用户Id',
    `amount`     bigint      NOT NULL COMMENT '订单总价',
    `type`       tinyint      DEFAULT NULL COMMENT '订单类型',
    `channel`    varchar(32) NOT NULL COMMENT '交易渠道',
    `status`     tinyint     NOT NULL COMMENT '交易状态：0-成功；-1：失败；1：已关闭；2：处理中',
    `remarks`    varchar(255) DEFAULT NULL COMMENT '订单备注',
    `extInfo`    varchar(255) DEFAULT NULL COMMENT '扩展字段',
    `createTime` datetime     DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_german2_ci;

CREATE TABLE `t_product`
(
    `id`          bigint NOT NULL COMMENT '商品Id',
    `productName` varchar(128) DEFAULT NULL COMMENT '商品名称',
    `price`       bigint       DEFAULT NULL COMMENT '商品单价',
    `productInfo` varchar(255) DEFAULT NULL COMMENT '商品描述',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;