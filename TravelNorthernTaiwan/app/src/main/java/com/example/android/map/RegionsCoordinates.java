package com.example.android.map;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;

/**
 * Created by David Rosas on 10/14/2018.
 */

public class RegionsCoordinates {
    private HashMap<String, LatLng> regionCoordinates;

    public RegionsCoordinates() {
        regionCoordinates = new HashMap<>();
        regionCoordinates.put("Taipei", new LatLng(25.0330, 121.5654));
        regionCoordinates.put("New Taipei", new LatLng(25.016969, 121.462988));
        regionCoordinates.put("Keelung", new LatLng(25.12825, 121.7419));
        regionCoordinates.put("Yilan", new LatLng(24.757, 121.753));
        regionCoordinates.put("Hsinchu", new LatLng(24.80361, 120.96861));
        regionCoordinates.put("Taoyuan", new LatLng(24.99368, 121.29696));
    }

    public LatLng getRegionCoordinates(String region) {
        return regionCoordinates.get(region);
    }
}
