package com.example.android.my_trip;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.travelnortherntaiwan.R;
import com.example.android.trip_organizer.ChooseRegionActivity;
import com.example.android.trip_organizer.TripsAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shashank.sony.fancydialoglib.Animation;
import com.shashank.sony.fancydialoglib.FancyAlertDialog;
import com.shashank.sony.fancydialoglib.FancyAlertDialogListener;
import com.shashank.sony.fancydialoglib.Icon;

import java.util.ArrayList;

public class ToDoListActivity extends AppCompatActivity {
    private ImageView addTaskBtn;
    private RecyclerView mRecyclerView;
    private TasksAdapter mAdapter;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference mRootReference;
    private ArrayList<Task> DataList;
    private String currentKey;
    private EditText taskInput;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_list);
        DataList = new ArrayList<Task>();

        taskInput = findViewById(R.id.task_input);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        currentKey = getIntent().getStringExtra("tripKey");
        mRootReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://travel-northern-taiwan.firebaseio.com/TripTasks/" + currentKey);
        mRecyclerView = findViewById(R.id.task_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        Log.d("ON CREATE", "on create");
        mRootReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mAdapter = new TasksAdapter(DataList, getApplicationContext());
                mAdapter.setOnDeletePressedListener(new TasksAdapter.OnDeletePressedListener() {
                    @Override
                    public void onDeletePressed(int position) {
                        showDeleteAlert(position);
                    }
                });
                mRecyclerView.setAdapter(mAdapter);
                clearCards();
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        addTaskBtn = findViewById(R.id.add_task);
        addTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!taskInput.getText().toString().trim().equals("")) {
                    addTask(taskInput.getText().toString());
                }
                else {
                    Toast.makeText(ToDoListActivity.this, "Please input a task first", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showData(DataSnapshot dataSnapshot) {
        for(DataSnapshot ds : dataSnapshot.getChildren()){
            Task task = new Task();
            String tripTask = ds.getKey().toString();
            Boolean done = (Boolean) ds.getValue();

            task.setTask(tripTask);
            task.setIsDone(done);
            task.setTripKey(currentKey);

            DataList.add(task);
//            mAdapter.addNewTask(task);
        }
    }

    private void addTask(String newTask){
        int n = DataList.size();
        String child = Integer.toString(n);
        mRootReference.child(newTask).setValue(false);
        taskInput.setText("");
    }

    private void clearCards(){
        if(mAdapter != null) {
            mAdapter.clearData();
        }
    }

    public void showDeleteAlert(final int position) {
        new FancyAlertDialog.Builder(ToDoListActivity.this)
                .setTitle("Do you really want to delete this task?")
                .setBackgroundColor(Color.parseColor("#FF0000"))  //Don't pass R.color.colorvalue
                .setNegativeBtnText("Cancel")
                .setPositiveBtnBackground(Color.parseColor("#FF4081"))  //Don't pass R.color.colorvalue
                .setPositiveBtnText("Yes")
                .setNegativeBtnBackground(Color.parseColor("#FFA9A7A8"))  //Don't pass R.color.colorvalue
                .setAnimation(Animation.SLIDE  )
                .isCancellable(true)
                .setIcon(R.drawable.ic_error_outline_black_24dp, Icon.Visible)
                .OnPositiveClicked(new FancyAlertDialogListener() {
                    @Override
                    public void OnClick() {
                        Log.d("DILIT", "DELET");
                        mAdapter.deleteTask(position);
                    }
                })
                .OnNegativeClicked(new FancyAlertDialogListener() {
                    @Override
                    public void OnClick() {
                    }
                })
                .build();
    }
}
