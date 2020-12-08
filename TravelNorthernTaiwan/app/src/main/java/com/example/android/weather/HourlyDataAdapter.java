package com.example.android.weather;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.travelnortherntaiwan.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by David Rosas on 8/23/2018.
 */

public class HourlyDataAdapter extends RecyclerView.Adapter<HourlyDataAdapter.ViewHolder> {
    private ArrayList<Forecast> forecasts;
    private HashMap<String, Integer> weatherIcons;


    public HourlyDataAdapter(ArrayList<Forecast> newForecast) {
        forecasts = newForecast;
        weatherIcons = new HashMap<>();
        populateIcons();
    }

    @NonNull
    @Override
    public HourlyDataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_hourly_template, parent, false);
        return new HourlyDataAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HourlyDataAdapter.ViewHolder holder, int position) {
        String hour = getTwelveHourFormat(forecasts.get(position).getFormattedTime());
        String temperature = Integer.toString(Math.round(forecasts.get(position).getTemperature())) + "°";
        String icon = forecasts.get(position).getIcon();

        holder.time.setText(hour);
        holder.temperature.setText(temperature);
        holder.weatherIcon.setImageResource(weatherIcons.get(icon));
    }

    @Override
    public int getItemCount() {
        return forecasts.size();
    }

    private String getTwelveHourFormat(String time) {
        //get the whole time string and turns it into twelve hour format
        boolean morning = true;
        String sHour = time.substring(time.lastIndexOf(' ') + 1, time.indexOf(':'));
        int iHour = 0;

        try{
            //converting to twelve hour format
            iHour = Integer.parseInt(sHour);
            morning = (iHour < 12);
            if(iHour > 12) {
                iHour -= 12;
            }
            else if(iHour == 0) {
                iHour = 12;
            }
            Log.i("HOUR", Integer.toString(iHour));
        }
        catch (NumberFormatException e) {
            //do nothing
        }

        sHour = Integer.toString(iHour);
        String timeOfDay = (morning) ? "am" : "pm";
        return sHour + timeOfDay;
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView time;
        TextView temperature;
        ImageView weatherIcon;

        public ViewHolder(View itemView) {
            super(itemView);

            time = (itemView).findViewById(R.id.hour);
            temperature = (itemView).findViewById(R.id.temperature);
            weatherIcon = (itemView).findViewById(R.id.weather_icon);
        }
    }
}
