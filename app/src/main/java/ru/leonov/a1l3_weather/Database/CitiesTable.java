package ru.leonov.a1l3_weather.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.ArrayMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

public class CitiesTable {
    private final static String CITIES_TABLE_NAME = "Cities";
    private final static String COLUMN_CITIES_ID = "_id";
    private final static String COLUMN_CITIES_NAME = "Name";


    static void createTable(SQLiteDatabase database) {
        String CreateCitiesTableRequest =
                String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "%s TEXT);", CITIES_TABLE_NAME, COLUMN_CITIES_ID, COLUMN_CITIES_NAME);
        database.execSQL(CreateCitiesTableRequest);
    }

//    static void onUpgrade(SQLiteDatabase database) {
//        database.execSQL("ALTER TABLE " + CITIES_TABLE_NAME + " ADD COLUMN " + COLUMN_CITIES_DATE
//                + " TEXT DEFAULT 'Default title'");
//    }

    public static long addCity(String city, SQLiteDatabase database) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_CITIES_NAME, city);

        return database.insert(CITIES_TABLE_NAME, null, values);
    }

    public static void editCityName(long index, String cityNewName, SQLiteDatabase database) {

        database.execSQL("UPDATE " + CITIES_TABLE_NAME + " set " + COLUMN_CITIES_NAME + " = " + cityNewName + " WHERE "
                + COLUMN_CITIES_ID + " = " + index + ";");
    }

    public static void deleteCity(long index, SQLiteDatabase database) {
        database.delete(CITIES_TABLE_NAME,
                COLUMN_CITIES_ID + " = " + index, null);
    }

    public static void deleteAllCities(SQLiteDatabase database) {
        database.delete(CITIES_TABLE_NAME, null, null);
    }

    public static Map<Long, String> getAllCities(SQLiteDatabase database) {
        Cursor cursor = database.rawQuery("SELECT * FROM " + CITIES_TABLE_NAME, null);
        return getResultFromCursor(cursor);
    }

    private static Map<Long, String> getResultFromCursor(Cursor cursor) {
        Map<Long, String> result = null;

        if(cursor != null && cursor.moveToFirst()) {
            result = new ArrayMap<>(cursor.getCount());

            int cityId = cursor.getColumnIndex(COLUMN_CITIES_ID);
            int cityName = cursor.getColumnIndex(COLUMN_CITIES_NAME);
            do {
                result.put(cursor.getLong(cityId), cursor.getString(cityName));
            } while (cursor.moveToNext());
        }

        try {
            if (cursor != null) cursor.close(); }
        catch (Exception ignored) {}

        return result == null ? new ArrayMap<Long, String>(0) : result;
    }
}
