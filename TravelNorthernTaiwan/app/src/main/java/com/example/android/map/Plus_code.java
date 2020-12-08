package com.example.android.map;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by David Rosas on 9/5/2018.
 */

public class Plus_code implements Parcelable {
    private String compound_code;

    private String global_code;

    protected Plus_code(Parcel in) {
        compound_code = in.readString();
        global_code = in.readString();
    }

    public static final Creator<Plus_code> CREATOR = new Creator<Plus_code>() {
        @Override
        public Plus_code createFromParcel(Parcel in) {
            return new Plus_code(in);
        }

        @Override
        public Plus_code[] newArray(int size) {
            return new Plus_code[size];
        }
    };

    public String getCompound_code ()
    {
        return compound_code;
    }

    public void setCompound_code (String compound_code)
    {
        this.compound_code = compound_code;
    }

    public String getGlobal_code ()
    {
        return global_code;
    }

    public void setGlobal_code (String global_code)
    {
        this.global_code = global_code;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [compound_code = "+compound_code+", global_code = "+global_code+"]";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(compound_code);
        parcel.writeString(global_code);
    }
}
