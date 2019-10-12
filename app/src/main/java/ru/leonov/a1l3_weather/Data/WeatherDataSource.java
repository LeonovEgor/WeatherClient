package ru.leonov.a1l3_weather.Data;

import android.content.res.Resources;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import ru.leonov.a1l3_weather.R;

public class WeatherDataSource implements DataSource{
    private Resources resources;

    public WeatherDataSource(Resources res) {
        this.resources = res;
    }
    @Override
    public List<WeatherData> getDataSource(int cityIndex) {
        String[] dayOfWeek = resources.getStringArray(R.array.dayOfWeek);
        WeatherData[] data = new WeatherData[7];
        for(int i=0; i<7; i++) {
            String[] cities = resources.getStringArray(R.array.cities);
            data[i] = new WeatherData(
                    cities[cityIndex],
                    GetParam(R.string.pressure, new Random().nextInt(1000), R.string.pressureDimension),
                    GetParam(R.string.humidity, new Random().nextInt(100), R.string.humidityDimension),
                    GetParam(R.string.windSpeed, new Random().nextInt(40), R.string.windSpeedDimension),
                    GetParam(R.string.temperature, new Random().nextInt(50), R.string.celsiusDimension),
                    dayOfWeek[i]);
        }

        List<WeatherData> list = new ArrayList<>(data.length);
        list.addAll(Arrays.asList(data));
        return list;
    }

    private String GetParam(int resourceParamName, int value, int resourceParamDimensionName) {

        return getFullParameterString(
                resources.getString(resourceParamName),
                String.valueOf(value),
                resources.getString(resourceParamDimensionName));
    }

    private String getFullParameterString(String paramName, String paramValue, String paramDimension) {
        return String.format("%s: %s %s", paramName, paramValue, paramDimension);
    }

}
