package ru.leonov.a1l3_weather.Data;

import java.util.List;

public interface DataSource {
    List<WeatherData> getDataSource(int cityIndex);
}
