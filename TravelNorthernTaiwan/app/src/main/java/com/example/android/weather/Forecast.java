package com.example.android.weather;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by David Rosas on 8/8/2018.
 */

public class Forecast implements Parcelable {
    private int time;
    private float precipProbability;
    private float temperature;
    private float temperatureHigh;
    private float temperatureLow;
    private double windSpeed;
    private String precipType;
    private String summary;
    private String icon;
    private String formattedTime;

    public Forecast() {
        time = 0;
        precipProbability = 0;
        temperature = 0;
        temperatureHigh = 0;
        temperatureLow = 0;
        windSpeed = 0;
        precipType = "";
        summary = "";
        icon = "";
        formattedTime = "";
    }

    protected Forecast(Parcel in) {
        time = in.readInt();
        precipProbability = in.readFloat();
        temperature = in.readFloat();
        temperatureHigh = in.readFloat();
        temperatureLow = in.readFloat();
        windSpeed = in.readDouble();
        precipType = in.readString();
        summary = in.readString();
        icon = in.readString();
        formattedTime = in.readString();
    }

    public static final Creator<Forecast> CREATOR = new Creator<Forecast>() {
        @Override
        public Forecast createFromParcel(Parcel in) {
            return new Forecast(in);
        }

        @Override
        public Forecast[] newArray(int size) {
            return new Forecast[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(time);
        dest.writeFloat(precipProbability);
        dest.writeFloat(temperature);
        dest.writeFloat(temperatureHigh);
        dest.writeFloat(temperatureLow);
        dest.writeDouble(windSpeed);
        dest.writeString(precipType);
        dest.writeString(summary);
        dest.writeString(icon);
        dest.writeString(formattedTime);
    }

    public int getTime() {
        return time;
    }

    public String getFormattedTime() {
        return formattedTime;
    }

    public float getPrecipProbability() {
        return precipProbability;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public float getTemperatureHigh() {
        return temperatureHigh;
    }

    public float getTemperatureLow() {
        return temperatureLow;
    }

    public float getTemperature() {
        return temperature;
    }

    public String getPrecipType() {
        return precipType;
    }

    public String getSummary() {
        return summary;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setPrecipProbability(float precipProbability) {
        this.precipProbability = precipProbability;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public void setPrecipType(String precipType) {
        this.precipType = precipType;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void unixToDate() {
        Date date = new Date((long)time*1000);
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE yyyy-MM-dd HH:mm");
        formattedTime = dateFormat.format(date);
        Log.i("UNIXTODATE", formattedTime);
    }

    public void showData() {
        Log.i("TIMESTAMP", "time: " + formattedTime);
        Log.i("Data", "precipitation: " + Float.toString(precipProbability));
        Log.i("Data", "temperature: " + Float.toString(temperature));
        if(temperatureLow != 0.0) {
            Log.i("DataTEMP", "temperatureHigh: " + Float.toString(temperatureHigh));
            Log.i("Data", "temperatureLow: " + Float.toString(temperatureLow));
        }
        Log.i("Data", "precipitation type: " + precipType);
        Log.i("Data", "summary: " + summary);
        Log.i("Data", "icon: " + icon);
    }
}
