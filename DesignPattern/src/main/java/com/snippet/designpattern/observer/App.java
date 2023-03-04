package com.snippet.designpattern.observer;

public class App {

    public static void main(String[] args) throws InterruptedException {
        Weather weather = new Weather(new NotificationCenter());

        weather.riseUp("35");

        weather.coolDown("12");

    }
}
