package com.example.android.weather;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.android.travelnortherntaiwan.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class WeatherMainFragment extends android.support.v4.app.Fragment implements Serializable {
    private static final String WEATHER_KEY = "db4321093bdd7e123918dc6fa6e9c1e3";
    private int requestsFinished;
    private ProgressBar progressBar;
    private LinkedHashMap<String, ArrayList<String>> coordinates;
    private ArrayList<WeatherData> weatherData;
    private RequestQueue queue;
    private RecyclerView recycler;
    private SummaryWeatherAdapter adapter;
    private FusedLocationProviderClient locationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private LatLng currentLocation;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.weather_activity_main, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        requestsFinished = 0;

        locationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        recycler = getView().findViewById(R.id.provinces_recycler);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        weatherData = new ArrayList<>();
        coordinates = new LinkedHashMap<>();

        progressBar = new ProgressBar(getActivity(), null, android.R.attr.progressBarStyleLarge);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(100, 100);
        LinearLayout layout = getView().findViewById(R.id.weather_main);
        layout.addView(progressBar, params);
        progressBar.setVisibility(View.VISIBLE);  //To show ProgressBar

        setLocationCallBack();
        startLocationUpdates();
    }

    public void populateData() {
        for(String province : coordinates.keySet()) {
            setWeather(province);
        }
    }

    public void setWeather(String province) {
        queue = com.example.android.travelnortherntaiwan.SingletonRequestQueue.getInstance(getActivity()).getRequestQueue();

        String url = "https://api.darksky.net/forecast/" + WEATHER_KEY + "/" + coordinates.get(province).get(0) + "?units=si";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.i("Response", response);
                        updateGUI(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Find", "Fail");
            }
        });

        queue.add(stringRequest);
    }

    public void updateGUI(String response) {
        requestsFinished++;
        weatherData.add(new Gson().fromJson(response, WeatherData.class));
        weatherData.get(weatherData.size() - 1).unixToDate();

        if(requestsFinished == coordinates.size()) {
            setProvincesNames();
            progressBar.setVisibility(View.GONE);     // To Hide ProgressBar
            adapter = new SummaryWeatherAdapter(weatherData, getActivity());
            recycler.setAdapter(adapter);

            requestsFinished = 0;
        }
    }

    public void setProvincesNames() {
        for(WeatherData weather : weatherData) {
            for(Map.Entry<String, ArrayList<String>> entry : coordinates.entrySet()) {
                //set the names of the provinces if the coordinates match
                if(entry.getValue().get(0).contains(Double.toString(weather.getLatitude())) &&
                        entry.getValue().get(0).contains(Double.toString(weather.getLongitude()))) {
                    Log.i("String", entry.getValue().get(0) + "-" + entry.getKey());
                    weather.setCity(entry.getKey());
                }
            }
        }
    }

    public void setProvinces() throws IOException {
        String city = getCurrentCity();
//        Toast.makeText(getActivity(), city, Toast.LENGTH_SHORT).show();
        if(!coordinates.isEmpty()) {
            coordinates.clear();
        }

        //latitude and longitude of the provinces
        coordinates.put(city, new ArrayList<>(Arrays.asList(currentLocation.latitude + "," + currentLocation.longitude, "0")));
        coordinates.put("Taipei", new ArrayList<>(Arrays.asList("25.0330,121.5654", "1")));
        coordinates.put("New Taipei", new ArrayList<>(Arrays.asList("25.0170,121.4628", "2")));
        coordinates.put("Keelung", new ArrayList<>(Arrays.asList("25.1276,121.7392", "3")));
        coordinates.put("Hsinchu", new ArrayList<>(Arrays.asList("24.8138,120.9675", "4")));
        coordinates.put("Taoyuan", new ArrayList<>(Arrays.asList("24.9936,121.3010", "5")));
        coordinates.put("Yilan", new ArrayList<>(Arrays.asList("24.7021,121.7378", "6")));
    }

    private void setLocationCallBack() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                        try {
                            setProvinces();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        populateData();
                        locationProviderClient.removeLocationUpdates(locationCallback);
                    }
                }
            }
        };
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getActivity(), "permissions", Toast.LENGTH_SHORT).show();
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    private String getCurrentCity() throws IOException {
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(currentLocation.latitude, currentLocation.longitude, 1);
            String cityName = addresses.get(0).getLocality();
            return cityName;
        }
        catch (IOException e) {

        }
        return "";
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
