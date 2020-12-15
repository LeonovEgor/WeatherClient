package ru.leonov.weather.storages;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class Storage {
    private static final String SETTINGS = "WEATHER";

    public static Settings loadSettings(Activity activity) {
        Settings settings = new Settings();
        SharedPreferences pref = activity.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
        settings.cities = pref.getString(Settings.CITIES_KEY, "");
        settings.showPressure = pref.getBoolean(Settings.PRESSURE_KEY, true);
        settings.showHumidity = pref.getBoolean(Settings.HUMIDITY_KEY, true);
        settings.showWindSpeed = pref.getBoolean(Settings.WIND_SPEED_KEY, true);
        settings.units = pref.getString(Settings.UNITS_KEY, "metric");
        settings.lastLat = pref.getFloat(Settings.LASTLAT_KEY, 0);
        settings.lastLong = pref.getFloat(Settings.LASTLONG_KEY, 0);

        return settings;
    }

    public static void saveSettings(Activity activity, Settings settings) {
        SharedPreferences pref = activity.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Settings.CITIES_KEY, settings.cities);
        editor.putBoolean(Settings.PRESSURE_KEY, settings.showPressure);
        editor.putBoolean(Settings.HUMIDITY_KEY, settings.showHumidity);
        editor.putBoolean(Settings.WIND_SPEED_KEY, settings.showWindSpeed);
        editor.putString(Settings.UNITS_KEY, settings.units);
        editor.putFloat(Settings.LASTLAT_KEY, (float)settings.lastLat);
        editor.putFloat(Settings.LASTLONG_KEY, (float)settings.lastLong);

        editor.apply();
    }
}