package ru.leonov.a1l3_weather.Data;

import android.content.res.Resources;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import ru.leonov.a1l3_weather.R;

public class WeatherDataSource implements DataSource{

    private final static String LOG_TAG = WeatherDataSource.class.getSimpleName();

    private Resources resources;

    public WeatherDataSource(Resources res) {
        this.resources = res;
    }

    @Override
    public void requestDataSource(String city, ResponseCallback callback) {
        updateWeatherData(city, callback);
    }

    private void updateWeatherData(final String city, final ResponseCallback callback) {

        Thread requestThread = new Thread() {
            @Override
            public void run() {
                final JSONObject jsonObject = WeatherDataLoader.getJSONData(city);
                if(jsonObject != null) {
                    final ArrayList<WeatherData> data = renderWeather(jsonObject);
                    callback.response(data);
                }
                else callback.response(null);
            }
        };
        requestThread.start();

        //TODO: По моему это не нужно!!!
        try {
            requestThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<WeatherData> renderWeather(JSONObject jsonObject) {
        Log.d(LOG_TAG, "json: " + jsonObject.toString());
        ArrayList<WeatherData> list = new ArrayList<>();

        try {
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
            list.add(new WeatherData(
                    resources.getString(R.string.error),
                    resources.getString(R.string.error),
                    resources.getString(R.string.error),
                    resources.getString(R.string.error),
                    resources.getString(R.string.error),
                    resources.getString(R.string.error),
                    resources.getString(R.string.error)));
            exc.printStackTrace();
            Log.e(LOG_TAG, "One or more fields not found in the JSON data");
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
                        resources.getString(R.string.celsiusDimension));
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
                icon = "\u2600";
                //icon = getString(R.string.weather_sunny);
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
                    icon = "\u2601";
                    // icon = getString(R.string.weather_cloudy);
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
