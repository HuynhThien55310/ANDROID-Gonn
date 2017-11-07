package com.gonnteam.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gonnteam.R;

/**
 * Created by MrThien on 2017-11-07.
 */

public class PartyFoodFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_party_food, container, false);
        return rootView;
    }

    public PartyFoodFragment() {
    }
}
