package ru.leonov.a1l3_weather.Fragments;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
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

import ru.leonov.a1l3_weather.Data.WeatherData;
import ru.leonov.a1l3_weather.R;
import ru.leonov.a1l3_weather.Requests.RequestService;

public class WeatherDetailFragment extends Fragment {

    private static final String TAG = "WEATHER";
    private static final String CITY_VALUE_KEY = "CITY";

    private ServiceFinishedReceiver receiver = new ServiceFinishedReceiver();

    private RecyclerView recyclerView;
    private TextView listEmptyView;

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

        startService();
        registerService();
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

//        DataSource source = new WeatherDataSource(getResources());
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity.getBaseContext());
        recyclerView.setLayoutManager(layoutManager);
//        source.requestDataSource(getCity(),this);
    }

    private void startService() {
        FragmentActivity activity = getActivity();
        if (activity == null) {
            Log.d(TAG, "startService - Activity is detached");
            return;
        }

        Intent intent = new Intent(activity.getApplicationContext(), RequestService.class);
        intent.putExtra(RequestService.CITY, getCity());
        getActivity().startService(intent);

    }
    private void registerService() {
        Objects.requireNonNull(getActivity()).registerReceiver(receiver,
                new IntentFilter(RequestService.ACTION_REQUEST_SERVICE));
    }

    @Override
    public void onPause() {
        unregisterService();

        super.onPause();
    }

    private void unregisterService() {
        Objects.requireNonNull(getActivity()).unregisterReceiver(receiver);
    }

    @SuppressWarnings("unchecked")
    private class ServiceFinishedReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            final ArrayList<WeatherData> response =
                    (ArrayList<WeatherData>) intent.getSerializableExtra(RequestService.WEATHER_KEY);

            Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(response == null) {
                        Toast.makeText(Objects.requireNonNull(getActivity()).getBaseContext(),
                                R.string.place_not_found,
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                    listEmptyView.setVisibility(View.GONE);
                    RecyclerViewAdapter adapter = new RecyclerViewAdapter(response);
                    recyclerView.setAdapter(adapter);
                }
            });
        }
    }
}
