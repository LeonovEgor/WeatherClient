package ru.leonov.a1l3_weather.Storages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SettingsHelper {
    public static List<String> getCityListFromString(String cities) {
        ArrayList<String> list = new ArrayList<>(Arrays.asList(cities.split(",")));
        if(list.size() == 1 && list.get(0).equals("")) {
            list.clear();
        }
        return list;
    }

    public static String getCitesStringFromList(List<String> list) {
        return android.text.TextUtils.join(",", list);
    }
}
