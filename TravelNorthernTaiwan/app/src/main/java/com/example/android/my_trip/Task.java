package com.example.android.my_trip;

import java.io.Serializable;

public class Task implements Serializable{
    String tripKey;
    String task;
    boolean isDone;

    public boolean getIsDone() {
        return isDone;
    }

    public void setIsDone(boolean isDone) {
        this.isDone = isDone;
    }

    public Task(){
        tripKey = "";
        task = "";
        isDone = false;
    }

    public String getTripKey() {
        return tripKey;
    }

    public void setTripKey(String tripKey) {
        this.tripKey = tripKey;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

}
