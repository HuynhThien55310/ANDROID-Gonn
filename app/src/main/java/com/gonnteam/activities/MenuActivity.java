package com.gonnteam.activities;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gonnteam.R;

public class MenuActivity extends AppCompatActivity {
    private AlertDialog dialog;
    private EditText txtTitle;
    private TextView txtCustomTitle;
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

    }

    private void addControls() {


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
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Thêm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(MenuActivity.this,txtTitle.getText(),Toast.LENGTH_SHORT).show();
            }
        });
        dialog.setTitle("Tên thực đơn");

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

}
