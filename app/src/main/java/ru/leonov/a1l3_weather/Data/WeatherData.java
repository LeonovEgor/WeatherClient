package ru.leonov.a1l3_weather.Data;

import java.io.Serializable;

public class WeatherData implements Serializable {
    public String city;
    public String pressure;
    public String humidity;
    public String windSpeed;
    public String temperature;
    public String weatherIcon;
    public String updateDate;

    public WeatherData(String city, String pressure, String humidity, String windSpeed, String temperature,
                       String weatherIcon, String updateDate) {
        this.city = city;
        this.pressure = pressure;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.temperature = temperature;
        this.weatherIcon = weatherIcon;
        this.updateDate = updateDate;
    }
}
