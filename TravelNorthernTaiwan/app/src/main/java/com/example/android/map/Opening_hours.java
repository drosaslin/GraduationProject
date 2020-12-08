package com.example.android.map;

import android.os.Parcel;
import android.os.Parcelable;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by David Rosas on 9/5/2018.
 */

public class Opening_hours implements Parcelable
{
    private ArrayList<Periods> periods;

    private String open_now;

    private ArrayList<String> weekday_text;

    protected Opening_hours(Parcel in) {
        open_now = in.readString();
        weekday_text = in.createStringArrayList();
    }

    public static final Creator<Opening_hours> CREATOR = new Creator<Opening_hours>() {
        @Override
        public Opening_hours createFromParcel(Parcel in) {
            return new Opening_hours(in);
        }

        @Override
        public Opening_hours[] newArray(int size) {
            return new Opening_hours[size];
        }
    };

    public ArrayList<Periods> getPeriods ()
    {
        return periods;
    }

    public void setPeriods (ArrayList<Periods> periods)
    {
        this.periods = periods;
    }

    public String getOpen_now ()
    {
        return open_now;
    }

    public void setOpen_now (String open_now)
    {
        this.open_now = open_now;
    }

    public ArrayList<String> getWeekday_text ()
    {
        return weekday_text;
    }

    public void setWeekday_text (ArrayList<String> weekday_text)
    {
        this.weekday_text = weekday_text;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [periods = "+periods+", open_now = "+open_now+", weekday_text = "+weekday_text+"]";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(open_now);
        dest.writeStringList(weekday_text);
    }
}