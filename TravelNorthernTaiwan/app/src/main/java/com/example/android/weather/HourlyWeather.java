package com.example.android.weather;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class HourlyWeather implements Parcelable {
    private String summary;
    private String icon;
    private ArrayList<Forecast> data;

    protected HourlyWeather(Parcel in) {
        summary = in.readString();
        icon = in.readString();
        data = in.createTypedArrayList(Forecast.CREATOR);
    }

    public static final Creator<HourlyWeather> CREATOR = new Creator<HourlyWeather>() {
        @Override
        public HourlyWeather createFromParcel(Parcel in) {
            return new HourlyWeather(in);
        }

        @Override
        public HourlyWeather[] newArray(int size) {
            return new HourlyWeather[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(summary);
        dest.writeString(icon);
        dest.writeTypedList(this.data);
    }

    public String getSummary() {
        return summary;
    }

    public String getIcon() {
        return icon;
    }

    public ArrayList<Forecast> getData() {
        return data;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setData(ArrayList<Forecast> data) {
        this.data = data;
    }

    public void unixToDate() {
        for(Forecast forecast : data) {
            forecast.unixToDate();
        }
    }

    public void showData() {
        for(Forecast forecast : data) {
            forecast.showData();
        }
    }
}
