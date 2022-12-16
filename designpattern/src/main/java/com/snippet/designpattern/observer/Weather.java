package com.snippet.designpattern.observer;

public class Weather {

    private final NotificationCenter notificationCenter;

    public Weather(NotificationCenter notificationCenter) {
        this.notificationCenter = notificationCenter;
    }

    public void riseUp(String temperature) {
        System.out.println("weather is rise up:" + temperature);

        // 发布消息
        notificationCenter.publishTempUp(temperature);
    }

    public void coolDown(String temperature) {
        System.out.println("weather is cool down:" + temperature);

        // 发布消息
        notificationCenter.publishTempDown(temperature);
    }

}
