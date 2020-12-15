package ru.leonov.weather.requests;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.leonov.weather.requests.model.WeatherRequestRestModel;

public interface IOpenWeather {
    String API_PATH = "data/2.5/weather";

    @GET(API_PATH)
    Call<WeatherRequestRestModel> loadWeather(@Query("q") String city,
                                              @Query("apiKey") String keyApi,
                                              @Query("units") String units);

    @GET(API_PATH)
    Call<WeatherRequestRestModel> loadWeatherByGeo(@Query("lat") String lat,
                                                   @Query("lon") String lon,
                                              @Query("apiKey") String keyApi,
                                              @Query("units") String units);
}