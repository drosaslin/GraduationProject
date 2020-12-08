package com.example.android.trip_organizer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.travelnortherntaiwan.R;

import java.util.ArrayList;

public class TripNameAdapter extends ArrayAdapter<TripItem> {
    public TripNameAdapter(Context context, ArrayList<TripItem> tripList){
        super(context, 0,tripList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.trip_spinner_layout, parent, false
            );
        }

        TextView textViewName = convertView.findViewById(R.id.text_view_name);

        TripItem currentItem = getItem(position);

        if(currentItem != null){
            textViewName.setText(currentItem.getTripName());
        }

        return convertView;
    }
}
