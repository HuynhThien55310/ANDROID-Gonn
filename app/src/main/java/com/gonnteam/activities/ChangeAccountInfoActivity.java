package com.gonnteam.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DownloadManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gonnteam.R;
import com.gonnteam.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.Date;

public class ChangeAccountInfoActivity extends AppCompatActivity {
    private EditText txtFname, txtLname;
    private DatePicker dpBirthday;
    private Button btnSave, btnBirthday;
    private FirebaseAuth mAuth;
    private User user;
    private Calendar cal;
    private Date date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_account_info);
        addControls();
        addEvents();
    }

    private void addControls(){
        txtFname = findViewById(R.id.txtFName);
        txtLname = findViewById(R.id.txtLName);
        btnBirthday = findViewById(R.id.btnBirthday);
        btnSave = findViewById(R.id.btnSave);
        cal=Calendar.getInstance();

    }

    private void addEvents(){
        btnBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog.OnDateSetListener callback = new DatePickerDialog.OnDateSetListener() {
                    //Sự kiện khi click vào nút Done trên Dialog
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        // Set text cho textView
                        btnBirthday.setText(day + "/" + (month +1) + "/" + year);
                        //Lưu vết lại ngày mới cập nhật
                        cal.set(year, month, day);
                        date = cal.getTime();
                    }
                };
                String s = btnBirthday.getText().toString();
                int nam, thang, ngay;
                if (user.getDateOfBirth() == ""){
                    nam = 1990;
                    thang = Calendar.getInstance().get(Calendar.MONTH + 1);
                    ngay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                }else {
                    String strArrtmp[]= s.split("/");
                    ngay=Integer.parseInt(strArrtmp[0]);
                    thang=Integer.parseInt(strArrtmp[1]) - 1;
                    nam=Integer.parseInt(strArrtmp[2]);
                }
                //Hiển thị ra Dialog
                DatePickerDialog pic = new DatePickerDialog(
                        ChangeAccountInfoActivity.this, AlertDialog.THEME_HOLO_DARK,
                        callback, nam, thang, ngay);
                pic.setTitle("Chọn ngày sinh");
                pic.show();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ChangeAccountInfoActivity.this,"abc",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getUserInfo(){
        mAuth = FirebaseAuth.getInstance();
        CollectionReference mUserRef = FirebaseFirestore.getInstance().collection("users");
        mUserRef.document(mAuth.getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    user = task.getResult().toObject(User.class);
                    setUserInfo();
                }
            }
        });

    }

    public void setUserInfo(){
        if (user.getDateOfBirth() == ""){
            btnBirthday.setText("Ngày sinh");
        }else {
            btnBirthday.setText(user.getDateOfBirth());
        }
        txtLname.setText(user.getLastName());
        txtFname.setText(user.getFirstName());
    }

    public void updateInfo(){

    }

    @Override
    protected void onStart() {
        super.onStart();
        getUserInfo();
    }
}
