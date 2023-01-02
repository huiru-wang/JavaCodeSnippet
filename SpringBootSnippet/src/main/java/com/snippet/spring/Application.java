package com.snippet.spring;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Properties;

@SpringBootApplication
public class Application {

    public static void main(String[] args) throws IllegalAccessException, NoSuchFieldException, IOException {

        // ConfigurableApplicationContext 实现 BeanFactory接口，并组合一个BeanFactory对象(DefaultListableBeanFactory)
        ConfigurableApplicationContext applicationContext = SpringApplication.run(Application.class, args);

        /**=============================BeanFactory=================================
         * BeanFactory: 声明了spring核心容器的基本接口：getBean、getType、isSingleton等等
         * DefaultListableBeanFactory: IOC核心实现类，并在BeanFactory基础上，增强了IOC的功能：
         * 1. 注册所有的Bean Definition，创建Bean的元数据对象
         * 2. 对Bean进行初始化的功能；
         * 3. 多种的Bean后置处理能力：处理注解(@Bean)、处理依赖注入(@Autowire)
         * 4. 延迟加载功能(一般不使用懒加载，提前创建好Bean，加速运行时)
         *
         * 容器通过ConcurrentHashMap存放Spring管理的bean：
         * 1. 一级缓存：Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256);
         *
         */
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) applicationContext.getBeanFactory();


        Field singletonObjectsField = DefaultSingletonBeanRegistry.class.getDeclaredField("singletonObjects");
        singletonObjectsField.setAccessible(true);
        Map<String, Object> singletonObjects = (Map<String, Object>) singletonObjectsField.get(beanFactory);
        for (Map.Entry<String, Object> bean : singletonObjects.entrySet()) {
            // 找到自定义的Bean
            if (bean.getKey().equals("helloController")) {
                System.out.println(bean.getKey() + " = " + bean.getValue());
            }
        }

        /**=============================ApplicationContext=================================
         * ApplicationContext它依然是一个spring容器，在BeanFactory的基础功能之上，进行扩展
         * 提供：
         * 1. 实现BeanFactory接口，提供基础的Bean功能
         * 2. 实现MessageSource，提供国际化功能，基本都在前端处理了，后端基本不处理国际化
         * 3. 实现ResourceLoader，提供加载配置文件、环境变量的能力
         * 4. 实现ApplicationEventPublisher，提供事件发布功能
         */

        // 实现ResourceLoader接口，提供加载配置文件、环境变量的能力
        Resource resource = applicationContext.getResource("classpath:common.properties");
        Properties properties = PropertiesLoaderUtils.loadProperties(resource);

        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        String serverPort = environment.getProperty("server.port");

        // 实现ApplicationEventPublisher发布事件
        applicationContext.publishEvent(new Object());
    }

}
