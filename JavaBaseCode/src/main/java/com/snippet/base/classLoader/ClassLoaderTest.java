package com.snippet.base.classLoader;

/**
 * create by whr on 2023/2/12
 */
public class ClassLoaderTest {
    public static void main(String[] args) throws ClassNotFoundException {
        //Class<?> name = Class.forName("org.snippet.classLoader.ClassLoaderTest$Father");
        Class<?> aClass = Father.class.getClassLoader().loadClass("org.snippet.classLoader.ClassLoaderTest$Father");
    }

    static class Father {
        public static int num = 1;
        public String name = "Father";

        static {
            System.out.println("Father Static Field: " + num);
            System.out.println("Father static init block");
        }

        {
            System.out.println("Father field: " + name);
            System.out.println("Father init block");
        }

        public Father() {
            System.out.println("Father constructor");
        }
    }

    static class Son extends Father {
        public static int num = 1;
        public String name = "Son";

        static {
            System.out.println("Son Static Field: " + num);
            System.out.println("Son static init block");
        }

        {
            System.out.println("Son field: " + name);
            System.out.println("Son init block");
        }

        public Son() {
            System.out.println("Son constructor");
        }
    }
}
