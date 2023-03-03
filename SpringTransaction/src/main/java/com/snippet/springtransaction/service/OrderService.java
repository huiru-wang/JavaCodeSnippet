package com.snippet.springtransaction.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.snippet.springtransaction.dao.entity.Order;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author haiah
 * @since 2023-03-03 07:42:30
 */
public interface OrderService extends IService<Order> {

    void annotationTransaction();

    void timeoutTransaction1();

    void timeoutTransaction2();

    void codeTransaction();

    void templateTest();
}
