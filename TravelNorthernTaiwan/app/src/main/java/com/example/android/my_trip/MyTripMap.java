package com.example.android.my_trip;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.android.locations_info.LocationDetailsFragment;
import com.example.android.locations_info.LocationDetailsResponse;
import com.example.android.locations_info.LocationsListFragment;
import com.example.android.locations_info.LocationsResponse;
import com.example.android.locations_info.Result;
import com.example.android.map.Location;
import com.example.android.map.RegionsCoordinates;
import com.example.android.map.Results;
import com.example.android.travelnortherntaiwan.R;
import com.example.android.travelnortherntaiwan.SingletonRequestQueue;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

public class MyTripMap extends AppCompatActivity implements
        OnMapReadyCallback,
        LocationsListFragment.OnLocationPressedListener,
        LocationsListFragment.OnLocationAddedListener,
        LocationsListFragment.OnLocationDeletedListener,
        LocationDetailsFragment.OnLocationAddedListener,
        LocationDetailsFragment.OnLocationDeletedListener{

    private final String GOOGLE_API_KEY = "AIzaSyCc4acsOQV7rnQ92weHYKO14fvL9wkRpKc";

    private GoogleMap mMap;
    private TripBasicInfo myTrip;
    private ArrayList<LocationDetailsResponse> destinationsDetail;
    private LocationsResponse destinationList;
    private RequestQueue queue;
    private RegionsCoordinates regionsCoordinates;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference mItineraryRef;
    private LocationsListFragment locationsListFragment;
    private PolylineOptions polylineOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_trip_map);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.my_trip_map);
        mapFragment.getMapAsync(this);

        destinationList = new LocationsResponse();
        destinationsDetail = new ArrayList<>();
        regionsCoordinates = new RegionsCoordinates();
        queue = SingletonRequestQueue.getInstance(getApplicationContext()).getRequestQueue();

        polylineOptions = new PolylineOptions().width(5).color(Color.BLUE);

        myTrip = (TripBasicInfo)getIntent().getExtras().get("basicInfo");
        locationsListFragment = new LocationsListFragment();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        String refUrl = "https://travel-northern-taiwan.firebaseio.com/";
        mItineraryRef = FirebaseDatabase.getInstance().getReferenceFromUrl(refUrl + "Itinerary/" + myTrip.getKey());

        setFirebaseListener();

        Bundle bundle = new Bundle();
        bundle.putString("tripKey", myTrip.getKey());
        bundle.putBoolean("newTrip", false);
        locationsListFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.locations_container, locationsListFragment).commit();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(regionsCoordinates.getRegionCoordinates(myTrip.getRegion()), 11));
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                LocationDetailsFragment fragment = new LocationDetailsFragment();
                Bundle bundle = new Bundle();
                bundle.putString("tripKey", myTrip.getKey());
                bundle.putInt("holderPosition", 0);
                bundle.putBoolean("newTrip", false);
                fragment.setArguments(bundle);
                fragment.setPlaceId(marker.getTag().toString());

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_from_top, R.anim.enter_from_bottom, R.anim.exit_from_top);
                fragmentTransaction.add(R.id.locations_container, fragment, "DetailsFragmentUp");
                fragmentTransaction.addToBackStack("locationDetailsStack");
                fragmentTransaction.commit();
            }
        });
    }

    private void apiCallPlaceDetails(String placeId, final int size) {
        String url = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + placeId + "&fields=price_level,name,rating,formatted_address,formatted_phone_number,geometry,icon,id,opening_hours,photos,place_id,plus_code,rating,reviews&key=" + GOOGLE_API_KEY;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("Responses", response);
                        addToItineraryList(response, size);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Find", "Fail");
            }
        });

        queue.add(stringRequest);
    }

    private void addToItineraryList(String response, int size) {
        LocationDetailsResponse placeDetails = new Gson().fromJson(response, LocationDetailsResponse.class);
//        Results results = new Gson().fromJson(response, Results.class);
        Results results = setResults(placeDetails);

        Log.d("DESTINATION", results.toString());
        placeMarker(placeDetails);

        destinationList.getResults().add(results);
        destinationsDetail.add(placeDetails);
        for(LocationDetailsResponse dest : destinationsDetail) {
            Log.d("DETAILS", dest.getResult().getName());
        }

        locationsListFragment.updateData(results);
    }

    private Results setResults(LocationDetailsResponse locationDetailsResponse) {
        Results results = new Results();
        results.setName(locationDetailsResponse.getResult().getName());
        results.setPlace_id(locationDetailsResponse.getResult().getPlace_id());
        results.setPhotos(locationDetailsResponse.getResult().getPhotos());
        results.setGeometry(locationDetailsResponse.getResult().getGeometry());
        results.setOpening_hours(locationDetailsResponse.getResult().getOpening_hours());
        results.setRating(locationDetailsResponse.getResult().getRating());

        return results;
    }

    private void placeMarker(LocationDetailsResponse placeDetails) {
        MarkerOptions marker = new MarkerOptions();
        marker.title(placeDetails.getResult().getName());
        marker.snippet(placeDetails.getResult().getRating());
        marker.position(placeDetails.getResult().getGeometry().getLocation().getLatLng());
        marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        marker.flat(true);

        polylineOptions.add(marker.getPosition());
        mMap.addMarker(marker).setTag(placeDetails.getResult().getPlace_id());
        mMap.addPolyline(polylineOptions);
    }

    private void GetItinerary(DataSnapshot ds) {
        Log.d("BASEFUEGO", "ho");
        ArrayList<String> placeIds = new ArrayList<>();
        for(DataSnapshot dataSnapshot : ds.getChildren()) {
            Log.d("BASEFUEGO", dataSnapshot.getValue().toString());
            if (dataSnapshot.getValue() != null && !dataSnapshot.getValue().equals("")) {
                placeIds.add(dataSnapshot.getValue().toString());
            }
        }

        int size = placeIds.size();
        for(int n = 0; n < size; n++) {
            Log.i("PLACESIDDD", placeIds.get(n));
            apiCallPlaceDetails(placeIds.get(n), size);
        }
    }

    private void setFirebaseListener() {
        String refUrl = "https://travel-northern-taiwan.firebaseio.com/";
        mItineraryRef = FirebaseDatabase.getInstance().getReferenceFromUrl(refUrl + "Itinerary/" + myTrip.getKey());

        mItineraryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GetItinerary(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onLocationPressed(String locationId, Location location, int position) {
        /*set the location id in the location details' fragment and put the locations
        details fragment in front of the locations list fragment*/
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location.getLatLng(), 16));

        LocationDetailsFragment fragment = new LocationDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("tripKey", myTrip.getKey());
        bundle.putBoolean("newTrip", false);
        fragment.setArguments(bundle);
        fragment.setPlaceId(locationId);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_from_top, R.anim.enter_from_bottom, R.anim.exit_from_top);
        fragmentTransaction.add(R.id.locations_container, fragment, "DetailsFragmentUp");
        fragmentTransaction.addToBackStack("locationDetailsStack");
        fragmentTransaction.commit();
    }

    @Override
    public void onLocationAdded(Results location) {

    }

    @Override
    public void onLocationDeleted(Results location) {

    }

    @Override
    public void onLocationAdded(Result location, int tripPosition) {

    }

    @Override
    public void onLocationDeleted(Result location, int tripPosition) {

    }
}
