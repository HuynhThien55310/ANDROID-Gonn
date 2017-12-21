package com.gonnteam.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.gonnteam.R;

public class ChangePasswordActivity extends AppCompatActivity {
    private EditText txtOldPass, txtNewPass, txtReNewPass;
    private Button btnSave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        addControls();
        addEvents();
    }

    private void addControls(){


    }

    private void addEvents(){
        changePassword();
    }

    public void changePassword(){

    }
}
