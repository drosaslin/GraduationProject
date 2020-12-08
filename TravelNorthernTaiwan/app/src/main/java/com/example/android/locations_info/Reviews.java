package com.example.android.locations_info;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by David Rosas on 9/16/2018.
 */

class Reviews implements Parcelable {
    private String text;

    private String profile_photo_url;

    private String relative_time_description;

    private String author_url;

    private String author_name;

    private String rating;

    private String language;

    protected Reviews(Parcel in) {
        text = in.readString();
        profile_photo_url = in.readString();
        relative_time_description = in.readString();
        author_url = in.readString();
        author_name = in.readString();
        rating = in.readString();
        language = in.readString();
    }

    public static final Creator<Reviews> CREATOR = new Creator<Reviews>() {
        @Override
        public Reviews createFromParcel(Parcel in) {
            return new Reviews(in);
        }

        @Override
        public Reviews[] newArray(int size) {
            return new Reviews[size];
        }
    };

    public String getText ()
    {
        return text;
    }

    public void setText (String text)
    {
        this.text = text;
    }

    public String getProfile_photo_url ()
    {
        return profile_photo_url;
    }

    public void setProfile_photo_url (String profile_photo_url)
    {
        this.profile_photo_url = profile_photo_url;
    }

    public String getRelative_time_description ()
    {
        return relative_time_description;
    }

    public void setRelative_time_description (String relative_time_description)
    {
        this.relative_time_description = relative_time_description;
    }

    public String getAuthor_url ()
    {
        return author_url;
    }

    public void setAuthor_url (String author_url)
    {
        this.author_url = author_url;
    }

    public String getAuthor_name ()
    {
        return author_name;
    }

    public void setAuthor_name (String author_name)
    {
        this.author_name = author_name;
    }

    public String getRating ()
    {
        return rating;
    }

    public void setRating (String rating)
    {
        this.rating = rating;
    }

    public String getLanguage ()
    {
        return language;
    }

    public void setLanguage (String language)
    {
        this.language = language;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [text = "+text+", profile_photo_url = "+profile_photo_url+", relative_time_description = "+relative_time_description+", author_url = "+author_url+", author_name = "+author_name+", rating = "+rating+", language = "+language+"]";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(text);
        parcel.writeString(profile_photo_url);
        parcel.writeString(relative_time_description);
        parcel.writeString(author_url);
        parcel.writeString(author_name);
        parcel.writeString(rating);
        parcel.writeString(language);
    }
}
