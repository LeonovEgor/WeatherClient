package ru.leonov.a1l3_weather.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class CitiesTable {
    private final static String CITIES_TABLE_NAME = "Cities";
    private final static String COLUMN_CITIES_ID = "_id";
    private final static String COLUMN_CITIES_NAME = "Name";


    static void createTable(SQLiteDatabase database) {
        String CreateCitiesTableRequest =
                String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "%S TEXT);", CITIES_TABLE_NAME, COLUMN_CITIES_ID, COLUMN_CITIES_NAME);
        database.execSQL(CreateCitiesTableRequest);
    }

//    static void onUpgrade(SQLiteDatabase database) {
//        database.execSQL("ALTER TABLE " + CITIES_TABLE_NAME + " ADD COLUMN " + COLUMN_CITIES_DATE
//                + " TEXT DEFAULT 'Default title'");
//    }

    public static void addCity(String city, SQLiteDatabase database) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_CITIES_NAME, city);

        database.insert(CITIES_TABLE_NAME, null, values);
    }

//    public static void editCityName(String cityOldName, String cityNewName, SQLiteDatabase database) {
//        ContentValues values = new ContentValues();
//        values.put(COLUMN_CITIES_NAME, newNote);
//
//        database.execSQL("UPDATE " + CITIES_TABLE_NAME + " set " + COLUMN_CITIES_NAME + " = " + newNote + " WHERE "
//                + COLUMN_CITIES_NAME + " = " + noteToEdit + ";");
//        //UPDATE Notes set note = 10 WHERE note = 5
//    }

    public static void deleteCity(String cityName, SQLiteDatabase database) {
        database.delete(CITIES_TABLE_NAME,
                COLUMN_CITIES_NAME + " = " + cityName, null);
    }

    public static void deleteAllCities(SQLiteDatabase database) {
        database.delete(CITIES_TABLE_NAME, null, null);
    }

    public static List<String> getAllCities(SQLiteDatabase database) {
        Cursor cursor = database.rawQuery("SELECT * FROM " + CITIES_TABLE_NAME, null);
        return getResultFromCursor(cursor);
    }

    private static List<String> getResultFromCursor(Cursor cursor) {
        List<String> result = null;

        if(cursor != null && cursor.moveToFirst()) {
            result = new ArrayList<>(cursor.getCount());

            int cityName = cursor.getColumnIndex(COLUMN_CITIES_NAME);
            do {
                result.add(cursor.getString(cityName));
            } while (cursor.moveToNext());
        }

        try {
            if (cursor != null) cursor.close(); }
        catch (Exception ignored) {}

        return result == null ? new ArrayList<String>(0) : result;
    }
}
