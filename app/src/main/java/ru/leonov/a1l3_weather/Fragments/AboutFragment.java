package ru.leonov.a1l3_weather.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import ru.leonov.a1l3_weather.R;

public class AboutFragment extends Fragment {
    private static final String TAG = "WEATHER";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, this.getClass().getName() + " - onCreateView");

        return inflater.inflate(R.layout.fragment_about, container, false);
    }
}