package org.snippet.dynamicproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class JDKProxy implements InvocationHandler {

    private final Object target;

    public JDKProxy(Object target){
        this.target =target;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("jdk proxy enhance method");
        return method.invoke(target, args);
    }
}
