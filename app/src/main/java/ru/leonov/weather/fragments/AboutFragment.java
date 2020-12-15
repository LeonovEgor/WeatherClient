package ru.leonov.weather.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import java.util.Objects;

import ru.leonov.weather.R;

public class AboutFragment extends Fragment {
    private static final String TAG = "WEATHER";
    private static final String PICTURE_URL = "https://geekbrains.ru/android-chrome-192x192.png";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, this.getClass().getName() + " - onCreateView");

        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();
    }

    private void initView() {
        ImageView imageView = Objects.requireNonNull(getActivity()).findViewById(R.id.imageView);
        Picasso.get().load(PICTURE_URL).error(R.drawable.geek_brains).into(imageView);
    }
}