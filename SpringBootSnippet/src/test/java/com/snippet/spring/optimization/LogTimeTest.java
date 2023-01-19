package com.snippet.spring.optimization;

import com.snippet.spring.optimization.model.OrderModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

@Slf4j
@SpringBootTest
public class LogTimeTest {

    StopWatch stopWatch = new StopWatch();

    public void serialize_object_Log() {
        stopWatch.start("object serialize");
        OrderModel orderModel = new OrderModel("1", "2", "1", "2", "1", "2", "1", "2", "1", "2");
        logObject(orderModel);
        stopWatch.stop();

        stopWatch.start("field serialize");
        logField(orderModel);
        stopWatch.stop();

        stopWatch.setKeepTaskList(true);
        System.out.println(stopWatch.prettyPrint());
    }

    private void logObject(OrderModel order) {
        for (int i = 0; i < 1000000; i++) {
            log.info("order:{}", order);
        }
    }

    private void logField(OrderModel order) {
        for (int i = 0; i < 1000000; i++) {
            log.info("order:{},{},{},{},{},{},{},{},{},{}}", order.getA0(), order.getA1(), order.getA2(), order.getA3(), order.getA4(), order.getA5(), order.getA6(), order.getA7(), order.getA8(), order.getA9());
        }
    }
}
