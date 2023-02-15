package com.snippet.jdkproxy;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * create by whr on 2023/2/15
 */
public class JDKProxyTest {

    @Test
    public void jdk_proxy_test() {
        Target target = new Target();
        Class<? extends Target> targetClass = target.getClass();
        CustomProxy proxy = new CustomProxy(target);

        // 需要强转为接口
        TargetService targetProxy =
                (TargetService) Proxy.newProxyInstance(targetClass.getClassLoader(), targetClass.getInterfaces(), proxy);

        targetProxy.targetMethod();
    }

    class Target implements TargetService {

        @Override
        public final void targetMethod() {
            System.out.println("invoke target method");
        }

    }

    interface TargetService {

        void targetMethod();
    }

    class CustomProxy implements InvocationHandler {

        private final Object target;

        public CustomProxy(Object target) {
            this.target = target;
        }

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            System.out.println("jdk proxy enhance method");
            return method.invoke(target, args);
        }
    }
}
