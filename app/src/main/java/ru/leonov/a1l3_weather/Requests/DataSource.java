package ru.leonov.a1l3_weather.Requests;

public interface DataSource {
    void requestDataSource(String city, String units, ResponseCallback callback);
    void requestDataSourceByGeo(String latitude, String longitude, String units, ResponseCallback callback);
}