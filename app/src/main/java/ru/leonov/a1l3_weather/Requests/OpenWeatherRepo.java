package ru.leonov.a1l3_weather.Requests;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class OpenWeatherRepo {
    private static final String BASE_URL = "https://api.openweathermap.org/";

    private static OpenWeatherRepo instance = null;
    private IOpenWeather adapter;

    private OpenWeatherRepo() {
        adapter = createAdapter();
    }

    static OpenWeatherRepo getInstance() {
        if(instance == null) instance = new OpenWeatherRepo();

        return instance;
    }

    IOpenWeather getAdapter() {
        return adapter;
    }

    private IOpenWeather createAdapter() {
        Retrofit adapter = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return adapter.create(IOpenWeather.class);
    }
}