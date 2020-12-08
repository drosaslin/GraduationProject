package com.example.android.locations_info;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.map.Photos;
import com.example.android.map.Results;
import com.example.android.travelnortherntaiwan.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by David Rosas on 8/23/2018.
 */

public class LocationListAdapter extends RecyclerView.Adapter<LocationListAdapter.LocationsViewHolder> {
    private final String GOOGLE_API_KEY = "AIzaSyCc4acsOQV7rnQ92weHYKO14fvL9wkRpKc";

    private Context context;
    private TripDestinations tripDestinations;
    private ArrayList<Results> locations;
    private LocationsListFragment locationsListFragment;
    private String tripKey;
    private FirebaseAuth mAuth;
    private DatabaseReference mRootReference;
    private boolean isNewTrip;

    public LocationListAdapter(ArrayList<Results> newLocations, Context newContext, String newTripKey, boolean newTrip) {
        locations = newLocations;
        context = newContext;
        tripKey = newTripKey;
        isNewTrip = newTrip;
        tripDestinations = TripDestinations.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mRootReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://travel-northern-taiwan.firebaseio.com");

        if(isNewTrip) {
            setDatabase();
        }
        for(String destinations : tripDestinations.getDestinations()) {
            Log.d("DESTINATIONS", destinations);
        }
    }

    @NonNull
    @Override
    public LocationListAdapter.LocationsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.map_location_list_template, parent, false);
        return new LocationListAdapter.LocationsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final LocationListAdapter.LocationsViewHolder holder, final int position) {
//        mHolder = holder;

        if(locations.get(position).getPhotos() != null) {
            Photos photo = locations.get(position).getPhotos().get(0);
            if (photo != null) {
                String reference = photo.getPhoto_reference();
                String url = "https://maps.googleapis.com/maps/api/place/photo?&maxwidth=500&photoreference=" + reference + "&key=" + GOOGLE_API_KEY;
                Picasso.get().load(url).into(holder.placePhoto);
            }
        }

        holder.placeName.setText(locations.get(position).getName());
        holder.placeRating.setText(locations.get(position).getRating());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notifyListener(position);
            }
        });

        if(isNewTrip) {
            holder.addButton.setChecked(isDestinationAlreadySelected(position));
            holder.addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("PLACEID", locations.get(position).getId());
                    if(tripDestinations.getDestinations().size() >= 10 && holder.addButton.isChecked()) {
                        Toast.makeText(context, "Itinerary full", Toast.LENGTH_SHORT).show();
                        locations.get(position).setAddedStatus(!locations.get(position).getAddedStatus());
                        holder.addButton.setChecked(false);
                    }
                    else {
                        locations.get(position).setAddedStatus(!locations.get(position).getAddedStatus());
                        updateItinerary(position, holder);
                    }
                }
            });
        }
        else {
            holder.addButton.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }



    public void addNewData(ArrayList<Results> newResults) {
        locations.addAll(newResults);
        notifyDataSetChanged();
    }

    public void clearData() {
        locations.clear();
        notifyDataSetChanged();
    }

    public void setListener(LocationsListFragment newLocationsListFragment) {
        locationsListFragment = newLocationsListFragment;
    }

    public void notifyListener(int position) {
        locationsListFragment.updateActivity(locations.get(position).getPlace_id(), locations.get(position).getGeometry().getLocation(), position);
    }

    private boolean isDestinationAlreadySelected(int position) {
        for(String destinationId : tripDestinations.getDestinations()) {
            if(destinationId.equals(locations.get(position).getPlace_id())) {
                return true;
            }
        }

        return false;
    }

    private void updateItinerary(int position, LocationListAdapter.LocationsViewHolder holder){
        Log.d("ADDINGGG", "adding");
        if(holder.addButton.isChecked()) {
            addToItinerary(position);
            locationsListFragment.updateMap(locations.get(position), true);
        }
        else {
            deleteFromItinerary(position);
            locationsListFragment.updateMap(locations.get(position), false);
        }

        String message = (holder.addButton.isChecked()) ? "Destination Added": "Destination Deleted";
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    private void addToItinerary(int position) {
        for(String destination : tripDestinations.getDestinations()) {
            Log.d("ITIINERARY", destination);
        }
        tripDestinations.addDestination(locations.get(position).getPlace_id());
        updateDatabase();
    }

    private void deleteFromItinerary(int position) {
        tripDestinations.deleteDestination(locations.get(position).getPlace_id());
        updateDatabase();
    }

    private void updateDatabase() {
        //updates the data in the database based on the items inside tripDestinations
        int arraySize = tripDestinations.getDestinations().size();
        for(String destinations : tripDestinations.getDestinations()) {
            Log.d("DESTINATIONS", destinations);
        }
        for(int n = 0; n < 10; n++) {
            //inserting all destinations added. Insert a blank character to all indexes without destinations
            if(n < arraySize) {
                mRootReference.child("Itinerary").child(tripKey).child(Integer.toString(n)).setValue(tripDestinations.getDestination(n));
            }
            else {
                mRootReference.child("Itinerary").child(tripKey).child(Integer.toString(n)).setValue("");
            }
        }
    }

    private void setDatabase() {
        //creating the desired itinerary structure in firebase
        LinkedHashMap<String, String> itinerary = new LinkedHashMap<>();

        for(int n = 0; n < 10; n++) {
            itinerary.put(Integer.toString(n), "");
        }

        mRootReference.child("Itinerary").child(tripKey).setValue(itinerary);
    }

    public ArrayList<Results> getLocations() {
        return locations;
    }

    public void finish() {
        tripDestinations.clearItinerary();
    }

    public class LocationsViewHolder extends RecyclerView.ViewHolder {
        TextView placeName;
        TextView placeRating;
        ImageView placePhoto;
        CheckBox addButton;

        public LocationsViewHolder(View itemView) {
            super(itemView);

            placeName = itemView.findViewById(R.id.place_name);
            placeRating = itemView.findViewById(R.id.place_rating);
            placePhoto = itemView.findViewById(R.id.place_image);
            addButton = itemView.findViewById(R.id.add_trip_button);
        }
    }
}
