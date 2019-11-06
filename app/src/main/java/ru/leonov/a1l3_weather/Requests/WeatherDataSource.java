package ru.leonov.a1l3_weather.Requests;

import android.content.res.Resources;
import android.util.Log;

import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import ru.leonov.a1l3_weather.Data.WeatherData;
import ru.leonov.a1l3_weather.R;

public class WeatherDataSource implements DataSource {
    private static final String RESPONSE = "cod";
    private static final int ALL_GOOD = 200;


    private final static String LOG_TAG = WeatherDataSource.class.getSimpleName();

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
        OkHttpRequester requester = new OkHttpRequester(new OkHttpRequester.OnResponseCompleted() {
            @Override
            public void onCompleted(String content) {
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(content);
                    int code = jsonObject.getInt(RESPONSE);
                    if(code != ALL_GOOD) {
                        callback.responseError(String.valueOf(code));
                    } else {
                        final ArrayList<WeatherData> data = renderWeather(jsonObject);
                        if (data == null) callback.responseError("JSON parsing error");
                        else callback.response(data);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
                callback.responseError(error);
            }
        });
        requester.run(city);
    }

    private ArrayList<WeatherData> renderWeather(@Nullable JSONObject jsonObject) {
        ArrayList<WeatherData> list = new ArrayList<>();

        try {
            if (jsonObject == null) throw new Exception("Вернулся пустой запрос");
            Log.d(LOG_TAG, "json: " + jsonObject.toString());

            JSONObject details = jsonObject.getJSONArray("weather").getJSONObject(0);
            JSONObject main = jsonObject.getJSONObject("main");
            JSONObject wind = jsonObject.getJSONObject("wind");

            String city = getPlaceName(jsonObject);
            String pressure = GetText(R.string.pressure, getParam(main, "pressure"),
                    R.string.pressureDimension);
            String humidity = GetText(R.string.humidity, getParam(main, "humidity"),
                    R.string.humidityDimension);
            String windSpeed = WindSpeed(wind);
            String temperature = getTemperature(main);
            String date = getUpdateDate(jsonObject);
            String weatherIcon = getWeatherIcon(details.getInt("id"),
                    jsonObject.getJSONObject("sys").getLong("sunrise") * 1000,
                    jsonObject.getJSONObject("sys").getLong("sunset") * 1000);

            list.add(new WeatherData(city, pressure,
                    humidity, windSpeed, temperature, weatherIcon, date));
        } catch (Exception exc) {
            Log.e(LOG_TAG,
                    "One or more fields not found in the JSON data. " + exc.getMessage());
            exc.printStackTrace();
            list = null;
        }
        return list;
    }

    private String getPlaceName(JSONObject jsonObject) throws JSONException {
        return jsonObject.getString("name").toUpperCase() + ", "
                + jsonObject.getJSONObject("sys").getString("country");
    }

    private String getParam(JSONObject jsonObject, String paramName) throws JSONException {
        return jsonObject.getString(paramName);
    }

    private String getTemperature(JSONObject main) throws JSONException {
        return String.format(Locale.getDefault(), "%s: %.0f %s",
                resources.getString(R.string.temperature),
                        Math.ceil(main.getDouble("temp")),
                        isCelsius?
                                resources.getString(R.string.celsiusDimension):
                                resources.getString(R.string.fahrenheitDimension));
    }

    private String WindSpeed(JSONObject wind) throws JSONException {
        return String.format(Locale.getDefault(),
                "%s: %.0f %s",
                resources.getString(R.string.windSpeed),
                        Math.ceil(wind.getDouble("speed")),
                resources.getString(R.string.windSpeedDimension));
    }

    private String getUpdateDate(JSONObject jsonObject) throws JSONException {
        DateFormat dateFormat = DateFormat.getDateTimeInstance();
        return dateFormat.format(new Date(jsonObject.getLong("dt") * 1000));
    }

    private String getWeatherIcon(int actualId, long sunrise, long sunset) {
        int id = actualId / 100;
        String icon = "";

        if(actualId == 800) {
            long currentTime = new Date().getTime();
            if(currentTime >= sunrise && currentTime < sunset) {
                icon = resources.getString(R.string.weather_sunny);
            } else {
                icon = resources.getString(R.string.weather_clear_night);
            }
        } else {
            switch (id) {
                case 2: {
                    icon = resources.getString(R.string.weather_thunder);
                    break;
                }
                case 3: {
                    icon = resources.getString(R.string.weather_drizzle);
                    break;
                }
                case 5: {
                    icon = resources.getString(R.string.weather_rainy);
                    break;
                }
                case 6: {
                    icon = resources.getString(R.string.weather_snowy);
                    break;
                }
                case 7: {
                    icon = resources.getString(R.string.weather_foggy);
                    break;
                }
                case 8: {
                    icon = resources.getString(R.string.weather_cloudy);
                    break;
                }
            }
        }
        return icon;
    }

    private String GetText(int resourceParamName, String value, int resourceParamDimensionName) {
        return getFullParameterString(
                resources.getString(resourceParamName),
                value,
                resources.getString(resourceParamDimensionName));
    }

    private String getFullParameterString(String paramName, String paramValue, String paramDimension) {
        return String.format("%s: %s %s", paramName, paramValue, paramDimension);
    }
}