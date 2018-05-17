package com.gonnteam.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.facebook.login.LoginManager;
import com.gonnteam.R;
import com.google.firebase.auth.FirebaseAuth;

public class AccountSettingActivity extends AppCompatActivity {

    private Button btnChangePass, btnLogout, btnChangeInfo, btnChangeAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setting);
        addControls();
        addEvents();
    }

    private void addEvents() {
        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent changePass = new Intent(AccountSettingActivity.this,ChangePasswordActivity.class);
                startActivity(changePass);
            }
        });

        btnChangeAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent changeAvatar = new Intent(AccountSettingActivity.this,ChangeAvatarActivity.class);
                startActivity(changeAvatar);
            }
        });

        btnChangeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent changeInfo = new Intent(AccountSettingActivity.this,ChangeAccountInfoActivity.class);
                startActivity(changeInfo);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                finish();
            }
        });
    }

    private void addControls() {
        btnLogout = findViewById(R.id.btnLogout);
        btnChangeInfo = findViewById(R.id.btnChangeInfo);
        btnChangeAvatar = findViewById(R.id.btnChangeAva);
        btnChangePass = findViewById(R.id.btnChangePass);
    }
}
