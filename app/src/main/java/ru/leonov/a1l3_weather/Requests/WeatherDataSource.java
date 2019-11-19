package ru.leonov.a1l3_weather.Requests;

import android.annotation.SuppressLint;
import android.content.res.Resources;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.leonov.a1l3_weather.Data.WeatherData;
import ru.leonov.a1l3_weather.R;
import ru.leonov.a1l3_weather.Requests.Model.WeatherRequestRestModel;

public class WeatherDataSource implements DataSource {
    private static final String OPEN_WEATHER_API_KEY = "1eb209182666b630fb58efb30a93cb00";
    private static final String UNITS = "metric"; //TODO: Вывести в настройки и оттуда выбирать единицы изменения
    private static final String ICONS_URL = "http://openweathermap.org/img/wn/";

    private final Resources resources;
    private boolean isCelsius;

    public WeatherDataSource(Resources res) {
        this.resources = res;
    }

    @Override
    public void requestDataSource(String city, boolean isCelsius, ResponseCallback callback) {
        this.isCelsius = isCelsius;
        updateWeatherData(city, callback);
    }

    private void updateWeatherData(final String city, final ResponseCallback callback) {
        OpenWeatherRepo.getInstance().getAdapter().loadWeather(city,
                OPEN_WEATHER_API_KEY, UNITS)
                .enqueue(new Callback<WeatherRequestRestModel>() {
                    @SuppressLint("DefaultLocale")
                    @Override
                    public void onResponse(@NonNull Call<WeatherRequestRestModel> call,
                                           @NonNull Response<WeatherRequestRestModel> response) {
                        if (response.body() != null && response.isSuccessful()) {
                            final ArrayList<WeatherData> data = renderWeather(response.body());
                            if (data == null) callback.responseError("JSON serialize error");
                            else callback.response(data);
                        } else {
                            if(response.body() != null)
                                callback.responseError(String.format("Error: %d", response.body().cod));
                            else
                                callback.responseError(resources.getString(R.string.SomeWrong));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<WeatherRequestRestModel> call, @NonNull Throwable t) {
                        callback.responseError(t.getMessage());
                    }
                });
    }

    private ArrayList<WeatherData> renderWeather(WeatherRequestRestModel model) {
        ArrayList<WeatherData> list = new ArrayList<>();
        if (model == null) return null;

        String pressure = GetText(R.string.pressure,
                String.valueOf(model.main.pressure),
                R.string.pressureDimension);
        String humidity = GetText(R.string.humidity,
                String.valueOf(model.main.humidity),
                R.string.humidityDimension);
        String windSpeed = WindSpeed(model.wind.speed);
        String temperature = getTemperature(model.main.temp);
        String weatherIcon = getWeatherIconUrl(model.weather[0].icon);

        list.add(new WeatherData(model.name, pressure,
                humidity, windSpeed, temperature, weatherIcon, model.dt));

        return list;
    }

    private String getTemperature(float temperature) {
        return String.format(Locale.getDefault(), "%s: %.0f %s",
                resources.getString(R.string.temperature),
                Math.ceil(temperature),
                isCelsius?
                        resources.getString(R.string.celsiusDimension):
                        resources.getString(R.string.fahrenheitDimension));
    }

    private String WindSpeed(float wind) {
        return String.format(Locale.getDefault(),
                "%s: %.0f %s",
                resources.getString(R.string.windSpeed),
                Math.ceil(wind),
                resources.getString(R.string.windSpeedDimension));
    }

    //http://openweathermap.org/img/wn/10d@2x.png
    private String getWeatherIconUrl(String IconId) {
        return String.format("%s%s@2x.png", ICONS_URL, IconId);
    }

    private String GetText(int resourceParamName, String value, int resourceParamDimensionName) {
        return String.format("%s: %s %s",
                resources.getString(resourceParamName),
                value,
                resources.getString(resourceParamDimensionName));
    }
}