package com.snippet.javacodebase;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 对比反射性能
 * 1、获取class方法
 * 2、代码检查带来的耗时
 * 3、获取class、method、field对象带来的耗时
 * <p>
 * create by whr on 2023/3/4
 */
public class ReflectTest {

    static class Tool {
        public void doSomething() {
            // ...
        }
    }

    // ======================== 对比三种获取class速度 ========================
    // 1,2差别不大，3最慢
    @Test
    public void get_class_test_1() {
        long start = System.nanoTime();
        for (int i = 0; i < 100_000; i++) {
            Class<Tool> toolClass = Tool.class;
        }
        System.out.println(System.nanoTime() - start); // 11_931_100
    }

    @Test
    public void get_class_test_2() {
        long start = System.nanoTime();
        for (int i = 0; i < 100_000; i++) {
            Class<? extends Tool> toolClass = new Tool().getClass();
        }
        System.out.println(System.nanoTime() - start); // 9_278_800
    }

    @Test
    public void get_class_test_3() throws ClassNotFoundException {
        long start = System.nanoTime();
        for (int i = 0; i < 100_000; i++) {
            Class<?> toolClass = Class.forName("com.snippet.javacodebase.ReflectTest$Tool");
        }
        System.out.println(System.nanoTime() - start); // 105_756_500
    }

    // ======================== 禁止代码检查，性能提升 ========================
    // 无论是不是私有，禁用代码检查，性能更好，10倍
    @Test
    public void accessible_test_1() throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        long start = System.nanoTime();
        Class<Tool> toolClass = Tool.class;
        Tool tool = toolClass.newInstance();
        Method method = toolClass.getDeclaredMethod("doSomething");
        for (int i = 0; i < 100_000; i++) {
            method.invoke(tool);
        }
        System.out.println(System.nanoTime() - start); // 26_924_600
    }

    @Test
    public void accessible_test_2() throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        long start = System.nanoTime();
        Class<Tool> toolClass = Tool.class;
        Tool tool = toolClass.newInstance();
        Method method = toolClass.getDeclaredMethod("doSomething");
        method.setAccessible(true);
        for (int i = 0; i < 100_000; i++) {
            method.invoke(tool);
        }
        System.out.println(System.nanoTime() - start); // 10_029_400
    }

    // ======================== 反射的耗时 主要再获取class对象、method对象上 ========================
    // 根据场景，可以适当缓存class对象
    @Test
    public void reflect_perf_test_1() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        long start = System.nanoTime();
        for (int i = 0; i < 100_000; i++) {
            Class<Tool> toolClass = Tool.class;
            Tool tool = toolClass.newInstance();
            Method method = toolClass.getDeclaredMethod("doSomething");
            method.setAccessible(true);
            method.invoke(tool);
        }
        System.out.println(System.nanoTime() - start); // 80_679_500
    }

    @Test
    public void reflect_perf_test_2() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        long start = System.nanoTime();
        Class<Tool> toolClass = Tool.class;
        Tool tool = toolClass.newInstance();
        for (int i = 0; i < 100_000; i++) {
            Method method = toolClass.getDeclaredMethod("doSomething");
            method.setAccessible(true);
            method.invoke(tool);
        }
        System.out.println(System.nanoTime() - start); // 66_171_600
    }

    @Test
    public void reflect_perf_test_3() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        long start = System.nanoTime();
        Class<Tool> toolClass = Tool.class;
        Tool tool = toolClass.newInstance();
        Method method = toolClass.getDeclaredMethod("doSomething");
        method.setAccessible(true);
        for (int i = 0; i < 100_000; i++) {
            method.invoke(tool);
        }
        System.out.println(System.nanoTime() - start); // 7_252_600
    }
}
