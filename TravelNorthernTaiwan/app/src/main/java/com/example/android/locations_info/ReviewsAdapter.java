package com.example.android.locations_info;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.android.travelnortherntaiwan.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by David Rosas on 9/29/2018.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsViewHolder> {
    private ArrayList<Reviews> reviews;

    public ReviewsAdapter(ArrayList<Reviews> newReviews) {
        reviews = newReviews;
    }

    @NonNull
    @Override
    public ReviewsAdapter.ReviewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.map_reviews_template, parent, false);
        return new ReviewsAdapter.ReviewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ReviewsAdapter.ReviewsViewHolder holder, final int position) {
        holder.reviewer.setText(reviews.get(position).getAuthor_name());
        holder.reviewTime.setText(reviews.get(position).getRelative_time_description());
        holder.reviewRating.setRating(Float.parseFloat(reviews.get(position).getRating()));
        holder.review.setText(reviews.get(position).getText());
    }

    @Override
    public int getItemCount() {
        if(reviews == null) {
            return 0;
        }

        return reviews.size();
    }

    public class ReviewsViewHolder extends RecyclerView.ViewHolder {
        TextView reviewer;
        TextView reviewTime;
        RatingBar reviewRating;
        TextView review;

        public ReviewsViewHolder(View itemView) {
            super(itemView);

            reviewer = itemView.findViewById(R.id.reviewer_name);
            reviewTime = itemView.findViewById(R.id.review_time);
            reviewRating = itemView.findViewById(R.id.review_rating);
            review = itemView.findViewById(R.id.review);
        }
    }
}
