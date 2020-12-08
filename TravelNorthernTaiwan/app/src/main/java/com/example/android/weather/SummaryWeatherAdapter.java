package com.example.android.weather;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.travelnortherntaiwan.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by David Rosas on 8/13/2018.
 */

public class SummaryWeatherAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Serializable {
    ArrayList<WeatherData> weatherData;
    HashMap<String, Integer> weatherIcons;
    Context context;

    public SummaryWeatherAdapter(ArrayList<WeatherData> newData, Context newContext){
        context = newContext;
        weatherData = newData;
        weatherIcons = new HashMap<>();
        populateIcons();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == R.layout.weather_list_main_row) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_list_main_row, parent, false);
            return new CurrentPositionViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weahter_province_list, parent, false);
            return new ProvinceViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        String temperature = Integer.toString(Math.round(weatherData.get(position).getCurrently().getTemperature())) + "°";
        String icon = weatherData.get(position).getCurrently().getIcon();

        if(holder instanceof ProvinceViewHolder) {
            ((ProvinceViewHolder)holder).province.setText(weatherData.get(position).getCity());
            ((ProvinceViewHolder)holder).temp.setText(temperature);
            ((ProvinceViewHolder)holder).weather.setImageResource(weatherIcons.get(icon));
        }
        else if(holder instanceof CurrentPositionViewHolder) {
            double precip = weatherData.get(position).getCurrently().getPrecipProbability() * 100;
            String precipitation = Integer.toString((int)precip) + "%";
            String windSpeed = Integer.toString((int)weatherData.get(position).getCurrently().getWindSpeed()) + " km/h";

            ((CurrentPositionViewHolder)holder).place.setText(weatherData.get(position).getCity());
            ((CurrentPositionViewHolder)holder).temp.setText(temperature);
            ((CurrentPositionViewHolder)holder).precipitationProb.setText(precipitation);
            ((CurrentPositionViewHolder)holder).windSpeed.setText(windSpeed);
            ((CurrentPositionViewHolder)holder).weather.setImageResource(weatherIcons.get(icon));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("WeatherData", weatherData.get(position));
                Intent intent = new Intent(context, HourlyDataActivity.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0) {
            return R.layout.weather_list_main_row;
        }

        return R.layout.weahter_province_list;
    }

    @Override
    public int getItemCount() {
        return weatherData.size();
    }

    private void populateIcons() {
        weatherIcons.put("clear-day", R.drawable.weather_wi_day_sunny);
        weatherIcons.put("clear-night", R.drawable.weather_wi_night_clear);
        weatherIcons.put("partly-cloudy-day", R.drawable.weather_wi_day_cloudy);
        weatherIcons.put("partly-cloudy-night", R.drawable.weather_wi_night_partly_cloudy);
        weatherIcons.put("cloudy", R.drawable.weather_wi_cloudy);
        weatherIcons.put("rain", R.drawable.weather_wi_day_rain);
        weatherIcons.put("sleet", R.drawable.weather_wi_day_sleet);
        weatherIcons.put("snow", R.drawable.weather_wi_day_snow);
        weatherIcons.put("wind", R.drawable.weather_wi_day_windy);
        weatherIcons.put("fog", R.drawable.weather_wi_day_fog);
    }

    public class ProvinceViewHolder extends RecyclerView.ViewHolder {
        TextView province;
        TextView temp;
        ImageView weather;

        public ProvinceViewHolder(View itemView) {
            super(itemView);

            province = itemView.findViewById(R.id.province_name);
            temp = itemView.findViewById(R.id.temp);
            weather = itemView.findViewById(R.id.weather);
        }
    }

    public class CurrentPositionViewHolder extends RecyclerView.ViewHolder {
        TextView place;
        TextView temp;
        TextView precipitationProb;
        TextView windSpeed;
        ImageView weather;

        public CurrentPositionViewHolder(View itemView) {
            super(itemView);

            place = itemView.findViewById(R.id.current_name);
            temp = itemView.findViewById(R.id.current_temperature);
            precipitationProb = itemView.findViewById(R.id.precipitation_prob);
            windSpeed = itemView.findViewById(R.id.wind_speed);
            weather = itemView.findViewById(R.id.current_weather);
        }
    }
}
