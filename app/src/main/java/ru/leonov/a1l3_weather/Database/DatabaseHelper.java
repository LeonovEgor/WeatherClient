package ru.leonov.a1l3_weather.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import ru.leonov.a1l3_weather.Data.WeatherData;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "weather.db";
    private static final int DATABASE_VERSION = 2;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        CitiesTable.createTable(sqLiteDatabase);
        WeatherTable.createTable(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        WeatherTable.createTable(sqLiteDatabase);
    }

    public static void UpdateForecast(SQLiteDatabase sqLiteDatabase, String city, ArrayList<WeatherData> response) {
        long cityId = CitiesTable.getId(city, sqLiteDatabase);
        WeatherTable.UpdateOrReplaceForecast(cityId, response.get(0), sqLiteDatabase);
    }

    public static WeatherData getForecast(SQLiteDatabase sqLiteDatabase, String city) {
        long cityId = CitiesTable.getId(city, sqLiteDatabase);
        return WeatherTable.getForecast(city, cityId, sqLiteDatabase);
    }
}