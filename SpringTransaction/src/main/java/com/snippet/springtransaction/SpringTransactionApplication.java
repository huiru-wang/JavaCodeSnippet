package com.snippet.springtransaction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@SpringBootApplication
public class SpringTransactionApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringTransactionApplication.class, args);
    }

}
