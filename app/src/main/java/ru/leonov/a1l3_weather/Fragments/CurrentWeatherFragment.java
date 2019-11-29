package ru.leonov.a1l3_weather.Fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

import ru.leonov.a1l3_weather.Data.WeatherData;
import ru.leonov.a1l3_weather.R;
import ru.leonov.a1l3_weather.Requests.DataSource;
import ru.leonov.a1l3_weather.Requests.ResponseCallback;
import ru.leonov.a1l3_weather.Requests.WeatherDataSource;
import ru.leonov.a1l3_weather.Storages.Settings;
import ru.leonov.a1l3_weather.Storages.Storage;

import static android.content.Context.LOCATION_SERVICE;

public class CurrentWeatherFragment extends Fragment implements ResponseCallback {

    private static final String TAG = "WEATHER";
    private static final int PERMISSION_REQUEST_CODE = 10;

    private TextView providerView;
    private TextView geoView;
    private TextView cityView;
    private TextView temperatureView;

    private Settings settings;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_current_weather, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d(TAG, this.getClass().getName() + " - onViewCreated");

        initView();
        getSettings();
        showLastLocation();
        checkPermission();
    }

    private void showLastLocation() {
        if (settings.lastLat != 0.0 && settings.lastLong != 0.0) {
            getCurrentWeather(Double.toString(settings.lastLat), Double.toString(settings.lastLong));
        }
    }

    private void getSettings() {
        settings = Storage.loadSettings(Objects.requireNonNull(getActivity()));
    }

    private void initView() {
        FragmentActivity activity = getActivity();
        if (activity == null) throw new NullPointerException("Activity is null");

        providerView = activity.findViewById(R.id.providerView);
        geoView = activity.findViewById(R.id.geoView);
        cityView = activity.findViewById(R.id.cityView);
        temperatureView = activity.findViewById(R.id.temperatureView);
    }

    private void checkPermission() {
        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getActivity()),
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            requestLocation();
        } else {
            requestLocationPermissions();
        }
    }

    private void requestLocation() {
        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getActivity()),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;
        final LocationManager locationManager = (LocationManager) Objects.requireNonNull(getActivity()).getSystemService(LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);

        String provider = Objects.requireNonNull(locationManager).getBestProvider(criteria, true);
        if (provider != null) {

            try {
                Location location = locationManager.getLastKnownLocation(provider);
                showLocation(location);
            }
            catch(SecurityException e) {
                requestLocationPermissions();
            }

            provider = String.format(getActivity().getString(R.string.Provider), provider);
            providerView.setText(provider);

            locationManager.requestSingleUpdate(criteria, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    showLocation(location);
                    settings.lastLat = location.getLatitude();
                    settings.lastLong = location.getLongitude();
                    Storage.saveSettings(Objects.requireNonNull(getActivity()), settings);
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {}
                @Override
                public void onProviderEnabled(String provider) {}
                @Override
                public void onProviderDisabled(String provider) {}
            }, null);
        }
        else {
            providerView.setText(R.string.NoProviderFound);
        }
    }

    private void showLocation(Location location) {
        if (location == null) {
            return;
        }

        String latitude = Double.toString(location.getLatitude()); // широта
        String longitude = Double.toString(location.getLongitude()); // Долгота
        String accuracy = Float.toString(location.getAccuracy()); // Точность
        String text = String.format("%s, %s; %s", latitude, longitude, accuracy);
        geoView.setText(text);
        getCurrentWeather(latitude, longitude);
    }

    private void getCurrentWeather(String latitude, String longitude) {

        DataSource source = new WeatherDataSource(getResources());
        source.requestDataSourceByGeo(latitude, longitude, settings.units, this);

    }

    private void requestLocationPermissions() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(Objects.requireNonNull(getActivity()), Manifest.permission.CALL_PHONE)) {
            ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()),
                    new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length == 2 &&
                    (grantResults[0] == PackageManager.PERMISSION_GRANTED || grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                requestLocation();
            }
        }
    }

    @Override
    public void response(ArrayList<WeatherData> response) {
        if (response != null && response.size() > 0) {
            WeatherData data = response.get(0);
            temperatureView.setText(data.temperature);
            cityView.setText(data.city);
        }
    }

    @Override
    public void responseError(String error) {
        temperatureView.setText(error);
    }
}