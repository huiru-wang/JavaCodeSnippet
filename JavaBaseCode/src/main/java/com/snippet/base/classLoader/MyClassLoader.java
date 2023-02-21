package com.snippet.base.classLoader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class MyClassLoader extends ClassLoader {

    private String directory;

    // 默认 AppClassLoader
    public MyClassLoader(String directory) {
        this.directory = directory;
    }

    @Override
    protected Class<?> findClass(String name) {
        System.out.println("MyClassLoader find class :" + name);
        try {
            String file = this.directory + File.separator + name.replace(".", File.separator) + ".class";
            FileInputStream inputStream = new FileInputStream(file);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int len = -1;
            while ((len = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, len);
            }
            byte[] data = outputStream.toByteArray();
            inputStream.close();
            outputStream.close();
            return defineClass(name, data, 0, data.length);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws Exception {
        MyClassLoader myClassLoader = new MyClassLoader("D:\\workspace\\JavaCodeSnippet\\JavaBase\\src\\main\\java\\org\\snippet\\classLoader");
        Class<?> app = myClassLoader.loadClass("org.snippet.App");
        Object instance = app.getDeclaredConstructor().newInstance();
    }
}
