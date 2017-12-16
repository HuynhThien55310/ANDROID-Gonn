package com.gonnteam.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.gonnteam.R;
import com.gonnteam.models.Food;
import com.squareup.picasso.Picasso;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by MrThien on 2017-12-14.
 */

public class FoodDetailFragment extends Fragment {
    private Food food;
    private TextView txtTitle;
    private ImageView imgBackdrop;
    private WebView wbvBody;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_food_detail, container, false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        this.food = (Food) getActivity().getIntent().getSerializableExtra("food");
        txtTitle = rootView.findViewById(R.id.txtTitle);
        imgBackdrop = rootView.findViewById(R.id.imgBackdrop);
        wbvBody = rootView.findViewById(R.id.wbvBody);

        txtTitle.setText(food.getTitle());
        Picasso.with(getActivity()).load(food.getBackdrop()).into(imgBackdrop);
        wbvBody.loadData(food.getBody(),"text/html; charset=UTF-8", null);
        return rootView;
    }

    public FoodDetailFragment() {
    }


}
