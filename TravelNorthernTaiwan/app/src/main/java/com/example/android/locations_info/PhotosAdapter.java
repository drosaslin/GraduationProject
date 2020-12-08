package com.example.android.locations_info;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.map.Photos;
import com.example.android.travelnortherntaiwan.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by David Rosas on 9/30/2018.
 */

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.PhotosViewHolder>{
    private final String GOOGLE_API_KEY = "AIzaSyCc4acsOQV7rnQ92weHYKO14fvL9wkRpKc";
    private ArrayList<Photos> photos;

    public PhotosAdapter(ArrayList<Photos> newPhotos) {
        photos = newPhotos;
    }

    @NonNull
    @Override
    public PhotosAdapter.PhotosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.map_photos_template, parent, false);
        return new PhotosAdapter.PhotosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PhotosViewHolder holder, int position) {
        String reference = photos.get(position).getPhoto_reference();
        Log.i("PHOTOOO", reference);
        String url = "https://maps.googleapis.com/maps/api/place/photo?&maxwidth=500&photoreference=" + reference + "&key=" + GOOGLE_API_KEY;
        Picasso.get().load(url).into(holder.placeImage);
    }

    @Override
    public int getItemCount() {
        if(photos == null) {
            return 0;
        }

        return photos.size();
    }

    public class PhotosViewHolder extends RecyclerView.ViewHolder {
        ImageView placeImage;

        public PhotosViewHolder(View itemView) {
            super(itemView);

            placeImage = itemView.findViewById(R.id.place_image);
        }
    }
}
