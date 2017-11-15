package com.gonnteam.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gonnteam.R;
import com.gonnteam.adapters.FoodAdapter;
import com.gonnteam.models.Food;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by MrThien on 2017-11-07.
 */

public class FoodDiscoverFragment extends Fragment {
    RecyclerView revFoodDiscover;
    FoodAdapter foodAdapter;
    List<Food> data;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_food_discover, container, false);
        revFoodDiscover = rootView.findViewById(R.id.revDiscover);
        data = new ArrayList<>();
        foodAdapter = new FoodAdapter(data);
        revFoodDiscover.setAdapter(foodAdapter);
        data.add(new Food());
        data.add(new Food());
        data.add(new Food());
        data.add(new Food());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        revFoodDiscover.setLayoutManager(layoutManager);
        return rootView;
    }

    public FoodDiscoverFragment() {
    }
}
