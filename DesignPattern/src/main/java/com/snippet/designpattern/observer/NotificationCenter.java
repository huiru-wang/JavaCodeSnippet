package com.snippet.designpattern.observer;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

public class NotificationCenter {

    private static final List<WeatherListener> listeners;

    static {
        ServiceLoader<WeatherListener> weatherListeners = ServiceLoader.load(WeatherListener.class);
        listeners = new ArrayList<>();
        for (WeatherListener weatherListener : weatherListeners) {
            listeners.add(weatherListener);
        }
    }

    public void publishTempDown(String temperature) {
        // 执行回调
        listeners.forEach(listener -> listener.onTemperatureDown(temperature));
    }

    public void publishTempUp(String temperature) {
        // 执行回调
        listeners.forEach(listener -> listener.onTemperatureUp(temperature));
    }

}
