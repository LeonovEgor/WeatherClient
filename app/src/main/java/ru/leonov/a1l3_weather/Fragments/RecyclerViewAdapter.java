package ru.leonov.a1l3_weather.Fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.leonov.a1l3_weather.Data.WeatherData;
import ru.leonov.a1l3_weather.R;
import ru.leonov.a1l3_weather.Storages.Settings;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<WeatherData> dataSource = new ArrayList<>();
    private final Settings settings;

    RecyclerViewAdapter(List<WeatherData> dataSource, Settings settings) {
        if(dataSource != null) {
            this.dataSource = dataSource;
        }
        this.settings = settings;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_list, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.tvCity.setText(dataSource.get(position).city);
        if (settings.showPressure) {
            holder.tvPressure.setText(dataSource.get(position).pressure);
        }
        else {
            holder.tvPressure.setVisibility(View.GONE);
        }

        if (settings.showHumidity) {
            holder.tvHumidity.setText(dataSource.get(position).humidity);
        }
        else {
            holder.tvHumidity.setVisibility(View.GONE);
        }

        if (settings.showWindSpeed) {
            holder.tvWindSpeed.setText(dataSource.get(position).windSpeed);
        }
        else {
            holder.tvWindSpeed.setVisibility(View.GONE);
        }

        holder.tvWeatherIcon.setText(dataSource.get(position).weatherIcon);
        holder.tvDateUpdate.setText(dataSource.get(position).updateDate);
        holder.tvTemperature.setText(dataSource.get(position).temperature);
    }

    @Override
    public int getItemCount() {
        return dataSource.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvCity;
        TextView tvPressure;
        TextView tvHumidity;
        TextView tvWindSpeed;
        TextView tvTemperature;
        TextView tvWeatherIcon;
        TextView tvDateUpdate;

        ViewHolder(View view) {
            super(view);

            initView(view);
            setFont();
        }

        private void initView(View view) {
            tvCity = view.findViewById(R.id.tvCity);
            tvPressure = view.findViewById(R.id.tvPressure);
            tvHumidity = view.findViewById(R.id.tvHumidity);
            tvWindSpeed = view.findViewById(R.id.tvWindSpeed);
            tvTemperature = view.findViewById(R.id.tvTemperature);
            tvWeatherIcon = view.findViewById(R.id.tvWeatherIcon);
            tvDateUpdate = view.findViewById(R.id.tvDate);
        }

        private void setFont() {
            Typeface weatherFont = Typeface.createFromAsset(itemView.getContext().getAssets(),
                    "fonts/weather.ttf");
            tvWeatherIcon.setTypeface(weatherFont);
        }
    }
}
