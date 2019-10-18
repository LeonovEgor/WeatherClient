package ru.leonov.a1l3_weather.Data;

import java.util.ArrayList;

public interface ResponseCallback {
    void response(ArrayList<WeatherData> response);
}
