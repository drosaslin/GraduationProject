package com.example.android.my_trip;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.android.travelnortherntaiwan.R;
import com.example.android.weather.HourlyDataAdapter;
import com.example.android.weather.WeatherData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class MyTripWeather extends AppCompatActivity {
    private WeatherData weatherData;
    private String tripRegion;

    private RecyclerView recycler;
    private HourlyDataAdapter adapter;

    private TextView tripDate;
    private TextView temperature;
    private TextView precipitation;
    private TextView windSpeed;
    private ImageView weatherType;

    HashMap<String, Integer> weatherIcons;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_trip_weather_activity);

        Bundle data = getIntent().getExtras();
        tripRegion = getIntent().getStringExtra("region");

        tripDate = findViewById(R.id.trip_date);
        temperature = findViewById(R.id.temperature);
        precipitation = findViewById(R.id.precipitation_prob);
        windSpeed = findViewById(R.id.wind_speed);
        weatherType = findViewById(R.id.weather_icon);
        recycler = findViewById(R.id.hourly_recycler);

        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false));

        weatherIcons = new HashMap<>();

        if(data != null) {
            weatherData = data.getParcelable("WeatherData");
            adapter = new HourlyDataAdapter(weatherData.getHourly().getData());
            recycler.setAdapter(adapter);
        }
        else {
            weatherData = null;
        }

        getSupportActionBar().setTitle(tripRegion + " Weather");
        populateIcons();
        updateUI();
    }

    private void updateUI() {
        double precip = weatherData.getCurrently().getPrecipProbability() * 100;
        String formattedPrecipitation = Integer.toString((int)precip) + "%";
        String formattedTemperature = Integer.toString(Math.round(weatherData.getCurrently().getTemperature())) + "°";
        String formattedWindSpeed = Integer.toString((int)weatherData.getCurrently().getWindSpeed()) + " km/h";
        String icon = weatherData.getCurrently().getIcon();
        String formattedDate = getFormattedDate();

        precipitation.setText(formattedPrecipitation);
        windSpeed.setText(formattedWindSpeed);
        temperature.setText(formattedTemperature);
        tripDate.setText(formattedDate);
        weatherType.setImageResource(weatherIcons.get(icon));
    }

    private String getFormattedDate() {
        SimpleDateFormat oldFormat = new SimpleDateFormat("EEEE yyyy-MM-dd HH:mm");
        String formattedDate = null;
        try {
            Date date = oldFormat.parse(weatherData.getCurrently().getFormattedTime());
            SimpleDateFormat newFormat = new SimpleDateFormat("MMMM dd, yyyy");
            formattedDate = newFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return formattedDate;
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
}
