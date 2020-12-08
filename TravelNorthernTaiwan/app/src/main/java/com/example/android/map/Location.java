package com.example.android.map;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by David Rosas on 9/5/2018.
 */

public class Location {
    private Double lng;

    private Double lat;

    public Double getLng ()
    {
        return lng;
    }

    public void setLng (Double lng)
    {
        this.lng = lng;
    }

    public Double getLat ()
    {
        return lat;
    }

    public void setLat (Double lat)
    {
        this.lat = lat;
    }

    public LatLng getLatLng() {
        return new LatLng(lat, lng);
    }

    @Override
    public String toString()
    {
        return "ClassPojo [lng = "+lng+", lat = "+lat+"]";
    }
}
