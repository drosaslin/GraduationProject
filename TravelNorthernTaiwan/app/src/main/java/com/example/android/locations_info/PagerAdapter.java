package com.example.android.locations_info;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.android.locations_info.LocationPhotosFragment;
import com.example.android.locations_info.LocationReviewsFragment;

/**
 * Created by David Rosas on 9/27/2018.
 */

public class PagerAdapter extends FragmentPagerAdapter {
    private int mNoOfTabs;
    LocationDetailsResponse data;
    private LocationReviewsFragment reviewsFragment;
    private LocationPhotosFragment photosFragment;

    public PagerAdapter(FragmentManager manager, int tabs, LocationDetailsResponse newData) {
        super(manager);
        mNoOfTabs = tabs;
        data = newData;
        reviewsFragment = new LocationReviewsFragment();
        photosFragment = new LocationPhotosFragment();
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", data);

        switch (position) {
            case 0:
                reviewsFragment.setArguments(bundle);
                return reviewsFragment;
            case 1:
                photosFragment.setArguments(bundle);
                return photosFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNoOfTabs;
    }
}
