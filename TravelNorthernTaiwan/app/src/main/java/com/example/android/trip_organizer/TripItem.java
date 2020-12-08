package com.example.android.trip_organizer;

public class TripItem {

    private String tripName;

    public TripItem(){

    }

    public TripItem(String tripName){
        this.tripName = tripName;
    }

    public String getTripName(){
        return tripName;
    }
}
