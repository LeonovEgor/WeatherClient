package ru.leonov.a1l3_weather.Data;

public interface DataSource {
    void requestDataSource(int cityIndex, ResponseCallback callback);
}
