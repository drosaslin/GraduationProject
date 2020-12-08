package com.example.android.my_trip;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.travelnortherntaiwan.R;
import com.example.android.trip_organizer.TripItem;
import com.example.android.trip_organizer.TripNameAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SecondActivity extends AppCompatActivity {

    private static final String TAG="SecondActivity";
    private ArrayList<ExpenseItem> mExpenseList;
    private ExpenseAdapter mAdapter;
    private ArrayList<TripItem> mTripNameList;
    private TripNameAdapter mtripNameAdapter;
    private Button submitBtn;
    private FirebaseDatabase database;
    private DatabaseReference mRef;
    private EditText textMoney;
    private int money, Budget, Accommodation, Food, Shopping, Souvenirs, Tickets, Others;
    private String tripName, expenseCategory;
    private TripBudget tripBudget, tempTripBudget;
    private String currentTripKey;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity_layout);

        tripName = null;

        database = FirebaseDatabase.getInstance();
        String url = "https://travel-northern-taiwan.firebaseio.com/ExpensesByTrip";
        mRef = FirebaseDatabase.getInstance().getReferenceFromUrl(url);
        currentTripKey = getIntent().getExtras().getString("tripKey");
        Log.d("secondActivity", "currentTripKey -> " + currentTripKey);

        initExpenseList();
        initTripNameList();

        Spinner spinnerExpense = findViewById(R.id.spinner_expense);
        Spinner spinnerTripName = findViewById(R.id.spinner_trip_name);

        mAdapter = new ExpenseAdapter(this, mExpenseList);
        spinnerExpense.setAdapter(mAdapter);
        mtripNameAdapter = new TripNameAdapter(this, mTripNameList);
        spinnerTripName.setAdapter(mtripNameAdapter);

        spinnerExpense.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ExpenseItem clickedItem = (ExpenseItem)parent.getItemAtPosition(position);
                String clickedExpenseName = clickedItem.getmCategoryName();
                expenseCategory = clickedExpenseName;
                Toast.makeText(SecondActivity.this, clickedExpenseName + " selected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerTripName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TripItem clickedItem = (TripItem)parent.getItemAtPosition(position);
                tripName = clickedItem.getTripName();
                Log.d("testinggg", tripName);
                Toast.makeText(SecondActivity.this, tripName + " selected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        textMoney = (EditText)findViewById(R.id.editText_money);
        submitBtn = (Button)findViewById(R.id.button_summit);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                money = Integer.valueOf(textMoney.getText().toString());
                if(TextUtils.isEmpty(textMoney.getText().toString().trim())){
                    Toast.makeText(SecondActivity.this, "Please input money", Toast.LENGTH_SHORT).show();
                    return;
                }

                mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        changeData(dataSnapshot);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                textMoney.getText().clear();
            }
        });
    }

    private void initExpenseList() {
        mExpenseList = new ArrayList<>();
        mExpenseList.add(new ExpenseItem("Budget", R.drawable.budget));
        mExpenseList.add(new ExpenseItem("Accommodation", R.drawable.accommodation));
        mExpenseList.add(new ExpenseItem("Food", R.drawable.food));
        mExpenseList.add(new ExpenseItem("Shopping", R.drawable.cart));
        mExpenseList.add(new ExpenseItem("Souvenirs", R.drawable.souvenir));
        mExpenseList.add(new ExpenseItem("Tickets", R.drawable.ticket));
        mExpenseList.add(new ExpenseItem("Others", R.drawable.other));
    }

    private void initTripNameList(){
        mTripNameList = new ArrayList<>();
        mTripNameList.add(new TripItem("Good Trip"));
        mTripNameList.add(new TripItem("Wonderful Trip"));
        mTripNameList.add(new TripItem("Boring Trip"));
        mTripNameList.add(new TripItem("Fun Trip"));
        mTripNameList.add(new TripItem("Terrible Trip"));
    }

    private void changeData(DataSnapshot dataSnapshot){
        Budget = Integer.valueOf(dataSnapshot.child(currentTripKey).child("Budget").getValue().toString());
        Accommodation = Integer.valueOf(dataSnapshot.child(currentTripKey).child("Accommodation").getValue().toString());
        Food = Integer.valueOf(dataSnapshot.child(currentTripKey).child("Food").getValue().toString());
        Shopping = Integer.valueOf(dataSnapshot.child(currentTripKey).child("Shopping").getValue().toString());
        Souvenirs = Integer.valueOf(dataSnapshot.child(currentTripKey).child("Souvenirs").getValue().toString());
        Tickets = Integer.valueOf(dataSnapshot.child(currentTripKey).child("Tickets").getValue().toString());
        Others = Integer.valueOf(dataSnapshot.child(currentTripKey).child("Others").getValue().toString());

        setTripBudgetInfo(money, expenseCategory);
    }

    public void setTripBudgetInfo(int money, String expenseCategory){
        switch(expenseCategory){
            case "Budget":
                Budget += money;
                mRef.child(currentTripKey).child("Budget").setValue(Budget);
                break;
            case "Accommodation":
                Accommodation += money;
                mRef.child(currentTripKey).child("Accommodation").setValue(Accommodation);
                break;
            case "Food":
                Food += money;
                mRef.child(currentTripKey).child("Food").setValue(Food);
                break;
            case "Shopping":
                Shopping += money;
                mRef.child(currentTripKey).child("Shopping").setValue(Shopping);
                break;
            case "Souvenirs":
                Souvenirs += money;
                mRef.child(currentTripKey).child("Souvenirs").setValue(Souvenirs);
                break;
            case "Tickets":
                Tickets += money;
                mRef.child(currentTripKey).child("Tickets").setValue(Tickets);
                break;
            case "Others":
                Others += money;
                mRef.child(currentTripKey).child("Others").setValue(Others);
                break;
            default:
                break;
        }
    }

    /*private void changeData(DataSnapshot dataSnapshot) {
        boolean findTrip = false;
        if(tripName != null){
            for(DataSnapshot ds : dataSnapshot.getChildren()){
                //Log.d("testtttt", "tripName -> " + ds.child(tripName).child("tripName").getValue().toString());
                if (ds.child(tripName).child("tripName").getValue().equals(tripName)){
                    Log.d("newTest", "in");
                    findTrip = true;
                    budget = Integer.valueOf(ds.child(tripName).child("budget").getValue().toString());
                    accommodation = Integer.valueOf(ds.child(tripName).child("accommodation").getValue().toString());
                    food = Integer.valueOf(ds.child(tripName).child("food").getValue().toString());
                    shopping = Integer.valueOf(ds.child(tripName).child("shopping").getValue().toString());
                    souvenir = Integer.valueOf(ds.child(tripName).child("souvenir").getValue().toString());
                    ticket = Integer.valueOf(ds.child(tripName).child("ticket").getValue().toString());
                    other = Integer.valueOf(ds.child(tripName).child("other").getValue().toString());
                    Log.d("newTest", "budget -> " + ds.child(tripName).child("budget").getValue());
                    Log.d("newTest", "accommodation -> " + ds.child(tripName).child("accommodation").getValue());
                    Log.d("newTest", "food -> " + ds.child(tripName).child("food").getValue());
                    Log.d("newTest", "shopping -> " + ds.child(tripName).child("shopping").getValue());
                    Log.d("newTest", "souvenir -> " + ds.child(tripName).child("souvenir").getValue());
                    Log.d("newTest", "ticket -> " + ds.child(tripName).child("ticket").getValue());
                    Log.d("newTest", "other -> " + ds.child(tripName).child("other").getValue());
                    tempTripBudget = new TripBudget(tripName, budget, accommodation, food, shopping, souvenir, ticket, other);
                    tempTripBudget.setTripBudgetInfo(tripName, money, expenseCategory);
                    break;
                }
            }
        }



            if (findTrip)mRef.child("Trip").child(tripName).setValue(tempTripBudget);
            else{
                tempTripBudget = new TripBudget();
                tempTripBudget.setTripBudgetInfo(tripName, money, expenseCategory);
                mRef.child("Trip").child(tripName).setValue(tempTripBudget);
            }
    }*/
}
