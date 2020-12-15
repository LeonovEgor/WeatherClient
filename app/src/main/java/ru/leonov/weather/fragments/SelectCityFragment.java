package ru.leonov.weather.fragments;

import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

import ru.leonov.weather.database.DatabaseHelper;
import ru.leonov.weather.R;
import ru.leonov.weather.sensorsView.SensorHelper;
import ru.leonov.weather.sensorsView.SensorView;

import static android.content.Context.SENSOR_SERVICE;

public class SelectCityFragment extends Fragment {
    private static final String TAG = "WEATHER";
    private static final String CITY_VALUE_KEY = "cityKey";
    private static final String BACK_STACK_KEY = "backStackKey";

    private int currentPosition = 0;    // Текущая позиция (выбранный город)

    private SQLiteDatabase database;

    private SensorView currentTemperature;
    private SensorView currentHumidity;
    private SensorView currentPressure;
    private ListView listView;
    private CitiesListViewAdapter adapter;
    private FloatingActionButton fab;

    private SensorManager sensorManager;
    private Sensor sensorTemperature;
    private Sensor sensorHumidity;
    private Sensor sensorPressure;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, this.getClass().getName() + " - onCreateView");

        return inflater.inflate(R.layout.fragment_select_city, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d(TAG, this.getClass().getName() + " - onViewCreated");

        initViews(view);
        initDB();
        initList(view);
        initFloatingBtn();
        getSensors(view);
        setCurrentWeatherFragment();
    }

    private void setCurrentWeatherFragment() {
        FragmentManager manager = getFragmentManager();
        if (manager == null) {
            Log.e(TAG, "Ошибка смены Fragment");
            return;
        }

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.currentWeatherFrame,  new CurrentWeatherFragment())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    private void initViews(View view) {
        currentTemperature = view.findViewById(R.id.currentTemperature);
        currentHumidity = view.findViewById(R.id.currentHumidity);
        currentPressure = view.findViewById(R.id.currentPressure);
    }

    private void initDB() {
        database = new DatabaseHelper(
                Objects.requireNonNull(getContext()).getApplicationContext()
        ).getWritableDatabase();
    }

    private void initList(View view) {
        listView = view.findViewById(R.id.cities_list_view);
        TextView emptyTextView = view.findViewById(R.id.cities_list_empty_view);

        adapter = new CitiesListViewAdapter(Objects.requireNonNull(getContext()), database);
        listView.setAdapter(adapter);
        listView.setEmptyView(emptyTextView);
        registerForContextMenu(listView);

        listView.setOnItemClickListener((parent, view1, position, id) -> {
            Log.d(TAG, "onItemClick");
            currentPosition = position;
            showWeatherDetail();
        });
    }

    private void initFloatingBtn() {
        fab = Objects.requireNonNull(getActivity()).findViewById(R.id.fab);
        fab.show();
        fab.setOnClickListener(view -> addItem());
    }

    private void getSensors(View view) {
        sensorManager = (SensorManager) view.getContext().getSystemService(SENSOR_SERVICE);
        if (sensorManager == null) {
            Log.w(TAG, "No sensors found!");
            return;
        }

        sensorTemperature = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        sensorHumidity = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        sensorPressure = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.d(TAG, this.getClass().getName() + " - onActivityCreated");

        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getInt(CITY_VALUE_KEY, 0);
        }
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        Objects.requireNonNull(getActivity()).getMenuInflater().inflate(R.menu.menu_context, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        handleMenuItemClick(item);
        return super.onContextItemSelected(item);
    }

    private void handleMenuItemClick(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add) addItem();
        else if (id == R.id.action_edit) editItem(item);
        else if (id == R.id.action_remove) removeItem(item);
    }

    private void addItem() {
        addInputDialog();
    }

    private void addInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        builder.setTitle(R.string.add_city);

        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("OK", (dialog, which) -> adapter.addNewCity(input.getText().toString()));
        builder.show();
    }

    private void editItem(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        editInputDialog(info.position);
    }

    private void editInputDialog(int position) {
        final int finalPosition = position;
        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText((String)adapter.getItem(position));

        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        builder.setTitle(R.string.edit_city);
        builder.setView(input);
        builder.setPositiveButton("OK",
                (dialog, which) -> adapter.editCityName((String)adapter.getItem(finalPosition), input.getText().toString()));
        builder.show();
    }

    private void removeItem(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        adapter.deleteCity((String)adapter.getItem(info.position));
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Log.d(TAG, this.getClass().getName() + " - onSaveInstanceState");

        outState.putInt(CITY_VALUE_KEY, currentPosition);
        super.onSaveInstanceState(outState);
    }

    private void showWeatherDetail() {
        listView.setItemChecked(currentPosition, true);
        String city = (String) listView.getAdapter().getItem(currentPosition);
        WeatherDetailFragment detail = WeatherDetailFragment.create(city);

        FragmentManager manager = getFragmentManager();
        if (manager == null) {
            Log.e(TAG, "Ошибка смены Fragment");
            return;
        }

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.container, detail)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack(BACK_STACK_KEY)
                .commit();
    }

    @Override
    public void onResume() {
        super.onResume();

        registerSensors();
    }

    private void registerSensors() {
        if (sensorTemperature != null)
            sensorManager.registerListener(listenerTemperature, sensorTemperature,
                    SensorManager.SENSOR_DELAY_NORMAL);
        else {
            currentTemperature.setVisibility(View.GONE);
        }
        if (sensorHumidity != null)
            sensorManager.registerListener(listenerHumidity, sensorHumidity,
                    SensorManager.SENSOR_DELAY_NORMAL);
        else {
            currentHumidity.setVisibility(View.GONE);
        }
        if (sensorPressure != null)
            sensorManager.registerListener(listenerPressure, sensorPressure,
                    SensorManager.SENSOR_DELAY_NORMAL);
        else {
            currentPressure.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPause() {
        unregisterSensors();
        fab.hide();
        super.onPause();
    }

    private void unregisterSensors() {
        if (sensorTemperature != null)
            sensorManager.unregisterListener(listenerTemperature, sensorTemperature);
        if (sensorHumidity != null)
            sensorManager.unregisterListener(listenerHumidity, sensorHumidity);
        if (sensorPressure != null)
            sensorManager.unregisterListener(listenerPressure, sensorPressure);
    }

    private final SensorEventListener listenerTemperature = new SensorEventListener() {

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}

        @Override
        public void onSensorChanged(SensorEvent event) {
            int percent = Math.round(
                    100f/SensorHelper.temperatureScale
                            *(event.values[0]+SensorHelper.temperatureScaleMinus));
            update(currentTemperature, event.values[0], percent);
        }
    };

    private void update(SensorView view, float value, int percent) {
        int iValue = Math.round(value);
        view.setParamValue(String.valueOf(iValue));
        view.setProgressPercent(percent);
    }

    private final SensorEventListener listenerHumidity = new SensorEventListener() {

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}

        @Override
        public void onSensorChanged(SensorEvent event) {
            int percent = Math.round(event.values[0]);
            update(currentHumidity, event.values[0], percent);
        }
    };

    private final SensorEventListener listenerPressure = new SensorEventListener() {

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}

        @Override
        public void onSensorChanged(SensorEvent event) {
            int percent = Math.round(100f/ SensorHelper.pressureScale*event.values[0]);
            update(currentPressure, event.values[0], percent);
        }
    };

}