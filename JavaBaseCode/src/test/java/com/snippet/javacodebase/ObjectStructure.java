package com.snippet.javacodebase;

import lombok.Data;
import org.junit.jupiter.api.Test;
import org.openjdk.jol.info.ClassLayout;

/**
 * 输出对象的内存分配：对象头、对象实例数据、对齐填充
 * create by whr on 2023/3/4
 */
public class ObjectStructure {

    @Data
    static class OrderModel {
        private long orderId;

        public String orderInfo;

        private UserModel userInfo;
    }

    @Data
    static class UserModel {
        private Long userId;

        private String username;

    }

    @Test
    public void empty_order_model_struct() {
        OrderModel orderModel = new OrderModel();
        ClassLayout classLayout1 = ClassLayout.parseInstance(orderModel);
        String emptyOrder = classLayout1.toPrintable();
        System.out.println(emptyOrder);
    }

    @Test
    public void order_model_struct_2() {
        OrderModel order = new OrderModel();
        order.setOrderId(1234562314544L);
        order.setUserInfo(new UserModel());
        ClassLayout classLayout2 = ClassLayout.parseInstance(order);
        String orderMemoryInfo = classLayout2.toPrintable();
        System.out.println(orderMemoryInfo);
    }

    @Test
    public void full_order_model_struct() {
        UserModel userModel = new UserModel();
        userModel.setUserId(7894564123L);
        userModel.setUsername("God");

        OrderModel orderWithUser = new OrderModel();
        orderWithUser.setOrderId(123456456789L);
        orderWithUser.setOrderInfo("info");
        orderWithUser.setUserInfo(userModel);
        ClassLayout classLayout3 = ClassLayout.parseInstance(orderWithUser);
        String orderUserMemoryInfo = classLayout3.toPrintable();
        System.out.println(orderUserMemoryInfo);
    }
}
