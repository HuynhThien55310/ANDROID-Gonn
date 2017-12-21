package com.gonnteam.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;

import com.gonnteam.R;

public class ChangeAccountInfoActivity extends AppCompatActivity {
    private EditText txtFname, txtLname;
    private DatePicker dpBirthday;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_account_info);
        addControls();
        addEvents();
    }

    private void addControls(){

    }

    private void addEvents(){

    }

    public void updateInfo(){

    }
}
