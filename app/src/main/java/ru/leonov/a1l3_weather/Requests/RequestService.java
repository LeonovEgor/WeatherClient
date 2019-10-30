package ru.leonov.a1l3_weather.Requests;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;
import java.util.ArrayList;

import ru.leonov.a1l3_weather.Data.DataSource;
import ru.leonov.a1l3_weather.Data.WeatherData;
import ru.leonov.a1l3_weather.Data.WeatherDataSource;

public class RequestService extends IntentService {
    public static final String CITY = "CITY";
    public static final String ACTION_REQUEST_SERVICE = "ru.leonov.a1l3_weather.RESPONSE";
    public static final String WEATHER_KEY = "WEATHER_DATA_LIST";

    public RequestService() {
        super("RequestService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent == null) {
            response(null);
            return;
        }

        String city = intent.getStringExtra(CITY);
        ArrayList<WeatherData> list = request(city);
        response(list);
    }

    private ArrayList<WeatherData> request(String city) {
        DataSource source = new WeatherDataSource(getResources());
        return source.requestDataSource(city);
    }

    private void response(ArrayList<WeatherData> list){
        Intent responseIntent = new Intent(ACTION_REQUEST_SERVICE);
        responseIntent.putExtra(WEATHER_KEY, list);
        sendBroadcast(responseIntent);
    }
}

