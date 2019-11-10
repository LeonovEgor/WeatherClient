package ru.leonov.a1l3_weather.Storages;

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
        settings.isCelsius = pref.getBoolean(Settings.IS_CELSIUS_KEY, true);
        return settings;
    }

    public static void saveSettings(Activity activity, Settings settings) {
        SharedPreferences pref = activity.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Settings.CITIES_KEY, settings.cities);
        editor.putString(Settings.CITIES_KEY, settings.cities);
        editor.putBoolean(Settings.PRESSURE_KEY, settings.showPressure);
        editor.putBoolean(Settings.HUMIDITY_KEY, settings.showHumidity);
        editor.putBoolean(Settings.WIND_SPEED_KEY, settings.showWindSpeed);
        editor.putBoolean(Settings.IS_CELSIUS_KEY, settings.isCelsius);

        editor.apply();
    }
}