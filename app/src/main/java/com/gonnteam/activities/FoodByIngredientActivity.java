package com.gonnteam.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.gonnteam.R;
import com.gonnteam.adapters.FoodAdapter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import static com.facebook.FacebookSdk.getApplicationContext;

public class FoodByIngredientActivity extends AppCompatActivity {
    private RecyclerView revFoodDiscover;
    private FoodAdapter adapter;
    private Query query;
    private String tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_by_ingredient);
        addControls();
    }

    private void addControls() {
        revFoodDiscover = findViewById(R.id.revDiscover);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        tag = getIntent().getStringExtra("tag");
        query = FirebaseFirestore.getInstance()
                .collection("foods")
                .whereEqualTo(tag, true);
        adapter = new FoodAdapter(query, this, this);
        revFoodDiscover.setAdapter(adapter.getAdapter());
        revFoodDiscover.setLayoutManager(layoutManager);
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
