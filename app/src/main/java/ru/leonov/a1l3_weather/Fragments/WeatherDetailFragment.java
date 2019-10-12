package ru.leonov.a1l3_weather.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.leonov.a1l3_weather.Data.DataSource;
import ru.leonov.a1l3_weather.Data.WeatherDataSource;
import ru.leonov.a1l3_weather.R;

public class WeatherDetailFragment extends Fragment{

    private static final String TAG = "WEATHER";
    private static final String CITY_VALUE_KEY = "cityKey";

    private RecyclerView recyclerView;


    static WeatherDetailFragment create(int index) {
        WeatherDetailFragment fragment = new WeatherDetailFragment();

        Bundle args = new Bundle();
        args.putInt(CITY_VALUE_KEY, index);
        fragment.setArguments(args);

        return fragment;
    }

    // Получить индекс из списка (фактически из параметра)
    int getIndex() {
        int index = 0;
        if (getArguments() != null) {
            index = getArguments().getInt(CITY_VALUE_KEY, 0);
        }

        return index;
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
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(source.getDataSource(getIndex()));

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    //region для задания
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        Log.d(TAG, this.getClass().getName() + " - onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, this.getClass().getName() + " - onCreate");
    }

    @Override
    public void onStart() {
        super.onStart();

        Log.d(TAG, this.getClass().getName() + " - onStart");
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d(TAG, this.getClass().getName() + " - onResume");
    }

    @Override
    public void onPause() {
        super.onPause();

        Log.d(TAG, this.getClass().getName() + " - onPause");
    }

    @Override
    public void onStop() {
        super.onStop();

        Log.d(TAG, this.getClass().getName() + " - onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        Log.d(TAG, this.getClass().getName() + " - onDestroyView");
    }

    @Override
    public void onDetach() {
        super.onDetach();

        Log.d(TAG, this.getClass().getName() + " - onDestroyView");
    }
    //endregion

}
