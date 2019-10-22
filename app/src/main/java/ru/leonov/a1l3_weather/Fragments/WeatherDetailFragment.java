package ru.leonov.a1l3_weather.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;

import ru.leonov.a1l3_weather.Data.DataSource;
import ru.leonov.a1l3_weather.Data.ResponseCallback;
import ru.leonov.a1l3_weather.Data.WeatherData;
import ru.leonov.a1l3_weather.Data.WeatherDataSource;
import ru.leonov.a1l3_weather.R;

public class WeatherDetailFragment extends Fragment implements ResponseCallback {

    private static final String TAG = "WEATHER";
    private static final String CITY_VALUE_KEY = "cityKey";

    private RecyclerView recyclerView;
    private final Handler handler = new Handler();

    static WeatherDetailFragment create(String city) {
        WeatherDetailFragment fragment = new WeatherDetailFragment();

        Bundle args = new Bundle();
        args.putString(CITY_VALUE_KEY, city);
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
        recyclerView = view.findViewById(R.id.recyclerView);
    }

    private void initRecyclerView() {
        FragmentActivity activity = getActivity();
        if (activity == null) {
            Log.d(TAG, "initRecyclerView - Activity is detached");
            return;
        }

        DataSource source = new WeatherDataSource(getResources());
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity.getBaseContext());
        recyclerView.setLayoutManager(layoutManager);
        source.requestDataSource(getCity(),this);
    }

    @Override
    public void response(final ArrayList<WeatherData> response) {
        if(response == null) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(Objects.requireNonNull(getActivity()).getBaseContext(),
                            R.string.place_not_found,
                            Toast.LENGTH_LONG).show();
                }
            });
        } else {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    RecyclerViewAdapter adapter = new RecyclerViewAdapter(response);
                    recyclerView.setAdapter(adapter);
                }
            });
        }
    }
}
