package ru.leonov.a1l3_weather.Fragments;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.Objects;

import ru.leonov.a1l3_weather.Data.CityHelper;
import ru.leonov.a1l3_weather.R;


public class SelectCityFragment extends Fragment {
    private static final String TAG = "WEATHER";
    private static final String CITY_VALUE_KEY = "cityKey";
    private static final String BACK_STACK_KEY = "backStackKey";

    private boolean isExistWeatherDetail;  // Можно ли расположить рядом фрагмент данными погоды
    private int currentPosition = 0;    // Текущая позиция (выбранный город)

    private ListView listView;
    private TextView emptyTextView;
    private ArrayAdapter<String> adapter;

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
        initList();
    }

    private void initViews(View view) {
        listView = view.findViewById(R.id.cities_list_view);
        emptyTextView = view.findViewById(R.id.cities_list_empty_view);
    }

    private void initList() {
        adapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()),
                android.R.layout.simple_list_item_activated_1);

        listView.setAdapter(adapter);
        listView.setEmptyView(emptyTextView);
        registerForContextMenu(listView);

        adapter.addAll(CityHelper.getCityList());

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick");
                currentPosition = position;
                showWeatherDetail();
            }
        });
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

        switch (id) {
            case R.id.action_add: {
                addItem();
                break;
            }

            case R.id.action_edit: {
                editItem(item);
                break;
            }
            case R.id.action_remove: {
                removeItem(item);
                break;
            }
        }
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
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                adapter.add(input.getText().toString());
            }
        });
        builder.show();
    }

    private void editItem(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        editInputDialog(info.position);
    }

    private void editInputDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        builder.setTitle(R.string.add_city);
        final int finalPosition = position;
        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(adapter.getItem(position));
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String sItem = adapter.getItem(finalPosition);
                adapter.remove(sItem);
                adapter.insert(input.getText().toString(), finalPosition);
            }
        });
        builder.show();
    }

    private void removeItem(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        String sItem = adapter.getItem(info.position);
        adapter.remove(sItem);
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
}
