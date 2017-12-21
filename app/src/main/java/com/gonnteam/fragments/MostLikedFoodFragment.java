package com.gonnteam.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gonnteam.R;
import com.gonnteam.adapters.FoodFirebaseAdapter;

/**
 * Created by MrThien on 2017-11-07.
 */

public class MostLikedFoodFragment extends Fragment {
    RecyclerView revFoodDiscover;
    FoodFirebaseAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_most_liked_food, container, false);
        return rootView;
    }

    public MostLikedFoodFragment() {

    }

    @Override
    public void onStart() {
        super.onStart();
       // adapter.getAdapter().startListening();
    }



    @Override
    public void onStop() {
        super.onStop();
      //  adapter.getAdapter().stopListening();
    }
}
