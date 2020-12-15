package ru.leonov.weather.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.button.MaterialButton;

import java.util.Objects;

import ru.leonov.weather.R;
import ru.leonov.weather.storages.Settings;
import ru.leonov.weather.storages.Storage;

public class SettingsFragment extends Fragment {
    private static final String TAG = "WEATHER";

    private Settings settings;
    private CheckBox cbShowPressure;
    private CheckBox cbShowHumidity;
    private CheckBox cbShowWindSpeed;
    private RadioButton rbIsCelsius;
    private RadioButton rbIsFahrenheit;

    private MaterialButton btnSave;
    private MaterialButton btnCancel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, this.getClass().getName() + " - onCreateView");
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d(TAG, this.getClass().getName() + " - onViewCreated");

        initButtons(view);
        loadSettings();
        setBehaviourForCloseActBtn();
        setBehaviourForSaveActBtn();
    }

    private void initButtons(View view) {
        cbShowPressure = view.findViewById(R.id.cbPressure);
        cbShowHumidity = view.findViewById(R.id.cbHumidity);
        cbShowWindSpeed = view.findViewById(R.id.cbWindSpeed);
        rbIsCelsius = view.findViewById(R.id.rbCelsius);
        rbIsFahrenheit = view.findViewById(R.id.rbFahrenheit);
        btnSave = view.findViewById(R.id.btnSave);
        btnCancel = view.findViewById(R.id.btnCancel);
    }

    private void loadSettings() {
        settings = Storage.loadSettings(Objects.requireNonNull(getActivity()));
        cbShowPressure.setChecked(settings.showPressure);
        cbShowHumidity.setChecked(settings.showHumidity);
        cbShowWindSpeed.setChecked(settings.showWindSpeed);
        rbIsCelsius.setChecked(settings.units.equals(Settings.METRIC));
        rbIsFahrenheit.setChecked(settings.units.equals(Settings.IMPERIAL));
    }

    private void setBehaviourForSaveActBtn() {
        btnSave.setOnClickListener(view -> saveSettings());
    }

    private void saveSettings() {

        settings.showPressure = cbShowPressure.isChecked();
        settings.showHumidity = cbShowHumidity.isChecked();
        settings.showWindSpeed = cbShowWindSpeed.isChecked();
        settings.units = rbIsCelsius.isChecked() ? Settings.METRIC: Settings.IMPERIAL;

        Storage.saveSettings(Objects.requireNonNull(getActivity()), settings);

        FragmentManager manager = getFragmentManager();
        if (manager != null)
            getFragmentManager().popBackStack();
    }

    private void setBehaviourForCloseActBtn() {
        btnCancel.setOnClickListener(view -> {
            FragmentManager manager = getFragmentManager();
            if (manager != null)
                getFragmentManager().popBackStack();
        });
    }

}