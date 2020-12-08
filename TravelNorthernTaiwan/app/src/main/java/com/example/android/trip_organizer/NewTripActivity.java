package com.example.android.trip_organizer;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.map.MapsActivity;
import com.example.android.travelnortherntaiwan.Messenger;
import com.example.android.travelnortherntaiwan.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class NewTripActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    //create function to check if the trip name from the same user already exists
    //date verification or datepicker restriction
    //change imageview and textview
    //switch views
    private String currentTripKey;
    private String currentRegion;

    private Button mNextBtn;

    private EditText mTripName;
    private EditText mDate;
    private EditText mBudget;
    private TextView mRegion;
    private ImageView mRegionImg;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference mRootReference;

    private DatePickerDialog datepicker;

    private boolean isToDateFocused = false;

    private Messenger messenger;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //obtaining the current trip's key
        currentRegion = getIntent().getStringExtra("region");
        currentTripKey = getIntent().getStringExtra("tripKey");
        //String currentRegion = getIntent().getStringExtra("region");

        setContentView(R.layout.activity_new_trip);

        messenger = Messenger.getInstance();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        mTripName = (EditText) findViewById(R.id.tripName);
        mDate = (EditText) findViewById(R.id.date);
        mBudget = (EditText) findViewById(R.id.budget);
        mNextBtn = (Button) findViewById(R.id.next_btn);
        mRegionImg = (ImageView) findViewById(R.id.regionImage);
        mRegion = (TextView) findViewById(R.id.regionField);

        //Setting region name in view
        mRegion.setText(mRegion.getText() + currentRegion);

        if(currentRegion.equals("Yilan")){
            mRegionImg.setImageDrawable(getResources().getDrawable(R.drawable.main_page_yilan, getApplicationContext().getTheme()));
        }else if(currentRegion.equals("Taipei")){
            mRegionImg.setImageDrawable(getResources().getDrawable(R.drawable.main_page_taipei, getApplicationContext().getTheme()));
        }else if(currentRegion.equals("New Taipei")){
            mRegionImg.setImageDrawable(getResources().getDrawable(R.drawable.main_page_newtaipei, getApplicationContext().getTheme()));
        }else if(currentRegion.equals("Hsinchu")){
            mRegionImg.setImageDrawable(getResources().getDrawable(R.drawable.main_page_hsinchu, getApplicationContext().getTheme()));
        }else if(currentRegion.equals("Taoyuan")){
            mRegionImg.setImageDrawable(getResources().getDrawable(R.drawable.main_page_taoyuan, getApplicationContext().getTheme()));
        }else{
            mRegionImg.setImageDrawable(getResources().getDrawable(R.drawable.main_page_keelung, getApplicationContext().getTheme()));
        }


        //Get db reference
        String url = "https://travel-northern-taiwan.firebaseio.com/";
        mRootReference = FirebaseDatabase.getInstance().getReferenceFromUrl(url);
        mDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getFragmentManager(), "date picker");
            }
        });

        mNextBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                attemptNewTrip();
            }
        });
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        //Month starts from 0, so it has to be added +1 to show the correct date
        mDate.setText(new StringBuilder().append(year).append("/").append(month+1).append("/").append(dayOfMonth)); //yyyy/MM/dd

    }

//    protected void onPause() {
//        super.onPause();
//        NewTripActivity.this.finish();
//    }

    public void attemptNewTrip(){
        String tripName = mTripName.getText().toString().trim();
        String userName = currentUser.getUid();
        String date = mDate.getText().toString().trim();
        String budget = (mBudget.getText().toString().trim().isEmpty()) ? "0" : mBudget.getText().toString().trim();

        DatabaseReference budgetReference = mRootReference.child("ExpensesByTrip");
        DatabaseReference basicInfoReference = mRootReference.child("BasicTripInfo");

        if(!isValidInput(tripName, date, budget)){
            return;
        }

        Log.d("CURRENT TRIP", " is " + currentTripKey);
        //add region
        basicInfoReference.child(currentTripKey).child("TripName").setValue(tripName);
        basicInfoReference.child(currentTripKey).child("Date").setValue(date);
        basicInfoReference.child(currentTripKey).child("Budget").setValue(budget);
        basicInfoReference.child(currentTripKey).child("Author").setValue(userName);
        basicInfoReference.child(currentTripKey).child("Region").setValue(currentRegion);

        budgetReference.child(currentTripKey).child("Budget").setValue(stringToDouble(budget));
        budgetReference.child(currentTripKey).child("Accommodation").setValue(Double.valueOf(0));
        budgetReference.child(currentTripKey).child("Tickets").setValue(Double.valueOf(0));
        budgetReference.child(currentTripKey).child("Souvenirs").setValue(Double.valueOf(0));
        budgetReference.child(currentTripKey).child("Food").setValue(Double.valueOf(0));
        budgetReference.child(currentTripKey).child("Others").setValue(Double.valueOf(0));
        budgetReference.child(currentTripKey).child("Shopping").setValue(Double.valueOf(0));

        //move later to another function
        //sending the user to another view and passing the current trip parameter to the view
        Intent mapActivity = new Intent(this,MapsActivity.class);
        mapActivity.putExtra("tripKey", currentTripKey);
        mapActivity.putExtra("region", currentRegion);
        Log.d("test","key = " + currentTripKey);
        startActivity(mapActivity);
    }


    //display error if trip name is null or trip already exists
    private boolean isValidInput(String tripName, String date, String budget){
        String[] toDateToken = date.split("/");
        if(TextUtils.isEmpty(tripName)){
            Toast.makeText(getApplicationContext(), "Please enter a name for your trip", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private Double stringToDouble(String budgetString){
        Double newValue = Double.valueOf(0);
        if(budgetString != null && budgetString.length() > 0){
            try{
                return Double.parseDouble(budgetString);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Please enter a valid budget", Toast.LENGTH_SHORT).show();
            }
        }
        return newValue;
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Toast.makeText(this, Integer.toString(messenger.getCount()), Toast.LENGTH_SHORT).show();
        if(messenger.isTripFinished() || messenger.isTripCanceled()) {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        mRootReference.child("BasicTripInfo").child(currentTripKey).removeValue();
        mRootReference.child("ExpensesByTrip").child(currentTripKey).removeValue();

        super.onBackPressed();
    }
}