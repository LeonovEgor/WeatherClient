package ru.leonov.a1l3_weather.Requests;

public interface DataSource {
    void requestDataSource(String city, boolean isCelsius, ResponseCallback callback);
    void requestDataSourceByGoe(String latitude, String longitude, boolean isCelsius, ResponseCallback callback);
}