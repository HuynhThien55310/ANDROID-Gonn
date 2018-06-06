package com.gonnteam.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.gonnteam.R;
import com.gonnteam.adapters.MenuAdapter;
import com.gonnteam.adapters.MenuDetailAdapter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class MenuDetailActivity extends AppCompatActivity {

    private ArrayList<String>  foods;
    private RecyclerView revMenu;
    private MenuDetailAdapter adapter;
    private Query query;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_detail);
        addControls();
        addEvents();
    }

    private void addControls() {
        revMenu = findViewById(R.id.revMenuDetail);

        Bundle bundle = getIntent().getBundleExtra("bundle");
        foods = bundle.getStringArrayList("foods");
        adapter = new MenuDetailAdapter(MenuDetailActivity.this, foods);
        revMenu.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        revMenu.setLayoutManager(layoutManager);
    }

    private void addEvents() {

    }

    @Override
    public void onStart() {
        super.onStart();
        revMenu.getAdapter().notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        revMenu.getAdapter().notifyDataSetChanged();
    }

}
