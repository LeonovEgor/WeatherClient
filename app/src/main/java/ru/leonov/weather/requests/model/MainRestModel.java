package ru.leonov.weather.requests.model;

import com.google.gson.annotations.SerializedName;

public class MainRestModel {
    @SerializedName("temp") public float temp;
    @SerializedName("pressure") public float pressure;
    @SerializedName("humidity") public float humidity;
    @SerializedName("temp_min") public float tempMin;
    @SerializedName("temp_max") public float tempMax;
}