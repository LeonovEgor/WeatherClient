package ru.leonov.a1l3_weather.Data;

public class WeatherData {
    public String city;
    public String pressure;
    public String humidity;
    public String windSpeed;
    public String temperature;
    public String dayOfWeek;

    public WeatherData(String city, String pressure, String humidity, String windSpeed, String temperature, String dayOfWeek) {
        this.city = city;
        this.pressure = pressure;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.temperature = temperature;
        this.dayOfWeek = dayOfWeek;
    }
}
