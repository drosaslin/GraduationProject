package com.example.android.my_trip;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.VibrationEffect;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.travelnortherntaiwan.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shashank.sony.fancydialoglib.Animation;
import com.shashank.sony.fancydialoglib.FancyAlertDialog;
import com.shashank.sony.fancydialoglib.FancyAlertDialogListener;
import com.shashank.sony.fancydialoglib.Icon;
import android.os.Vibrator;

import java.util.ArrayList;

public class BudgetManagerActivity extends AppCompatActivity {
    private ArrayList<ExpenseItem> mExpenseList;
    private float money, Budget, Accommodation, Food, Shopping, Souvenirs, Tickets, Others;
    private Button submitBtn;
    private FirebaseDatabase database;
    private DatabaseReference mRef;
    private EditText textMoney;
    private String tripName, expenseCategory, currentTripKey;
    private ExpenseAdapter mAdapter;
    public PieChart pieChart;
    private Vibrator v ;
    private VibrationEffect vibrationEffect;
    private long[] timings = {1500};
    private int[] amplitudes = {255};
    private int repeat = -1;
    private Uri notification;
    private Ringtone r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_manager);
        getSupportActionBar().setTitle("Budget Manager");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);



        tripName = null;

        database = FirebaseDatabase.getInstance();
        String url = "https://travel-northern-taiwan.firebaseio.com/ExpensesByTrip";
        mRef = FirebaseDatabase.getInstance().getReferenceFromUrl(url);

        currentTripKey = getIntent().getExtras().getString("tripKey");

        submitBtn = (Button)findViewById(R.id.button_summit);
        pieChart = (PieChart) findViewById(R.id.pie_chart);

        pieChart.setUsePercentValues(false);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5,10,5,5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(61f);

        initExpenseList();
        Spinner spinnerExpense = findViewById(R.id.spinner_expense);
        mAdapter = new ExpenseAdapter(this, mExpenseList);
        spinnerExpense.setAdapter(mAdapter);
        spinnerExpense.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ExpenseItem clickedItem = (ExpenseItem)parent.getItemAtPosition(position);
                String clickedExpenseName = clickedItem.getmCategoryName();
                expenseCategory = clickedExpenseName;
                Toast.makeText(BudgetManagerActivity.this, clickedExpenseName + " selected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        textMoney = (EditText)findViewById(R.id.editText_money);

        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getData(dataSnapshot);
                setPieChart();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //setChart();
        setPieChart();
        submitBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                if(TextUtils.isEmpty(textMoney.getText().toString().trim())){
                    Log.d("test", "empty");
                    Toast.makeText(BudgetManagerActivity.this, "Please input money", Toast.LENGTH_SHORT).show();
                    return;
                }
                money = Float.parseFloat(textMoney.getText().toString());
                mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        getData(dataSnapshot);
                        setTripBudgetInfo(money, expenseCategory);

                        if (Budget<Accommodation + Food + Shopping + Souvenirs + Tickets + Others){
                            //Toast.makeText(BudgetManagerActivity.this, "over budget !", Toast.LENGTH_SHORT).show();
                            // Vibrate for 500 milliseconds
                            v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                            vibrationEffect = VibrationEffect.createWaveform(timings, amplitudes, repeat);
                            v.vibrate(vibrationEffect);
                            notification = RingtoneManager.getDefaultUri(RingtoneManager.URI_COLUMN_INDEX);
                            r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                            r.play();
                            new FancyAlertDialog.Builder(BudgetManagerActivity.this)
                                    .setTitle("Warning")
                                    .setBackgroundColor(Color.parseColor("#FF0000"))  //Don't pass R.color.colorvalue
                                    .setMessage("Oooooover budget")
                                    .setNegativeBtnText("Cancel")
                                    .setPositiveBtnBackground(Color.parseColor("#FF4081"))  //Don't pass R.color.colorvalue
                                    .setPositiveBtnText("Ok")
                                    .setNegativeBtnBackground(Color.parseColor("#FFA9A7A8"))  //Don't pass R.color.colorvalue
                                    .setAnimation(Animation.SLIDE  )
                                    .isCancellable(true)
                                    .setIcon(R.drawable.ic_error_outline_black_24dp, Icon.Visible)
                                    .OnPositiveClicked(new FancyAlertDialogListener() {
                                        @Override
                                        public void OnClick() {
                                            Toast.makeText(getApplicationContext(),"Ok",Toast.LENGTH_SHORT).show();
                                            r.stop();
                                        }
                                    })
                                    .OnNegativeClicked(new FancyAlertDialogListener() {
                                        @Override
                                        public void OnClick() {
                                            Toast.makeText(getApplicationContext(),"Cancel",Toast.LENGTH_SHORT).show();
                                            setTripBudgetInfo(-1*money, expenseCategory);
                                            setPieChart();
                                            r.stop();
                                        }
                                    })
                                    .build();
                        }
                        setPieChart();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }
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

    private void getData(DataSnapshot dataSnapshot){
        Budget        = Float.parseFloat(dataSnapshot.child(currentTripKey).child("Budget").getValue().toString());
        Accommodation = Float.parseFloat(dataSnapshot.child(currentTripKey).child("Accommodation").getValue().toString());
        Food          = Float.parseFloat(dataSnapshot.child(currentTripKey).child("Food").getValue().toString());
        Shopping      = Float.parseFloat(dataSnapshot.child(currentTripKey).child("Shopping").getValue().toString());
        Souvenirs     = Float.parseFloat(dataSnapshot.child(currentTripKey).child("Souvenirs").getValue().toString());
        Tickets       = Float.parseFloat(dataSnapshot.child(currentTripKey).child("Tickets").getValue().toString());
        Others        = Float.parseFloat(dataSnapshot.child(currentTripKey).child("Others").getValue().toString());
    }

    public void setTripBudgetInfo(float money, String expenseCategory){
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

    private void setPieChart(){
        ArrayList<PieEntry> data = new ArrayList<>();
        if(Accommodation>0)data.add(new PieEntry(Accommodation, "Accommodation"));
        if(Budget>.0)data.add(new PieEntry(Budget, "Budget"));
        if(Food>0)data.add(new PieEntry(Food, "Food"));
        if(Others>0)data.add(new PieEntry(Others, "Others"));
        if(Shopping>0)data.add(new PieEntry(Shopping, "Shopping"));
        if(Souvenirs>0)data.add(new PieEntry(Souvenirs, "Souvenirs"));
        if(Tickets>0)data.add(new PieEntry(Tickets, "Tickets"));

        pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic);

        PieDataSet dataSet = new PieDataSet(data, "Trip Budget");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        PieData pieData = new PieData(dataSet);
        pieData.setValueTextSize(10f);
        pieData.setValueTextColor(Color.YELLOW);

        pieChart.setData(pieData);

        float restF = Budget-(Accommodation + Food + Shopping + Souvenirs + Tickets + Others);
        String rest = Float.toString(restF);

        pieChart.setCenterText("Remaining\n" + rest);
        pieChart.setCenterTextSize(30);


        if(restF<=0)pieChart.setCenterTextColor(Color.RED);
        else if(restF<=1000)pieChart.setCenterTextColor(Color.MAGENTA);
        else pieChart.setCenterTextColor(Color.BLACK);

        Log.d("rest", "rest -> " + Float.toString(Budget-(Accommodation + Food + Shopping + Souvenirs + Tickets + Others)));
    }
}
