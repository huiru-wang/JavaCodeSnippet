package org.snippet.dynamicproxy;

import org.snippet.dynamicproxy.impl.Target;

import java.lang.reflect.Proxy;

public class App {

    public static void main(String[] args) {
        Target target = new Target();
        Class<? extends Target> targetClass = target.getClass();
        JDKProxy proxy = new JDKProxy(target);

        // 需要强转为接口
        TargetService targetProxy = (TargetService)Proxy.newProxyInstance(targetClass.getClassLoader(), targetClass.getInterfaces(), proxy);

        targetProxy.targetMethod();
    }
}
