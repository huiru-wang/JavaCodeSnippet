package com.snippet.designpattern.observer;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;


public class NotificationCenter {

    private static final List<WeatherListener> listeners;

    // SPI：在META-INF/services下 增加接口的实现类
    static {
        ServiceLoader<WeatherListener> weatherListeners = ServiceLoader.load(WeatherListener.class);
        listeners = new ArrayList<>();
        for (WeatherListener weatherListener : weatherListeners) {
            listeners.add(weatherListener);
        }
    }

    /**
     * 发布后，调用订阅者回调，这里使用同步方式
     * 根据具体场景可以改为异步
     */
    public void publishTempDown(String temperature) {
        // 执行回调
        listeners.forEach(listener -> listener.onTemperatureDown(temperature));
    }

    public void publishTempUp(String temperature) {
        // 执行回调
        listeners.forEach(listener -> listener.onTemperatureUp(temperature));
    }

}
