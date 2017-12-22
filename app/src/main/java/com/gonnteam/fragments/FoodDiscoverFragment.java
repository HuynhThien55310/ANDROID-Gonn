package com.gonnteam.fragments;


import android.app.DownloadManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gonnteam.R;
import com.gonnteam.adapters.FoodFirebaseAdapter;
import com.gonnteam.models.Food;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by MrThien on 2017-11-07.
 */

public class FoodDiscoverFragment extends Fragment {
    RecyclerView revFoodDiscover;
    FoodFirebaseAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_food_discover, container, false);
        revFoodDiscover = rootView.findViewById(R.id.revDiscover);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        Query query = FirebaseFirestore.getInstance()
                .collection("foods")
                .orderBy("postedAt", Query.Direction.DESCENDING)
                .limit(50);
        adapter = new FoodFirebaseAdapter(query, getContext());
        revFoodDiscover.setAdapter(adapter.getAdapter());
        revFoodDiscover.setLayoutManager(layoutManager);
        return rootView;
    }

    public FoodDiscoverFragment() {

    }



    @Override
    public void onStart() {
        super.onStart();
        adapter.getAdapter().startListening();
    }



    @Override
    public void onStop() {
        super.onStop();
        adapter.getAdapter().stopListening();
    }
}
