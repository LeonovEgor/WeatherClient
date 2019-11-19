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
    private final static String COLUMN_WEATHER_ICON = "Icon";

    static void createTable(SQLiteDatabase database) {
        String CreateWeatherTableRequest =
                String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY, " +
                                "%s INTEGER, " +
                                "%s TEXT, %s TEXT, " +
                                "%s TEXT, %s TEXT, " +
                                "%s TEXT);",
                        WEATHER_TABLE_NAME, COLUMN_WEATHER_CITIES_ID,
                        COLUMN_WEATHER_DATE,
                        COLUMN_WEATHER_TEMPERATURE, COLUMN_WEATHER_HUMIDITY,
                        COLUMN_WEATHER_PRESSURE, COLUMN_WEATHER_WIND_SPEED,
                        COLUMN_WEATHER_ICON);

        database.execSQL(CreateWeatherTableRequest);
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
                COLUMN_WEATHER_WIND_SPEED + ", " +
                COLUMN_WEATHER_ICON + ") VALUES (" +
                cityId + ", " +
                data.updateDate + ", " +
                "'" + data.temperature + "', " +
                "'" + data.humidity + "', " +
                "'" + data.pressure + "', " +
                "'" + data.windSpeed + "', " +
                "'" + data.weatherIcon + "');";

        database.execSQL(sql);
    }

    static WeatherData getForecast(String city, long cityId, SQLiteDatabase database) {
        String Query = "SELECT * FROM " + WEATHER_TABLE_NAME + " WHERE " + COLUMN_WEATHER_CITIES_ID
                + " = '" + cityId + "';";
        Cursor cursor = database.rawQuery(Query, null);
        if (cursor != null && cursor.moveToFirst()) {
            int colDate = cursor.getColumnIndex(COLUMN_WEATHER_DATE);
            int colTemp = cursor.getColumnIndex(COLUMN_WEATHER_TEMPERATURE);
            int colHum = cursor.getColumnIndex(COLUMN_WEATHER_HUMIDITY);
            int colPres = cursor.getColumnIndex(COLUMN_WEATHER_PRESSURE);
            int colWind = cursor.getColumnIndex(COLUMN_WEATHER_WIND_SPEED);
            int colIcon = cursor.getColumnIndex(COLUMN_WEATHER_ICON);

            try {
                return new WeatherData(city,
                        cursor.getString(colPres),
                        cursor.getString(colHum),
                        cursor.getString(colWind),
                        cursor.getString(colTemp),
                        cursor.getString(colIcon),
                        cursor.getLong(colDate)
                );

            } finally {
                cursor.close();
            }
        }
        return new WeatherData("NoDataFound", "NoDataFound", "NoDataFound",
                "NoDataFound", "NoDataFound", "NoDataFound", 0);
    }
}