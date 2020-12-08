package com.example.android.locations_info;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by David Rosas on 10/10/2018.
 */

public class TripDestinations {
    private static TripDestinations singletonTripDestinations;
    private ArrayList<String> destinations = new ArrayList<>();

    private TripDestinations() {
    }

    public static TripDestinations getInstance() {
        if(singletonTripDestinations == null) {
            singletonTripDestinations = new TripDestinations();
            return singletonTripDestinations;
        }

        return singletonTripDestinations;
    }

    public void addDestination(String newDestination) {
        destinations.add(newDestination);
    }

    public void deleteDestination(String destinationToDelete) {
        if(destinations != null) {
            for (String destination : destinations) {
                if (destination.equals(destinationToDelete)) {
                    destinations.remove(destination);
                    break;
                }
            }
        }
    }

    public ArrayList<String> getDestinations() {
        return destinations;
    }

    public String getDestination(int index) {
        return destinations.get(index);
    }

    public void clearItinerary() {
        destinations.clear();
    }
}
