package com.gonnteam.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.gonnteam.R;
import com.gonnteam.models.Food;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import static com.facebook.FacebookSdk.getApplicationContext;



public class FoodDetailFragment extends Fragment {
    private Food food;
    private TextView txtTitle;
    private ImageView imgBackdrop;
    private WebView wbvBody;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_food_detail, container, false);

        String body = getActivity().getIntent().getStringExtra("body");
        String title = getActivity().getIntent().getStringExtra("title");
        String backdrop = getActivity().getIntent().getStringExtra("backdrop");
        // String foodID = getActivity().getIntent().getStringExtra("foodID");
        txtTitle = rootView.findViewById(R.id.txtTitle);
        imgBackdrop = rootView.findViewById(R.id.imgBackdrop);
        wbvBody = rootView.findViewById(R.id.wbvBody);
        txtTitle.setText(title);
        if (backdrop.contains("data:image/jpeg;base64")) {
            backdrop = backdrop.substring(backdrop.indexOf(",") + 1);
            byte[] decodedString = Base64.decode(backdrop, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imgBackdrop.setImageBitmap(decodedByte);
        } else {
            Picasso.with(getActivity()).load(backdrop).into(imgBackdrop);
        }

        wbvBody.loadData("<style>img{display: inline;height: auto;max-width: 100%;}</style>" + body,"text/html; charset=UTF-8", null);
        return rootView;
    }

    public void setFood(Food food){
        this.food = food;
    }

    public FoodDetailFragment() {

    }
}
