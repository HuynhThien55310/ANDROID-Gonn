package com.gonnteam.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.gonnteam.R;
import com.google.firebase.auth.FirebaseAuth;

public class TestActivity extends AppCompatActivity {

    private Button btnLogin, btnSignup, btnLogout, btnMain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        addControls();
        addEvents();
    }

    private void addControls(){
        btnLogin = findViewById(R.id.btnLogin);
        btnSignup = findViewById(R.id.btnSignup);
        btnLogout = findViewById(R.id.btnLogout);
        btnMain = findViewById(R.id.btnMain);
    }

    private void addEvents(){
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent login = new Intent(TestActivity.this,LoginActivity.class);
                startActivity(login);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
            }
        });
        btnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent main = new Intent(TestActivity.this,MainActivity.class);
                startActivity(main);
            }
        });
    }

}
