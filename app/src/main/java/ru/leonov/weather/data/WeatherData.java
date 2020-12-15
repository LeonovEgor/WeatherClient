package ru.leonov.weather.data;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;

public class WeatherData implements Serializable {
    public String city;
    public String pressure;
    public String humidity;
    public String windSpeed;
    public String temperature;
    public String weatherIcon;
    public long updateDate;
    public String units;

    public String getUpdateDate() {
        DateFormat dateFormat = DateFormat.getDateTimeInstance();
        return dateFormat.format(new Date(updateDate * 1000));
    }


    public WeatherData(String city, String pressure, String humidity, String windSpeed, String temperature,
                       String weatherIcon, long updateDate, String units) {
        this.city = city;
        this.pressure = pressure;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.temperature = temperature;
        this.weatherIcon = weatherIcon;
        this.updateDate = updateDate;
        this.units = units;
    }

}