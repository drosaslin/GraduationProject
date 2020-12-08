package com.example.android.weather;

import android.support.annotation.NonNull;
        import android.support.v7.widget.RecyclerView;
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

public class WeeklyDataAdapter extends RecyclerView.Adapter<WeeklyDataAdapter.ViewHolder> {
    private ArrayList<Forecast> forecasts;
    private HashMap<String, Integer> weatherIcons;

    public WeeklyDataAdapter(ArrayList<Forecast> newForecast) {
        forecasts = newForecast;
        weatherIcons = new HashMap<>();
        populateIcons();
    }

    @NonNull
    @Override
    public WeeklyDataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_daily_template, parent, false);
        return new WeeklyDataAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WeeklyDataAdapter.ViewHolder holder, int position) {
        String time = forecasts.get(position).getFormattedTime();
        String day = time.substring(0, time.indexOf(" "));
        String tempHigh = Integer.toString(Math.round(forecasts.get(position).getTemperatureHigh())) + "°";
        String tempLow = Integer.toString(Math.round(forecasts.get(position).getTemperatureLow())) + "°";
        String icon = forecasts.get(position).getIcon();

        holder.day.setText(day);
        holder.tempHigh.setText(tempHigh);
        holder.tempLow.setText(tempLow);
        holder.weatherIcon.setImageResource(weatherIcons.get(icon));
    }

    @Override
    public int getItemCount() {
        return forecasts.size();
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
        TextView day;
        TextView tempHigh;
        TextView tempLow;
        ImageView weatherIcon;

        public ViewHolder(View itemView) {
            super(itemView);

            day = (itemView).findViewById(R.id.day);
            tempHigh= (itemView).findViewById(R.id.temp_high);
            tempLow = (itemView).findViewById(R.id.temp_low);
            weatherIcon = (itemView).findViewById(R.id.weather);
        }
    }
}
