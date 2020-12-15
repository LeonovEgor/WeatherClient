package ru.leonov.weather.requests.Model;

import com.google.gson.annotations.SerializedName;

public class WeatherRequestRestModel {
    @SerializedName("weather") public WeatherRestModel[] weather;
    @SerializedName("coord") public Coord coord;
    @SerializedName("main") public MainRestModel main;
    @SerializedName("wind") public WindRestModel wind;
    @SerializedName("dt") public long dt;
    @SerializedName("id") public  long id;
    @SerializedName("name") public String name;
    @SerializedName("cod") public int cod;
}
