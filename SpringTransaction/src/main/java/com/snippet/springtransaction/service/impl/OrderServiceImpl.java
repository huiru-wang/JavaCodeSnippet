package com.snippet.springtransaction.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snippet.springtransaction.dao.entity.Order;
import com.snippet.springtransaction.dao.mapper.OrderMapper;
import com.snippet.springtransaction.service.OrderService;
import com.snippet.springtransaction.utils.OrderUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.concurrent.TimeUnit;

/**
 * 1、声明事务
 * 2、编程事务
 * 3、超时事务
 * 4、事务传播
 * 5、transactionTemplate
 *
 * @author haiah
 * @since 2023-03-03 07:42:30
 */
@Slf4j
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    DataSourceTransactionManager transactionManager;

    @Autowired
    TransactionTemplate transactionTemplate;

    /**
     * 声明式事务
     * 默认使用transactionManager，其他名称需要指定
     */
    @Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
    public void annotationTransaction() {
        Order order = OrderUtils.getOrder();
        orderMapper.insert(order);
        int s = 1 / 0;
    }

    /**
     * 超时事务：只有执行SQL时才判断是否超时，执行完成后再超时，事务不会回滚
     * 不会回滚
     */
    @Transactional(timeout = 3, rollbackFor = Exception.class)
    public void timeoutTransaction1() {
        Order order = OrderUtils.getOrder();
        orderMapper.insert(order);
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (Exception e) {
            log.error("time out");
        }
    }

    /**
     * 超时回滚
     */
    @Transactional(timeout = 3, rollbackFor = Exception.class)
    public void timeoutTransaction2() {
        Order order = OrderUtils.getOrder();
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            log.error("time out");
        }
        orderMapper.insert(order);
    }

    /**
     * 编程式事务
     */
    public void codeTransaction() {
        // 编程式事务 定义属性，同声明式的注解参数
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        definition.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        definition.setTimeout(5); // s
        definition.setName("code transaction");
        definition.setReadOnly(true);

        TransactionStatus transactionStatus = transactionManager.getTransaction(definition);
        try {
            Order order = OrderUtils.getOrder();
            orderMapper.insert(order);
            int s = 1 / 0;
            transactionManager.commit(transactionStatus);
        } catch (Exception e) {
            transactionManager.rollback(transactionStatus);
        }
    }

    public void templateTest() {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    Order order = OrderUtils.getOrder();
                    orderMapper.insert(order);
                    //int s = 1 / 0;
                } catch (Exception e) {
                    status.setRollbackOnly();
                    e.printStackTrace();
                }
            }
        });
    }
}
