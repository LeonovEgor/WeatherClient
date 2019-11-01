package ru.leonov.a1l3_weather.Storages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SettingsHelper {
    public static List<String> getCityListFromString(String cities) {
        return new ArrayList<>(Arrays.asList(cities.split(",")));
    }

    public static String getCitesStringFromList(List<String> list) {
        return android.text.TextUtils.join(",", list);
    }

}
