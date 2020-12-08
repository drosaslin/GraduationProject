package com.example.android.my_trip;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.travelnortherntaiwan.R;
import com.example.android.trip_organizer.TripsAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.ViewHolder>{
    ArrayList<Task> DataList;
    Context context;
    private DatabaseReference mRootReference;
    private OnDeletePressedListener onDeletePressed;

    public TasksAdapter(ArrayList<Task> newTripList, Context newContext) {
        DataList = newTripList;
        context = newContext;
        mRootReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://travel-northern-taiwan.firebaseio.com/");
    }

    @NonNull
    @Override
    public TasksAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tasks_template, parent, false);
        return new TasksAdapter.ViewHolder(view);
    }

    public void clearData() {
        DataList.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final TasksAdapter.ViewHolder holder, final int position) {
        String task = DataList.get(position).getTask();
        final boolean isDone = DataList.get(position).getIsDone();
        holder.taskName.setText(task);
        holder.isDoneChecked.setChecked(DataList.get(position).getIsDone());

        //Deleting a card
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDeletePressed.onDeletePressed(position);
//                deleteTasks(position);
//                updateDatabase();
//                DataList.remove(position);
//                showAllData();
//                Log.d("delete", DataList.get(position).toString() + " " + DataList.get(position).getTripKey());
//                if(DataList!=null){
//                    for(Task task : DataList){
//                        boolean isDone = holder.isDoneChecked.isChecked();
//                        String check = (isDone) ? "1" : "0";
//                        if(task.getTask()+check.equals())
//                    }
////                    mRootReference.child("TripTasks").child(DataList.get(position).getTripKey()).child(DataList.get(position).toString()).removeValue();
//                }
            }
        });

        holder.isDoneChecked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                DataList.get(position).setIsDone(isChecked);
                mRootReference.child("TripTasks").child(DataList.get(position).getTripKey()).child(DataList.get(position).getTask()).setValue(buttonView.isChecked());
            }
        });
    }

    private void updateDatabase() {
    }

    private void showAllData() {
        for(Task item : DataList) {
            Log.d("TASKSSSS", item.getTask());
        }
    }

    public void deleteTask(int position) {
        Log.d("DELETINGTASK", DataList.get(position).getTask());
        mRootReference.child("TripTasks").child(DataList.get(position).getTripKey()).child(DataList.get(position).getTask()).removeValue();
        notifyDataSetChanged();
    }

    public interface OnDeletePressedListener {
        void onDeletePressed(int position);
    }

    public void setOnDeletePressedListener(OnDeletePressedListener onDeletePressedListener) {
        onDeletePressed = onDeletePressedListener;
    }

    @Override
    public int getItemCount() {
        return DataList.size();
    }

    public void addNewTask(Task task) {
        DataList.add(task);
        DataList.add(task);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView taskName;
        ImageView deleteBtn;
        CheckBox isDoneChecked;
        CardView taskCard;

        public ViewHolder(View itemView) {
            super(itemView);
            taskName = (itemView).findViewById(R.id.taskName);
            deleteBtn = (itemView).findViewById(R.id.deleteBtn);
            taskCard = (itemView).findViewById(R.id.task_card);
            isDoneChecked = (itemView).findViewById(R.id.isDone);
        }
    }
}
