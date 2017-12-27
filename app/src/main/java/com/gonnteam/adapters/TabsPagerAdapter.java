package com.gonnteam.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.gonnteam.R;
import com.gonnteam.fragments.FoodDiscoverFragment;
import com.gonnteam.fragments.MostLikedFoodFragment;
import com.gonnteam.fragments.MostViewedFoodFragment;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

/**
 * Created by MrThien on 2017-11-07.
 */

public class TabsPagerAdapter extends FragmentStatePagerAdapter {
    private Activity activity;
    private String tag;
    public TabsPagerAdapter(FragmentManager fm, Activity activity, String tag) {
        super(fm);
        this.activity = activity;
        this.tag = tag;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return FoodDiscoverFragment.newInstance(tag);
            case 1: return MostViewedFoodFragment.newInstance(tag);
            case 2: return MostLikedFoodFragment.newInstance(tag);
            default: return null;
        }
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0: return activity.getString(R.string.tabDiscoverFood);
            case 1: return activity.getString(R.string.tabMostViewedFood);
            case 2: return activity.getString(R.string.tabMostLikedFood);
            default: return "";
        }
    }

    @Override
    public int getItemPosition(@NonNull Object object) {

        return POSITION_NONE;
    }
}
