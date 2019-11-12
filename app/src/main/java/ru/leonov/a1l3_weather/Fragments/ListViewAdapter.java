package ru.leonov.a1l3_weather.Fragments;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ru.leonov.a1l3_weather.Database.CitiesTable;
import ru.leonov.a1l3_weather.R;


public class ListViewAdapter extends BaseAdapter {
    private List<String> elements;
    private Context context;
    private LayoutInflater layoutInflater;
    private SQLiteDatabase database;

    ListViewAdapter(Context context, SQLiteDatabase database) {
        this.context = context;
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
        elements.add(name);
        CitiesTable.addCity(name, database);
        notifyDataSetChanged();
    }

    void deleteCity(int index) {
        if(elements.size() >= index) {
            String name = elements.get(index);
            elements.remove(index);
            CitiesTable.deleteCity(name, database);
            notifyDataSetChanged();
        }
    }

//    void editElement() {
//        if(elements.size() > 0) {
//            NotesTable.editNote(elements.get(elements.size() - 1), 100, database);
//            elements.set(elements.size() - 1, 100);
//            notifyDataSetChanged();
//        }
//    }

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

        String text = context.getString(R.string.cityStr);
        TextView textView = view.findViewById(R.id.city_text);
        textView.setText(text);

        return view;
    }
}
