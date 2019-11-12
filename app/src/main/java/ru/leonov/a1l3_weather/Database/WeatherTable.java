package ru.leonov.a1l3_weather.Database;

import android.database.sqlite.SQLiteDatabase;

public class WeatherTable {
    private final static String WEATHER_TABLE_NAME = "Weather";
    private final static String COLUMN_WEATHER_ID = "_id";
    private final static String COLUMN_WEATHER_CITIES_ID = "CityId";
    private final static String COLUMN_WEATHER_DATE = "Date";
    private final static String COLUMN_WEATHER_TEMPERATURE = "Temperature";
    private final static String COLUMN_WEATHER_HUMIDITY = "Humidity";
    private final static String COLUMN_WEATHER_PRESSURE = "Pressure";
    private final static String COLUMN_WEATHER_WIND_SPEED = "WindSpeed";

    static void createTable(SQLiteDatabase database) {
        String CreateWeatherTableRequest =
                String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                "%S INTEGER, %s INTEGER, %s TEXT, %s TEXT, %s TEXT, %s TEXT);",
                        WEATHER_TABLE_NAME, COLUMN_WEATHER_ID, COLUMN_WEATHER_CITIES_ID,
                        COLUMN_WEATHER_DATE, COLUMN_WEATHER_TEMPERATURE, COLUMN_WEATHER_HUMIDITY,
                        COLUMN_WEATHER_PRESSURE, COLUMN_WEATHER_WIND_SPEED);
        database.execSQL(CreateWeatherTableRequest);
    }

}
