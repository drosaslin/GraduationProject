package com.example.android.locations_info;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.android.map.Geometry;
import com.example.android.map.Opening_hours;
import com.example.android.map.Photos;
import com.example.android.map.Plus_code;

import java.util.ArrayList;

/**
 * Created by David Rosas on 9/16/2018.
 */

public class Result implements Parcelable {
    private ArrayList<Photos> photos;

    private String id;

    private String place_id;

    private String icon;

    private ArrayList<Reviews> reviews;

    private String name;

    private String formatted_address;

    private String formatted_phone_number;

    private Plus_code plus_code;

    private String rating;

    private Opening_hours opening_hours;

    private Geometry geometry;


    protected Result(Parcel in) {
        photos = in.createTypedArrayList(Photos.CREATOR);
        id = in.readString();
        place_id = in.readString();
        icon = in.readString();
        reviews = in.createTypedArrayList(Reviews.CREATOR);
        name = in.readString();
        formatted_address = in.readString();
        formatted_phone_number = in.readString();
        plus_code = in.readParcelable(Plus_code.class.getClassLoader());
        rating = in.readString();
        opening_hours = in.readParcelable(Opening_hours.class.getClassLoader());
        geometry = in.readParcelable(Geometry.class.getClassLoader());
    }

    public static final Creator<Result> CREATOR = new Creator<Result>() {
        @Override
        public Result createFromParcel(Parcel in) {
            return new Result(in);
        }

        @Override
        public Result[] newArray(int size) {
            return new Result[size];
        }
    };

    public ArrayList<Photos> getPhotos ()
    {
        return photos;
    }

    public void setPhotos (ArrayList<Photos> photos)
    {
        this.photos = photos;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getPlace_id ()
    {
        return place_id;
    }

    public void setPlace_id (String place_id)
    {
        this.place_id = place_id;
    }

    public String getIcon ()
    {
        return icon;
    }

    public void setIcon (String icon)
    {
        this.icon = icon;
    }

    public ArrayList<Reviews> getReviews ()
    {
        return reviews;
    }

    public void setReviews (ArrayList<Reviews> reviews)
    {
        this.reviews = reviews;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getFormatted_address ()
    {
        return formatted_address;
    }

    public void setFormatted_address (String formatted_address)
    {
        this.formatted_address = formatted_address;
    }

    public String getFormatted_phone_number ()
    {
        return formatted_phone_number;
    }

    public void setFormatted_phone_number (String formatted_phone_number)
    {
        this.formatted_phone_number = formatted_phone_number;
    }

    public Plus_code getPlus_code ()
    {
        return plus_code;
    }

    public void setPlus_code (Plus_code plus_code)
    {
        this.plus_code = plus_code;
    }

    public String getRating ()
    {
        return rating;
    }

    public void setRating (String rating)
    {
        this.rating = rating;
    }

    public Opening_hours getOpening_hours ()
    {
        return opening_hours;
    }

    public void setOpening_hours (Opening_hours opening_hours)
    {
        this.opening_hours = opening_hours;
    }

    public Geometry getGeometry ()
    {
        return geometry;
    }

    public void setGeometry (Geometry geometry)
    {
        this.geometry = geometry;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [photos = "+photos+", id = "+id+", place_id = "+place_id+", icon = "+icon+", reviews = "+reviews+", name = "+name+", formatted_address = "+formatted_address+", formatted_phone_number = "+formatted_phone_number+", plus_code = "+plus_code+", rating = "+rating+", opening_hours = "+opening_hours+", geometry = "+geometry+"]";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(photos);
        parcel.writeString(id);
        parcel.writeString(place_id);
        parcel.writeString(icon);
        parcel.writeTypedList(reviews);
        parcel.writeString(name);
        parcel.writeString(formatted_address);
        parcel.writeString(formatted_phone_number);
        parcel.writeParcelable(plus_code, i);
        parcel.writeString(rating);
        parcel.writeParcelable(opening_hours, i);
        parcel.writeParcelable(geometry, i);
    }
}
