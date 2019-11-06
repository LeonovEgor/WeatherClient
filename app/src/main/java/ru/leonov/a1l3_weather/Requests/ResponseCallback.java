package ru.leonov.a1l3_weather.Requests;

import java.util.ArrayList;

import ru.leonov.a1l3_weather.Data.WeatherData;

public interface ResponseCallback {
    void response(ArrayList<WeatherData> response);
    void responseError(String error);
}