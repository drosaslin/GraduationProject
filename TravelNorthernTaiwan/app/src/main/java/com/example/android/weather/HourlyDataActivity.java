package com.example.android.weather;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.travelnortherntaiwan.R;

import java.util.HashMap;

public class HourlyDataActivity extends AppCompatActivity {
    private WeatherData weatherData;
    private TextView place;
    private TextView temp;
    private TextView precipitationProb;
    private TextView windSpeed;
    private ImageView weather;
    private RecyclerView hourlyRecycler;
    private RecyclerView dailyRecycler;
    private HourlyDataAdapter hourlyAdapter;
    private WeeklyDataAdapter dailyAdapter;
    private HashMap<String, Integer> weatherIcons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_activity_hourly_data);

        hourlyRecycler = findViewById(R.id.hourly_recycler);
        hourlyRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false));

        dailyRecycler = findViewById(R.id.daily_recycler);
        dailyRecycler.setLayoutManager(new LinearLayoutManager(this));

        place = findViewById(R.id.current_name);
        temp = findViewById(R.id.current_temperature);
        precipitationProb = findViewById(R.id.precipitation_prob);
        windSpeed = findViewById(R.id.wind_speed);
        weather = findViewById(R.id.current_weather);
        weatherIcons = new HashMap<>();

        weatherData = null;
        getIncomingIntent();
        populateIcons();
        updateGUI();
        weatherData.showData();
    }

    private void updateGUI() {
        if(weatherData != null) {
            float precip = weatherData.getCurrently().getPrecipProbability() + 100;
            String precipitation = Integer.toString((int)precip) + "%";
            String temperature = Integer.toString(Math.round(weatherData.getCurrently().getTemperature())) + "°";
            String windSp = Integer.toString((int)weatherData.getCurrently().getWindSpeed()) + " km/h";
            String icon = weatherData.getCurrently().getIcon();

            place.setText(weatherData.getCity());
            temp.setText(temperature);
            precipitationProb.setText(precipitation);
            windSpeed.setText(windSp);
            weather.setImageResource(weatherIcons.get(icon));

            hourlyAdapter = new HourlyDataAdapter(weatherData.getHourly().getData());
            hourlyRecycler.setAdapter(hourlyAdapter);

            dailyAdapter = new WeeklyDataAdapter(weatherData.getDaily().getData());
            dailyRecycler.setAdapter(dailyAdapter);
        }
    }

    private void getIncomingIntent() {
        Bundle data = getIntent().getExtras();
        if(data != null) {
            weatherData = data.getParcelable("WeatherData");
        }
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
