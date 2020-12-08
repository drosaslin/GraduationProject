package com.example.android.trip_organizer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;

import com.example.android.my_trip.MyTrip;
import com.example.android.travelnortherntaiwan.Messenger;
import com.example.android.travelnortherntaiwan.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class ChooseRegionActivity extends AppCompatActivity {
    CardView taipeiCard, newTaipeiCard, keelungCard, yilanCard, hsinchuCard, taoyuanCard;
    ArrayList<CardView> cards;
    private String tripKey;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference mRootReference;
    private Messenger messenger;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        //obtaining the current trip's key
//        tripKey = getIntent().getExtras().getString("tripKey");
        //Log.d("test2", currentTripKey);

        messenger = Messenger.getInstance();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        mRootReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://travel-northern-taiwan.firebaseio.com/");

        taipeiCard = findViewById(R.id.taipeiCard);
        newTaipeiCard = findViewById(R.id.newTaipeiCard);
        keelungCard = findViewById(R.id.keelungCard);
        yilanCard = findViewById(R.id.yilanCard);
        hsinchuCard = findViewById(R.id.hsinchuCard);
        taoyuanCard = findViewById(R.id.taoyuanCard);

        cards = new ArrayList<>();
        cards.add(taipeiCard);
        cards.add(newTaipeiCard);
        cards.add(keelungCard);
        cards.add(yilanCard);
        cards.add(hsinchuCard);
        cards.add(taoyuanCard);

        final Intent newTripActivity = new Intent(ChooseRegionActivity.this, NewTripActivity.class);

        for(CardView card : cards) {
            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(messenger.getCount() == 0) {
                        messenger.addCount();
                    }
                    String tag = (String) view.getTag();
                    GenerateTripKey(tag);
                    //mapsActivity.putExtra("region", tag);
                    //mapsActivity.putExtra("tripKey", currentTripKey);
                    //mRootReference.child(currentTripKey).child("Region").setValue(tag);
                    //startActivity(newTripActivity);
                }
            });
        }
    }

    private void GenerateTripKey(String tag){
        //obtaining a randomly generated key for trip
        String currentKey = mRootReference.push().getKey();

        //Declaring hashmaps for BasicTripInfo and ExpensesByTrip
        HashMap<String, String> infoMap =  new HashMap<String, String>();
        HashMap<String, Double> expensesMap =  new HashMap<String, Double>();

        infoMap.put("TripName", "");
        infoMap.put("Date", "");
        infoMap.put("Author", currentUser.getUid());
        infoMap.put("Region", tag);

        expensesMap.put("Budget",Double.valueOf(0));
        expensesMap.put("Accommodation",Double.valueOf(0));
        expensesMap.put("Tickets",Double.valueOf(0));
        expensesMap.put("Souvenirs", Double.valueOf(0));
        expensesMap.put("Food",Double.valueOf(0));
        expensesMap.put("Others",Double.valueOf(0));

        //sending the data to the firebase database
        mRootReference.child("BasicTripInfo").child(currentKey).setValue(infoMap);
        mRootReference.child("ExpensesByTrip").child(currentKey).setValue(expensesMap);
        mRootReference.child("Itinerary").child(currentKey);
        mRootReference.child("TripTasks").child(currentKey);

        //move later to another function
        //sending the user to another view and passing the current trip parameter to the view
        Intent newTrip = new Intent(ChooseRegionActivity.this,NewTripActivity.class);
        tripKey = currentKey.toString();
        newTrip.putExtra("tripKey", tripKey);
        newTrip.putExtra("region", tag);
        Log.d("test","key = " + currentKey);
        startActivity(newTrip);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Toast.makeText(this, Integer.toString(messenger.getCount()), Toast.LENGTH_SHORT).show();
        if(messenger.isTripFinished()) {
            Intent intent = new Intent(this, MyTrip.class);
            intent.putExtra("tripKey", tripKey);
            startActivity(intent);
            finish();
        }
        if(messenger.isTripCanceled()) {
            finish();
        }
        messenger.setTripFinished(false);
        messenger.setTripCanceled(false);
        messenger.setTripKey("");
    }
}