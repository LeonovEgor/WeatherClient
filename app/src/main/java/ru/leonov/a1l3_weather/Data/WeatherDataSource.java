package ru.leonov.a1l3_weather.Data;

import android.content.res.Resources;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import ru.leonov.a1l3_weather.R;

public class WeatherDataSource implements DataSource{
    private final Handler handler = new Handler();
    private final static String LOG_TAG = WeatherDataSource.class.getSimpleName();

    private Resources resources;

    public WeatherDataSource(Resources res) {
        this.resources = res;
    }
    @Override
    public List<WeatherData> getDataSource(int cityIndex) {
        String city = resources.getStringArray(R.array.cities)[cityIndex];
        RawWeatherData[] rawData = updateWeatherData(city);
        List<WeatherData> list = new ArrayList<>(rawData.length);
        for(int i=0; i<rawData.length; i++) {
            list.add(
                new WeatherData(
                    city,
                    GetParam(R.string.pressure, new Random().nextInt(1000), R.string.pressureDimension),
                    GetParam(R.string.humidity, new Random().nextInt(100), R.string.humidityDimension),
                    GetParam(R.string.windSpeed, new Random().nextInt(40), R.string.windSpeedDimension),
                    GetParam(R.string.temperature, new Random().nextInt(50), R.string.celsiusDimension),
                    "" /*dayOfWeek[i]*/
                )
            );
        }

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

    private RawWeatherData[] updateWeatherData(final String city) {
        RawWeatherData[] rawData = new RawWeatherData[16];

        Thread requestThread = new Thread() {
            @Override
            public void run() {
                final JSONObject jsonObject = WeatherDataLoader.getJSONData(city);
                if(jsonObject == null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(null, R.string.place_not_found,
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            rawData = renderWeather(jsonObject);
                        }
                    });
                }
            }
        };
        requestThread.start();
        try {
            requestThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //TODO: fix it
        return rawData;
    }

    private RawWeatherData[] renderWeather(JSONObject jsonObject) {
        Log.d(LOG_TAG, "json: " + jsonObject.toString());
        try {
            JSONObject details = jsonObject.getJSONArray("weather").getJSONObject(0);
            JSONObject main = jsonObject.getJSONObject("main");

            setPlaceName(jsonObject);
            setDetails(details, main);
            setCurrentTemp(main);
            setUpdatedText(jsonObject);
            setWeatherIcon(details.getInt("id"),
                    jsonObject.getJSONObject("sys").getLong("sunrise") * 1000,
                    jsonObject.getJSONObject("sys").getLong("sunset") * 1000);
        } catch (Exception exc) {
            exc.printStackTrace();
            Log.e(LOG_TAG, "One or more fields not found in the JSON data");
        }
    }

    private void setPlaceName(JSONObject jsonObject) throws JSONException {
        String cityText = jsonObject.getString("name").toUpperCase() + ", "
                + jsonObject.getJSONObject("sys").getString("country");
        cityTextView.setText(cityText);
    }

    private void setDetails(JSONObject details, JSONObject main) throws JSONException {
        String detailsText = details.getString("description").toUpperCase() + "\n"
                + "Humidity: " + main.getString("humidity") + "%" + "\n"
                + "Pressure: " + main.getString("pressure") + "hPa";
        detailsTextView.setText(detailsText);
    }

    private void setCurrentTemp(JSONObject main) throws JSONException {
        String currentTextText = String.format(Locale.getDefault(), "%.2f",
                main.getDouble("temp")) + "\u2103";
        currentTemperatureTextView.setText(currentTextText);
    }

    private void setUpdatedText(JSONObject jsonObject) throws JSONException {
        DateFormat dateFormat = DateFormat.getDateTimeInstance();
        String updateOn = dateFormat.format(new Date(jsonObject.getLong("dt") * 1000));
        String updatedText = "Last update: " + updateOn;
        updatedTextView.setText(updatedText);
    }

    private void setWeatherIcon(int actualId, long sunrise, long sunset) {
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
        weatherIconTextView.setText(icon);
    }


}
