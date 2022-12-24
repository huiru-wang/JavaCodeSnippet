package com.snippet.designpattern.observer;

public interface WeatherListener {

    void onTemperatureUp(String temperature);

    void onTemperatureDown(String temperature);
}
