package com.example.android.travelnortherntaiwan;

/**
 * Created by David Rosas on 10/9/2018.
 */

public class Messenger {
    private static Messenger singletonInstance = null;
    private boolean isTripCanceled = false;
    private boolean isTripFinished = false;
    private String tripKey = "";
    private int count = 0;

    private Messenger() {

    }

    public static Messenger getInstance() {
        if(singletonInstance == null) {
            singletonInstance = new Messenger();
        }

        return singletonInstance;
    }

    public int getCount() {
        return count;
    }

    public void addCount() {
        count++;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isTripCanceled() {
        return isTripCanceled;
    }

    public void setTripCanceled(boolean tripCanceled) {
        isTripCanceled = tripCanceled;
    }

    public boolean isTripFinished() {
        return isTripFinished;
    }

    public void setTripFinished(boolean tripFinished) {
        isTripFinished = tripFinished;
    }

    public String getTripKey() {
        return tripKey;
    }

    public void setTripKey(String tripKey) {
        this.tripKey = tripKey;
    }
}
