package com.snippet.designpattern.singleton;

/**
 * 懒加载，第一次调用初始化内部类，才会初始化单例对象，依赖于JVM的类加载的线程安全
 * create by whr on 2023/3/4
 */
public class InnerClassSingleton {

    private InnerClassSingleton() {
    }

    // 内部类来实例化 单例
    private static class SingletonHolder {
        private static final InnerClassSingleton INSTANCE = new InnerClassSingleton();
    }

    // 获取内部类实例化的 单例对象
    public static InnerClassSingleton getUniqueInstance() {
        return SingletonHolder.INSTANCE;
    }
}
