package com.gonnteam.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.gonnteam.R;
import com.gonnteam.databinding.ActivitySignupBinding;
import com.gonnteam.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Text;

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
    private TextView txtBirth;
    private Button btnSignup;
    private Button btnLogin;

    private FirebaseAuth mAuth;
    private User user;
    private DocumentReference mDocRef;
    private AlertDialog.Builder builder;
    private FirebaseUser fuser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ActivitySignupBinding binding = DataBindingUtil.setContentView(SigupActivity.this, R.layout.activity_signup);
        user = new User("", "", "");
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
        btnLogin = findViewById(R.id.btnLogin);
        mAuth = FirebaseAuth.getInstance();

        // AlertDialog used to show message

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
    }

    private void addEvents() {
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signupUser();
            }
        });

        btnLogin.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SigupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        }));
    }


    private boolean validateUser(String password, String rePass) {

        //// Validate
        // check empty
        if (user.email.isEmpty() || password.isEmpty() || user.firstName.isEmpty() || user.lastName.isEmpty() || rePass.isEmpty()) {
            builder.setTitle("Thông báo")
                    .setMessage("Vui lòng nhập đầy đủ thông tin bạn nhé!")
                    .show();
            return false;
        }
        // check email format
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(user.email).matches()) {
            builder.setMessage("Email không đúng định dạng. Kiểm tra lại bạn nhé!")
                    .show();
            return false;
        }
        // check password
        if (!password.equals(rePass)) {
            builder.setMessage("Mật khẩu không đúng. Kiểm tra lại bạn nhé!")
                    .show();
            return false;
        }
        return true;
    }

    private void signupUser() {
        String password = txtPassword.getText().toString();
        String rePass = txtRePassword.getText().toString();
        if (validateUser(password, rePass)) {
            // Create user
            mAuth.createUserWithEmailAndPassword(user.email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                builder.setMessage("Đăng ký thành công.").show();
                                Intent login = new Intent(SigupActivity.this,LoginActivity.class);
                                startActivity(login);
                                finish();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                try {
                                    throw task.getException();
                                } catch (FirebaseAuthWeakPasswordException passEx) {
                                    builder.setMessage("Mật khẩu phải ít nhất 6 kí tự.")
                                            .show();
                                    return;
                                } catch (FirebaseAuthUserCollisionException emailEx) {
                                    builder.setMessage("Email đã tồn tại.")
                                            .show();
                                    return;
                                } catch (Exception e) {
                                    Log.e(TAG, e.getMessage());
                                }
                                Toast.makeText(SigupActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                            }
                            task.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    fuser = task.getResult().getUser();
                                    mDocRef = FirebaseFirestore.getInstance().document("users/" + fuser.getUid());
                                    user.setAvatar("https://firebasestorage.googleapis.com/v0/b/gonn-86b4a.appspot.com/o/image%2Fprofile.png?alt=media&token=3918c606-0594-4609-bba7-19283b19dee1");
                                    mDocRef.set(user)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d(TAG, "DocumentSnapshot added with ID: " + mDocRef.getId());
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w(TAG, "Error adding document", e);
                                                }
                                            });
                                }
                            });
                        }
                    });
        }
    }
}
