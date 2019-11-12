package ru.leonov.a1l3_weather.Fragments;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import ru.leonov.a1l3_weather.Database.CitiesTable;
import ru.leonov.a1l3_weather.R;


public class CitiesListViewAdapter extends BaseAdapter {
    //private List<String> elements;
    private Map<Long, String> elements;
    private Context context;
    private LayoutInflater layoutInflater;
    private SQLiteDatabase database;

    CitiesListViewAdapter(Context context, SQLiteDatabase database) {
        //this.context = context;
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
        return elements.get((long)i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    void addNewCity(String name) {
        long index = CitiesTable.addCity(name, database);
        elements.put(index, name);
        notifyDataSetChanged();
    }

    void deleteCity(long index) {
        if(elements.size() >= index) {
            CitiesTable.deleteCity(index, database);
            elements.remove(index);
            notifyDataSetChanged();
        }
    }

    void editCityName(long index, String newCityName) {
        if(elements.size() > 0) {
            CitiesTable.editCityName(index, newCityName, database);
            elements.put(index, newCityName);
            notifyDataSetChanged();
        }
    }

    void clearList() {
        elements.clear();
        CitiesTable.deleteAllCities(database);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null) {
            view = layoutInflater.inflate(R.layout.cities_list, viewGroup, false);
        }

        String text = elements.get((long)i);
        TextView textView = view.findViewById(R.id.city_text);
        textView.setText(text);

        return view;
    }
}
