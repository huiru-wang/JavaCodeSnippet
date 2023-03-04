package com.snippet.designpattern.singleton;

/**
 * 1、懒加载
 * 2、锁粒度大，每次进来都要加锁
 * <p>
 * create by whr on 2023/3/4
 */
public class LazySingleton {

    private LazySingleton() {
    }

    private static LazySingleton instance;

    // 加锁
    public static synchronized LazySingleton getInstance() {
        if (instance == null) {
            instance = new LazySingleton();
        }
        return instance;
    }
}
