package org.snippet.dynamicproxy.impl;

import org.snippet.dynamicproxy.TargetService;

public class Target implements TargetService {

    @Override
    public final void targetMethod(){
        System.out.println("invoke target method");
    }

}
