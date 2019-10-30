package ru.leonov.a1l3_weather.Data;

import java.util.ArrayList;

public interface DataSource {
    ArrayList<WeatherData> requestDataSource(String city);
}
