package com.example.android.my_trip;

import java.io.Serializable;

public class TripBasicInfo implements Serializable {
    String Name;
    String Date;
    String Region;
    String Key;
    Double Budget;

    public TripBasicInfo() {
        Name = "";
        Date = "";
        Region = "";
        Key = "";
        Budget = Double.valueOf(0);
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getRegion() {
        return Region;
    }

    public void setRegion(String region) {
        Region = region;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public Double getBudget(){return Budget;}

    public void setBudget(Double budget){Budget = budget;}
}
