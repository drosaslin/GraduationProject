package com.example.android.map;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by David Rosas on 9/5/2018.
 */

public class Photos implements Parcelable {
    private String photo_reference;

    private String height;

    private String[] html_attributions;

    private String width;


    protected Photos(Parcel in) {
        photo_reference = in.readString();
        height = in.readString();
        html_attributions = in.createStringArray();
        width = in.readString();
    }

    public static final Creator<Photos> CREATOR = new Creator<Photos>() {
        @Override
        public Photos createFromParcel(Parcel in) {
            return new Photos(in);
        }

        @Override
        public Photos[] newArray(int size) {
            return new Photos[size];
        }
    };

    public String getPhoto_reference ()
    {
        return photo_reference;
    }

    public void setPhoto_reference (String photo_reference)
    {
        this.photo_reference = photo_reference;
    }

    public String getHeight ()
    {
        return height;
    }

    public void setHeight (String height)
    {
        this.height = height;
    }

    public String[] getHtml_attributions ()
    {
        return html_attributions;
    }

    public void setHtml_attributions (String[] html_attributions)
    {
        this.html_attributions = html_attributions;
    }

    public String getWidth ()
    {
        return width;
    }

    public void setWidth (String width)
    {
        this.width = width;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [photo_reference = "+photo_reference+", height = "+height+", html_attributions = "+html_attributions+", width = "+width+"]";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(photo_reference);
        parcel.writeString(height);
        parcel.writeStringArray(html_attributions);
        parcel.writeString(width);
    }
}
