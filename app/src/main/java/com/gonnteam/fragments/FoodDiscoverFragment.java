package com.gonnteam.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gonnteam.R;
import com.gonnteam.adapters.FoodAdapter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by MrThien on 2017-11-07.
 */

public class FoodDiscoverFragment extends Fragment {
    private RecyclerView revFoodDiscover;
    private FoodAdapter adapter;
    private Query query;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_food_discover, container, false);
        revFoodDiscover = rootView.findViewById(R.id.revDiscover);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        String tag = getArguments().getString("tag");
        if (tag == "all"){
            query = FirebaseFirestore.getInstance()
                    .collection("foods")
                    .orderBy("postedAt", Query.Direction.DESCENDING);
        } else {
            query = FirebaseFirestore.getInstance()
                    .collection("foods")
                    .whereEqualTo(tag,true);
        }

        adapter = new FoodAdapter(query, getContext());
        revFoodDiscover.setAdapter(adapter.getAdapter());
        revFoodDiscover.setLayoutManager(layoutManager);
        return rootView;
    }

    public FoodDiscoverFragment() {

    }

    public static FoodDiscoverFragment newInstance(String tag){
        FoodDiscoverFragment newFragment = new FoodDiscoverFragment();
        Bundle args = new Bundle();
        args.putString("tag",tag);
        newFragment.setArguments(args);
        return newFragment;
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
