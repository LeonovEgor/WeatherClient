package ru.leonov.weather.storages;

import java.io.Serializable;

public class Settings implements Serializable {

    static final String PRESSURE_KEY = "SHOW_PRESSURE";
    static final String HUMIDITY_KEY = "SHOW_HUMIDITY";
    static final String WIND_SPEED_KEY = "SHOW_WIND_SPEED";
    static final String UNITS_KEY = "UNITS";
    static final String CITIES_KEY = "CITIES";
    static final String LASTLAT_KEY = "LASTLAT_KEY";
    static final String LASTLONG_KEY = "LASTLONG_KEY";

    public static final String METRIC = "metric";
    public static final String IMPERIAL = "imperial";

    public boolean showPressure;
    public boolean showHumidity;
    public boolean showWindSpeed;
    public String units;
    public String cities;
    public double lastLat;
    public double lastLong;

}