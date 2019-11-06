package ru.leonov.a1l3_weather.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;

import ru.leonov.a1l3_weather.Requests.DataSource;
import ru.leonov.a1l3_weather.Requests.ResponseCallback;
import ru.leonov.a1l3_weather.Data.WeatherData;
import ru.leonov.a1l3_weather.Requests.WeatherDataSource;
import ru.leonov.a1l3_weather.R;
import ru.leonov.a1l3_weather.Storages.Settings;

public class WeatherDetailFragment extends Fragment implements ResponseCallback {

    private static final String TAG = "WEATHER";
    private static final String CITY_VALUE_KEY = "CITY";
    private static final String SETTINGS_VALUE_KEY = "SETTINGS";

    private RecyclerView recyclerView;
    private TextView listEmptyView;

    static WeatherDetailFragment create(String city, Settings settings) {
        WeatherDetailFragment fragment = new WeatherDetailFragment();

        Bundle args = new Bundle();
        args.putString(CITY_VALUE_KEY, city);
        args.putSerializable(SETTINGS_VALUE_KEY, settings);
        fragment.setArguments(args);

        return fragment;
    }

    // Получить город из списка (фактически из параметра)
    private String getCity() {
        String city = "";
        if (getArguments() != null) {
            city = getArguments().getString(CITY_VALUE_KEY, "No_City_Found");
        }

        return city;
    }

    private Settings getSettings() {
        Settings settings = null;
        if (getArguments() != null) {
            settings = (Settings)getArguments().getSerializable(SETTINGS_VALUE_KEY);
        }

        return settings;
    }

    @Override
    @SuppressLint("Recycle")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, this.getClass().getName() + " - onCreateView");

        return inflater.inflate(R.layout.fragment_weather_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d(TAG, this.getClass().getName() + " - onViewCreated");

        initViews(view);
        initRecyclerView();
    }

    private void initViews(View view) {
        listEmptyView = view.findViewById(R.id.list_empty_view);
        recyclerView = view.findViewById(R.id.recyclerView);
    }

    private void initRecyclerView() {
        FragmentActivity activity = getActivity();
        if (activity == null) {
            Log.d(TAG, "initRecyclerView - Activity is detached");
            return;
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(activity.getBaseContext());
        recyclerView.setLayoutManager(layoutManager);

        DataSource source = new WeatherDataSource(getResources());
        source.requestDataSource(getCity(), getSettings().isCelsius, this);
    }

    @Override
    public void response(ArrayList<WeatherData> response) {
        if(response == null) {
            Toast.makeText(Objects.requireNonNull(getActivity()).getBaseContext(),
                    R.string.place_not_found,
                    Toast.LENGTH_LONG).show();
            return;
        }
        listEmptyView.setVisibility(View.GONE);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(response, getSettings());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void responseError(String error) {
        listEmptyView.setText(error);
    }
}