package ru.leonov.a1l3_weather.Storages;

import java.io.Serializable;

public class Settings implements Serializable {

    static final String PRESSURE_KEY = "SHOW_PRESSURE";
    static final String HUMIDITY_KEY = "SHOW_HUMIDITY";
    static final String WIND_SPEED_KEY = "SHOW_WIND_SPEED";
    static final String IS_CELSIUS_KEY = "IS_CELSIUS";
    static final String CITIES_KEY = "CITIES";

    public boolean showPressure;
    public boolean showHumidity;
    public boolean showWindSpeed;
    public boolean isCelsius;
    String cities;
}
