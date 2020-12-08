package com.example.android.weather;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by David Rosas on 8/8/2018.
 */

public class WeatherData implements Parcelable {
    private double latitude;
    private double longitude;
    private String city;
    private Forecast currently;
    private HourlyWeather hourly;
    private HourlyWeather daily;

    public WeatherData(Parcel in) {
        latitude = in.readDouble();
        longitude = in.readDouble();
        city = in.readString();
        currently = in.readParcelable(Forecast.class.getClassLoader());
        hourly = in.readParcelable(HourlyWeather.class.getClassLoader());
        daily = in.readParcelable(HourlyWeather.class.getClassLoader());
    }

    public static final Creator<WeatherData> CREATOR = new Creator<WeatherData>() {
        @Override
        public WeatherData createFromParcel(Parcel in) {
            return new WeatherData(in);
        }

        @Override
        public WeatherData[] newArray(int size) {
            return new WeatherData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(longitude);
        dest.writeDouble(latitude);
        dest.writeString(city);
        dest.writeParcelable(currently, flags);
        dest.writeParcelable(hourly, flags);
        dest.writeParcelable(daily, flags);
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getCity() {
        return city;
    }

    public Forecast getCurrently() {
        return currently;
    }

    public HourlyWeather getHourly() {
        return hourly;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCurrently(Forecast currently) {
        this.currently = currently;
    }

    public void setHourly(HourlyWeather hourly) {
        this.hourly = hourly;
    }

    public void showData() {
        Log.i("Data", "province: " + city);
        Log.i("Data", "coordinates: " + Double.toString(latitude) + ", " + Double.toString(longitude));
        currently.showData();
        hourly.showData();
        daily.showData();
    }

    public void unixToDate() {
        currently.unixToDate();
        hourly.unixToDate();
        daily.unixToDate();
    }

    public HourlyWeather getDaily() {
        return daily;
    }

    public void setDaily(HourlyWeather daily) {
        this.daily = daily;
    }
}
