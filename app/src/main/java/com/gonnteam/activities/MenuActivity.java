package com.gonnteam.activities;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gonnteam.adapters.FoodAdapter;
import com.gonnteam.adapters.MenuAdapter;
import com.gonnteam.models.FoodMenu;
import com.gonnteam.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Calendar;
import java.util.Date;

import static com.facebook.FacebookSdk.getApplicationContext;

public class MenuActivity extends AppCompatActivity {
    private AlertDialog dialog;
    private EditText txtTitle;
    private TextView txtCustomTitle;

    private FirebaseUser fuser;
    private FirebaseAuth mAuth;
    private CollectionReference mFoodRef;
    private CollectionReference mMenuRef;

    private String uid;
    private String foodID = "";

    private RecyclerView revMenu;
    private MenuAdapter adapter;
    private Query query;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        addControls();
        addEvents();

    }

    private void addEvents() {
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Thêm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                addMenu(txtTitle.getText().toString());
            }
        });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                return;
            }
        });
    }

    private void addControls() {

        // init dialog
        dialog = new AlertDialog.Builder(this).create();
        txtTitle = new EditText(this);

        dialog.setView(txtTitle);
        txtCustomTitle = new EditText(this);
        // edit style dialog o day ne m
        txtCustomTitle.setText("Tên thực đơn");
        txtCustomTitle.setBackgroundColor(Color.DKGRAY);
        txtCustomTitle.setPadding(10, 10, 10, 10);
        txtCustomTitle.setGravity(Gravity.CENTER);
        txtCustomTitle.setTextColor(Color.WHITE);
        txtCustomTitle.setTextSize(20);
        txtCustomTitle.setEnabled(false);
        // end style
        dialog.setCustomTitle(txtCustomTitle);
        dialog.setTitle("Tên thực đơn");

        uid = getIntent().getStringExtra("uid");

        // init recycler view
        revMenu = findViewById(R.id.revMenu);

        query = FirebaseFirestore.getInstance()
                .collection("menus")
                .whereEqualTo("uid",uid)
                .orderBy("createdAt", Query.Direction.DESCENDING);
        foodID = getIntent().getStringExtra("foodID");
        adapter = new MenuAdapter(MenuActivity.this,query,foodID,this );
        revMenu.setAdapter(adapter.getAdapter());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        revMenu.setLayoutManager(layoutManager);
    }

    public void addMenu(String title){
        final FoodMenu menu = new FoodMenu();
        menu.createdAt = Calendar.getInstance().getTime();
        menu.setUid(getIntent().getStringExtra("uid"));
        menu.setTitle(title);
        mMenuRef = FirebaseFirestore.getInstance().collection("menus");
        mMenuRef.add(menu).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                revMenu.getAdapter().notifyDataSetChanged();
                menu.setId(task.getResult().getId());
                mMenuRef.document(menu.getId()).set(menu);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_food_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                dialog.show();
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.getAdapter().startListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        revMenu.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.getAdapter().stopListening();
    }

}
