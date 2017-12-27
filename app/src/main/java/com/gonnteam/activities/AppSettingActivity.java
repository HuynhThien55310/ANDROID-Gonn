package com.gonnteam.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;

import com.facebook.login.LoginManager;
import com.gonnteam.R;
import com.google.firebase.auth.FirebaseAuth;

public class AppSettingActivity extends AppCompatActivity {
    private ToggleButton btnNotification;
    private Button btnPolicy, btnAppInfo, btnContact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_setting);
        addControls();
        addEvents();
    }

    private void addEvents() {
        btnPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent policy = new Intent(AppSettingActivity.this,AppInfoActivity.class);
                startActivity(policy);
            }
        });
        btnAppInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent appInfo = new Intent(AppSettingActivity.this,AppInfoActivity.class);
                startActivity(appInfo);
            }
        });
        btnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent contact = new Intent(AppSettingActivity.this,AppInfoActivity.class);
                startActivity(contact);
            }
        });
    }

    private void addControls() {
        btnContact = findViewById(R.id.btnContact);
        btnAppInfo = findViewById(R.id.btnAbout);
        btnPolicy = findViewById(R.id.btnPolicy);
        btnNotification = findViewById(R.id.btnNotification);
    }

    public void switchNotification(){

    }
}
