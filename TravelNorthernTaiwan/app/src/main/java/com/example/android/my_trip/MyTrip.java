package com.example.android.my_trip;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.NavigationMenu;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.android.locations_info.LocationDetailsResponse;
import com.example.android.travelnortherntaiwan.R;
import com.example.android.travelnortherntaiwan.SingletonRequestQueue;
import com.example.android.weather.WeatherData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.transferwise.sequencelayout.SequenceLayout;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;

import io.github.yavski.fabspeeddial.FabSpeedDial;

public class MyTrip extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    private final String GOOGLE_API_KEY = "AIzaSyCc4acsOQV7rnQ92weHYKO14fvL9wkRpKc";
    private static final String WEATHER_KEY = "db4321093bdd7e123918dc6fa6e9c1e3";

    private RequestQueue queue;

    private SequenceLayout sequenceLayout;
    private MyAdapter sequenceAdapter;
    private ArrayList<MyAdapter.MyItem> myItemList;

    private EditText mTripName;
    private EditText mDate;
    private TextView mRest;
    private TextView mRegion;

    private Button manageBudgetBtn;
    private Button saveInfoBtn;
    private FabSpeedDial showMoreButton;

    private String currentTripKey;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference mBasicInfoRef;
    private DatabaseReference mItineraryRef;
    private DatabaseReference mBudgetRef;

    private TripBasicInfo infoToDisplay;
    private LinkedHashMap<String, ArrayList<String>> coordinates;
    private WeatherData weatherData;
    private DatePickerDialog datepicker;
    private ArrayList<LocationDetailsResponse> destinationsDetails;

    private ValueEventListener budgetListener, basicInfoListener;
    private float Budget, Accommodation, Food, Shopping, Souvenirs, Tickets, Others;

    int counter = 0;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_info);
        setProvinces();
        currentTripKey = getIntent().getExtras().getString("tripKey");
        String refUrl = "https://travel-northern-taiwan.firebaseio.com/";

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        destinationsDetails = new ArrayList<>();
        queue = SingletonRequestQueue.getInstance(getApplicationContext()).getRequestQueue();
        sequenceLayout = findViewById(R.id.sequence_layout);
        myItemList = new ArrayList<>();

        //Database references for the basic trip information and the itinerary
        mBasicInfoRef = FirebaseDatabase.getInstance().getReferenceFromUrl(refUrl + "BasicTripInfo/" + currentTripKey);
        mItineraryRef = FirebaseDatabase.getInstance().getReferenceFromUrl(refUrl + "Itinerary/" + currentTripKey);
        mBudgetRef = FirebaseDatabase.getInstance().getReferenceFromUrl(refUrl + "ExpensesByTrip/" + currentTripKey);
        Log.d("ITINERARY", mItineraryRef.getRef().toString());

        mTripName = (EditText)findViewById(R.id.tripName);
        mDate = (EditText)findViewById(R.id.start_date);
        mRest = (TextView) findViewById(R.id.rest);
        mRegion = (TextView) findViewById(R.id.regionField);

        manageBudgetBtn = findViewById(R.id.manageBudget);
        showMoreButton = findViewById(R.id.selection_button);
        setActionButton();

        infoToDisplay = new TripBasicInfo();


        manageBudgetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyTrip.this, BudgetManagerActivity.class);
                intent.putExtra("tripKey", currentTripKey);
                startActivity(intent);
            }
        });
//
//        saveInfoBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SaveInfo();
//            }
//        });
    }

    private void setActionButton() {
        showMoreButton.setMenuListener(new FabSpeedDial.MenuListener() {
            @Override
            public boolean onPrepareMenu(NavigationMenu navigationMenu) {
                return true;
            }

            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                if(menuItem.getItemId() == R.id.action_weather) {
//                    Toast.makeText(MyTrip.this, infoToDisplay.getRegion(), Toast.LENGTH_SHORT).show();
                    try {
                        prepareWeatherData();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                if(menuItem.getItemId() == R.id.action_map) {
                    Intent intent = new Intent(MyTrip.this, MyTripMap.class);
                    intent.putExtra("basicInfo", infoToDisplay);
                    startActivity(intent);
                }
                if(menuItem.getItemId() == R.id.action_tasks) {
                    Bundle bundle = new Bundle();
                    Intent intent = new Intent(MyTrip.this, ToDoListActivity.class);
                    intent.putExtra("tripKey", infoToDisplay.getKey());
                    getApplicationContext().startActivity(intent);
                }
                return true;
            }

            @Override
            public void onMenuClosed() {

            }
        });
    }

    private void GetBasicInfo(DataSnapshot ds) {
        infoToDisplay.setKey(ds.getKey());
        infoToDisplay.setName(ds.child("TripName").getValue().toString());
        infoToDisplay.setRegion(ds.child("Region").getValue().toString());
        infoToDisplay.setDate(ds.child("Date").getValue().toString());
        infoToDisplay.setBudget(Double.parseDouble(ds.child("Budget").getValue().toString()));
    }

    private void GetItinerary(DataSnapshot ds) {
        ArrayList<String> placeIds = new ArrayList<>();
        boolean flag = false;
        for(DataSnapshot dataSnapshot : ds.getChildren()){
            if(dataSnapshot.getValue()!=null && !dataSnapshot.getValue().equals("")){
                for(LocationDetailsResponse tripInfo : destinationsDetails) {
                    if(tripInfo.getResult().getPlace_id().trim().equals(dataSnapshot.getValue().toString().trim())) {
                        flag = true;
                        break;
                    }
                }
                if(!flag) {
                    placeIds.add(dataSnapshot.getValue().toString());
                }
                flag = false;
            }
        }

        int size = placeIds.size();
        for(int n = 0; n < size; n++) {
            Log.i("PLACESIDDD", placeIds.get(n));
            apiCallPlaceDetails(placeIds.get(n), size);
        }
    }

    private void GetBudget(DataSnapshot ds) {
        Budget        = Float.parseFloat(ds.child("Budget").getValue().toString());
        Accommodation = Float.parseFloat(ds.child("Accommodation").getValue().toString());
        Food          = Float.parseFloat(ds.child("Food").getValue().toString());
        Shopping      = Float.parseFloat(ds.child("Shopping").getValue().toString());
        Souvenirs     = Float.parseFloat(ds.child("Souvenirs").getValue().toString());
        Tickets       = Float.parseFloat(ds.child("Tickets").getValue().toString());
        Others        = Float.parseFloat(ds.child("Others").getValue().toString());
        Log.d("GetBudget", "budget -> " + Budget);
        Log.d("GetBudget", "Accommodation -> " + Accommodation);
        Log.d("GetBudget", "Food -> " + Food);
        Log.d("GetBudget", "Shopping -> " + Shopping);
        Log.d("GetBudget", "Souvenirs -> " + Souvenirs);
        Log.d("GetBudget", "Tickets -> " + Tickets);
        Log.d("GetBudget", "Others -> " + Others);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        /*Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        //Month starts from 0, so it has to be added +1 to show the correct date
        if(isToDateFocused){
            mToDate.setText(new StringBuilder().append(year).append("/").append(month+1).append("/").append(dayOfMonth)); //yyyy/MM/dd
        }else{
            mFromDate.setText(new StringBuilder().append(year).append("/").append(month+1).append("/").append(dayOfMonth)); //yyyy/MM/dd
        }*/
    }

    private void DisplayInfo(TripBasicInfo infoToDisplay){
        mTripName.setText(infoToDisplay.getName(), TextView.BufferType.EDITABLE);
        mDate.setText(infoToDisplay.getDate(), TextView.BufferType.EDITABLE);
        mRegion.setText("Region: " + infoToDisplay.getRegion());
    }

    private void DisplayRest(){
        float restF = Budget-(Accommodation + Food + Shopping + Souvenirs + Tickets + Others);
        String rest = Float.toString(restF);
        mRest.setText(rest);
        Log.d("GetBudget", "rest -> " + rest);
    }

//    private void SaveInfo(){
//        mBasicInfoRef.child("TripName").setValue(mTripName.getText().toString());
//        mBasicInfoRef.child("Date").setValue(mDate.getText().toString());
//        Toast.makeText(getApplicationContext(), "Changes saved", Toast.LENGTH_SHORT).show();
//    }

    private void apiCallPlaceDetails(String placeId, final int size) {
        String url = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + placeId + "&fields=price_level,name,rating,formatted_address,formatted_phone_number,geometry,icon,id,opening_hours,photos,place_id,plus_code,rating,reviews&key=" + GOOGLE_API_KEY;
        Log.d("PLACEULR", url);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("Response", response);
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
        destinationsDetails.add(placeDetails);
        for(LocationDetailsResponse destination : destinationsDetails) {
            Log.d("DETAILS", destination.getResult().getName());
        }

        myItemList.add(new MyAdapter.MyItem(false, "", placeDetails.getResult().getName(), ""));

        counter++;
        if(counter == size) {
            myItemList.get(myItemList.size() - 1).setActive(true);
            sequenceAdapter = new MyAdapter(myItemList);
            sequenceLayout.setAdapter(sequenceAdapter);
            counter = 0;
        }
    }

    public void prepareWeatherData() throws ParseException {
        queue = com.example.android.travelnortherntaiwan.SingletonRequestQueue.getInstance(getApplicationContext()).getRequestQueue();
        DateFormat dateFormat = new SimpleDateFormat("yyy/MM/dd");

        Date tripDate = (infoToDisplay.getDate().equals("")) ? new Date() : dateFormat.parse(infoToDisplay.getDate());
        String unixDate = Long.toString(tripDate.getTime() / 1000L);

        String url = "https://api.darksky.net/forecast/" + WEATHER_KEY + "/" + coordinates.get(infoToDisplay.getRegion()).get(0) + "," +  unixDate + "?units=si";
        Log.d("URLLL", url);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("Response", response);
                        openTripWeather(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Find", "Fail");
            }
        });

        queue.add(stringRequest);
    }

    public void openTripWeather(String response) {
        weatherData = new Gson().fromJson(response, WeatherData.class);
        weatherData.unixToDate();
        Log.d("TIMEEEEE", weatherData.getHourly().getData().get(0).getFormattedTime());

        Bundle bundle = new Bundle();
        bundle.putParcelable("WeatherData", weatherData);
        Intent intent = new Intent(getApplicationContext(), MyTripWeather.class);
        intent.putExtra("region", infoToDisplay.getRegion());

        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void setProvinces() {
        coordinates = new LinkedHashMap<>();

        coordinates.put("Taipei", new ArrayList<>(Arrays.asList("25.0330,121.5654", "1")));
        coordinates.put("New Taipei", new ArrayList<>(Arrays.asList("25.0170,121.4628", "2")));
        coordinates.put("Keelung", new ArrayList<>(Arrays.asList("25.1276,121.7392", "3")));
        coordinates.put("Hsinchu", new ArrayList<>(Arrays.asList("24.8138,120.9675", "4")));
        coordinates.put("Taoyuan", new ArrayList<>(Arrays.asList("24.9936,121.3010", "5")));
        coordinates.put("Yilan", new ArrayList<>(Arrays.asList("24.7021,121.7378", "6")));
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBasicInfoRef.removeEventListener(basicInfoListener);
        mBudgetRef.removeEventListener(budgetListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        basicInfoListener = mBasicInfoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GetBasicInfo(dataSnapshot);
                DisplayInfo(infoToDisplay);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        budgetListener = mBudgetRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GetBudget(dataSnapshot);
                DisplayRest();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
}