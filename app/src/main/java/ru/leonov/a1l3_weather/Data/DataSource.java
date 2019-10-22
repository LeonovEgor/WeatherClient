package ru.leonov.a1l3_weather.Data;

public interface DataSource {
    void requestDataSource(String city, ResponseCallback callback);
}
