package com.gonnteam.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gonnteam.R;
import com.gonnteam.databinding.ActivitySignupBinding;
import com.gonnteam.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Minh Phuong on 03/11/2017.
 */

public class SigupActivity extends FragmentActivity {
    private static final String TAG = "SigupActivity";
    private EditText txtEmail;
    private EditText txtPassword;
    private EditText txtRePassword;
    private EditText txtFirstName;
    private EditText txtLastName;
    private Button btnSignup;

    private FirebaseAuth mAuth;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ActivitySignupBinding binding = DataBindingUtil.setContentView(SigupActivity.this,R.layout.activity_signup);
        user = new User("","","");
        binding.setUser(user);
        addControls();
        addEvents();
    }

    private void addControls() {
        txtEmail = findViewById(R.id.txtSignupEmail);
        txtPassword = findViewById(R.id.txtSignupPass);
        txtRePassword = findViewById(R.id.txtSignupRePass);
        txtFirstName = findViewById(R.id.txtSignupFName);
        txtLastName = findViewById(R.id.txtSignupLName);
        btnSignup = findViewById(R.id.btnSignup);
        mAuth = FirebaseAuth.getInstance();
    }

    private void addEvents() {
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signupUser();
            }
        });
    }

    private boolean validateUser(String password, String rePass){
        // AlertDialog used to show message
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(SigupActivity.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(SigupActivity.this);
        }
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // continue with delete
            }
        });
        //// Validate
        // check empty
        if (user.email.isEmpty() || password.isEmpty() || user.firstName.isEmpty() || user.lastName.isEmpty() || rePass.isEmpty()){
            builder.setTitle("Thông báo")
                    .setMessage("Vui lòng nhập đầy đủ thông tin bạn nhé!")
                    .show();
            return false;
        }
        // check email format
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(user.email).matches()){
            builder.setMessage("Email không đúng định dạng. Kiểm tra lại bạn nhé!")
                    .show();
            return false;
        }
        // check password
        if (!password.equals(rePass) ){
            builder.setMessage("Mật khẩu không đúng. Kiểm tra lại bạn nhé!")
                    .show();
            return false;
        }
        return true;
    }

    private void signupUser(){
        String password = txtPassword.getText().toString();
        String rePass = txtRePassword.getText().toString();
        if (validateUser(password, rePass)){
            // Create user
            mAuth.createUserWithEmailAndPassword(user.email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                //updateUI(user);
                                Toast.makeText(SigupActivity.this, "Sign up successfully",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(SigupActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                            }

                            // ...
                        }
                    });
        }
    }
}
