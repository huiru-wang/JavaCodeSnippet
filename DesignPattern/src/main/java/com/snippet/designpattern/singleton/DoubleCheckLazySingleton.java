package com.snippet.designpattern.singleton;

/**
 * 双重检查
 * 1、懒加载
 * 2、后续线程不需要再加锁获取对象
 * 3、二次检查必须
 * create by whr on 2023/3/4
 */
public class DoubleCheckLazySingleton {
    private DoubleCheckLazySingleton() {
    }

    private static DoubleCheckLazySingleton instance;

    public static DoubleCheckLazySingleton getInstance() {
        if (instance == null) {
            // 如果开始有多个线程进入这里等待，需要第二次检查防止创建多次
            synchronized (DoubleCheckLazySingleton.class) {
                if (instance == null) {
                    instance = new DoubleCheckLazySingleton();
                }
            }
        }
        return instance;
    }
}
