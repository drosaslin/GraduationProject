package com.example.android.locations_info;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.travelnortherntaiwan.R;

public class LocationReviewsFragment extends Fragment {
    LocationDetailsResponse data;
    RecyclerView reviewsRecycler;
    ReviewsAdapter reviewsAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments().getParcelable("data") != null) {
            data = getArguments().getParcelable("data");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.map_fragment_location_reviews, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        reviewsRecycler = getView().findViewById(R.id.reviews_recycler);
        reviewsRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        reviewsAdapter = new ReviewsAdapter(data.getResult().getReviews());
        reviewsRecycler.setAdapter(reviewsAdapter);

        super.onActivityCreated(savedInstanceState);
    }
}
