package ru.leonov.a1l3_weather.Requests;

public interface DataSource {
    void requestDataSource(String city, ResponseCallback callback);
}
