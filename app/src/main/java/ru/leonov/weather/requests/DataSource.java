package ru.leonov.weather.requests;

public interface DataSource {
    void requestDataSource(String city, String units, ResponseCallback callback);
    void requestDataSourceByGeo(String latitude, String longitude, String units, ResponseCallback callback);
}