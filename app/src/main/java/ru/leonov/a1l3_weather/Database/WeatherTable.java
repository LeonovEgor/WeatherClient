package ru.leonov.a1l3_weather.Database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import ru.leonov.a1l3_weather.Data.WeatherData;

public class WeatherTable {
    private final static String WEATHER_TABLE_NAME = "Weather";
    private final static String COLUMN_WEATHER_CITIES_ID = "CityId";
    private final static String COLUMN_WEATHER_DATE = "Date";
    private final static String COLUMN_WEATHER_TEMPERATURE = "Temperature";
    private final static String COLUMN_WEATHER_HUMIDITY = "Humidity";
    private final static String COLUMN_WEATHER_PRESSURE = "Pressure";
    private final static String COLUMN_WEATHER_WIND_SPEED = "WindSpeed";

    static void createTable(SQLiteDatabase database) {
        String CreateWeatherTableRequest =
                String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY, " +
                                "%s INTEGER, " +
                                "%s TEXT, %s TEXT, " +
                                "%s TEXT, %s TEXT);",
                        WEATHER_TABLE_NAME, COLUMN_WEATHER_CITIES_ID,
                        COLUMN_WEATHER_DATE,
                        COLUMN_WEATHER_TEMPERATURE, COLUMN_WEATHER_HUMIDITY,
                        COLUMN_WEATHER_PRESSURE, COLUMN_WEATHER_WIND_SPEED);

        database.execSQL(CreateWeatherTableRequest);
    }

    static boolean isWeatherForecastExists(int cityId, SQLiteDatabase database) {
        String Query = "SELECT * FROM " + WEATHER_TABLE_NAME + " WHERE " + COLUMN_WEATHER_CITIES_ID
                + " = '" + cityId + "';";
        Cursor cursor = database.rawQuery(Query, null);
        if (cursor != null) {
            try {
                return cursor.getInt(0) > 0;
            } finally {
                cursor.close();
            }
        }
        return false;
    }

    static void UpdateForecast(int cityId, WeatherData data, SQLiteDatabase database) {
       String sql = "UPDATE " + WEATHER_TABLE_NAME + " SET " +
                COLUMN_WEATHER_DATE + " = " + data.updateDate + ", " +
                COLUMN_WEATHER_TEMPERATURE + " = " + data.temperature + ", " +
                COLUMN_WEATHER_HUMIDITY  + " = " + data.humidity + ", " +
                COLUMN_WEATHER_PRESSURE + " = " + data.pressure + ", " +
                COLUMN_WEATHER_WIND_SPEED + " = " + data.windSpeed +
                "WHERE " + COLUMN_WEATHER_CITIES_ID + " = " + cityId;
        database.execSQL(sql);
    }

    public static void deleteForecast(int cityId, SQLiteDatabase database) {
        database.delete(WEATHER_TABLE_NAME,
                COLUMN_WEATHER_CITIES_ID + " = '" + cityId + "';", null);
    }

    static void UpdateOrReplaceForecast(long cityId, WeatherData data, SQLiteDatabase database) {
        String sql = "REPLACE INTO " + WEATHER_TABLE_NAME + " (" +
                COLUMN_WEATHER_CITIES_ID + ", " +
                COLUMN_WEATHER_DATE + ", " +
                COLUMN_WEATHER_TEMPERATURE + ", " +
                COLUMN_WEATHER_HUMIDITY + ", " +
                COLUMN_WEATHER_PRESSURE + ", " +
                COLUMN_WEATHER_WIND_SPEED + ") VALUES (" +
                cityId + ", " +
                data.updateDate + ", " +
                data.temperature + ", " +
                data.humidity + ", " +
                data.pressure + ", " +
                data.windSpeed + ");";

        database.execSQL(sql);
    }
}