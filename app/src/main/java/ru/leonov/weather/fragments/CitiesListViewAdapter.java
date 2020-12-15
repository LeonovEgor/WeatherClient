package ru.leonov.weather.fragments;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ru.leonov.weather.database.CitiesTable;
import ru.leonov.weather.R;


public class CitiesListViewAdapter extends BaseAdapter {
    private List<String> elements;
    private LayoutInflater layoutInflater;
    private SQLiteDatabase database;

    CitiesListViewAdapter(Context context, SQLiteDatabase database) {
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.database = database;

        elements = CitiesTable.getAllCities(database);
    }

    @Override
    public int getCount() {
        return elements.size();
    }

    @Override
    public Object getItem(int i) {
        return elements.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    void addNewCity(String name) {
        CitiesTable.addCity(name, database);
        elements.add(name);
        notifyDataSetChanged();
    }

    void deleteCity(String city) {
        CitiesTable.deleteCity(city, database);
        elements.remove(city);
        notifyDataSetChanged();
    }

    void editCityName(String oldCityName, String newCityName) {
            CitiesTable.editCityName(oldCityName, newCityName, database);
            int index = elements.indexOf(oldCityName);
            if (index != -1) {
                elements.set(index, newCityName);
            }

            notifyDataSetChanged();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null) {
            view = layoutInflater.inflate(R.layout.cities_list, viewGroup, false);
        }

        String text = elements.get(i);
        TextView textView = view.findViewById(R.id.city_text);
        textView.setText(text);

        return view;
    }
}
