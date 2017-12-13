package com.gonnteam.adapters;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.gonnteam.R;
import com.gonnteam.fragments.FoodDiscoverFragment;
import com.gonnteam.fragments.MostLikedFoodFragment;
import com.gonnteam.fragments.MostViewedFoodFragment;

/**
 * Created by MrThien on 2017-11-07.
 */

public class TabsPagerAdapter extends FragmentPagerAdapter {
    private Activity activity;
    public TabsPagerAdapter(FragmentManager fm, Activity activity) {
        super(fm);
        this.activity = activity;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new FoodDiscoverFragment();
            case 1: return new MostViewedFoodFragment();
            case 2: return new MostLikedFoodFragment();
            default: return null;
        }
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
}
