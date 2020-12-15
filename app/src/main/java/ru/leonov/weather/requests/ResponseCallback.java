package ru.leonov.weather.requests;

import java.util.ArrayList;

import ru.leonov.weather.data.WeatherData;

public interface ResponseCallback {
    void response(ArrayList<WeatherData> response);
    void responseError(String error);
}