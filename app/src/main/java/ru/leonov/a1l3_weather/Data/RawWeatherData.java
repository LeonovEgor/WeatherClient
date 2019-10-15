package ru.leonov.a1l3_weather.Data;

public class RawWeatherData {
    public String city;
    public double pressure;
    public double humidity;
    public double windSpeed;
    public double temperature;

    public RawWeatherData(String city, double pressure, double humidity, double windSpeed, double temperature) {
        this.city = city;
        this.pressure = pressure;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.temperature = temperature;
    }
}
