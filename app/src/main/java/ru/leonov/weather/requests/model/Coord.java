package ru.leonov.weather.requests.model;

import com.google.gson.annotations.SerializedName;

//"coord": { "lon": 139,"lat": 35}
public class Coord {
    @SerializedName("lat") public float latitude;
    @SerializedName("lon") public float longitude;
}