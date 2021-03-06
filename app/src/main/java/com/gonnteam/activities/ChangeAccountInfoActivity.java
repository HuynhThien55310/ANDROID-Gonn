package com.gonnteam.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gonnteam.R;
import com.gonnteam.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class ChangeAccountInfoActivity extends AppCompatActivity {
    private EditText txtFname, txtLname,
            txtAge, txtHeight, txtWeight;
    private TextView  txtBMI, txtTDEE;
    private RadioButton rdNam, rdNu;
    private Spinner spinner;
    private String arr[]
            = {"Không vận động", "Vận động nhẹ",
            "Vận động vừa phải", "Vận động nhiều",
            "Vận động nặng"};
    private Button btnSave;
    private FirebaseAuth mAuth;
    private User user;
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
        btnSave = findViewById(R.id.btnSave);
        txtHeight = findViewById(R.id.txtHeight);
        txtWeight = findViewById(R.id.txtWeight);
        txtAge = findViewById(R.id.txtAge);
        txtBMI = findViewById(R.id.txtBMI);
        txtTDEE = findViewById(R.id.txtTDEE);
        rdNam = findViewById(R.id.rdNam);
        rdNu = findViewById(R.id.rdNu);
        spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, arr);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinner.setAdapter(adapter);
    }

    private void addEvents(){
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateInfo();
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
        try{
            if(user.isUpdated){
                double bmi = user.getBMI();
                double bmr, tdee;
                bmr = user.getBMR();
                tdee = user.getTDEE(bmr);
                txtTDEE.setText("Lượng calo cần thiết 1 ngày của bạn là: " + Math.round(tdee));
                if (bmi < 18.5){
                    // gầy
                    txtBMI.setText("BMI: " + bmi);
                    txtBMI.append(". Bạn thuộc nhóm người gầy, bạn nên cung cấp thêm dinh dưỡng cho cơ thể nhé");
                }else if (bmi > 24.9){
                    // mập
                    txtBMI.setText("BMI: " + bmi);
                    txtBMI.append(". Bạn thuộc nhóm người béo, bạn nên hạn chế khẩu phần ăn và tập luyện thêm nhé");
                } else {
                    // cân đối
                    txtBMI.setText("BMI: " + bmi);
                    txtBMI.append(". Bạn thuộc nhóm người cân đối, cố gắng giữ vững phong độ nhé");
                }
            }
            txtLname.setText(user.getLastName());
            txtFname.setText(user.getFirstName());
            spinner.setSelection(user.getActivity_level());
            if (user.gender == 0){
                rdNam.setChecked(true);
            }else {
                rdNu.setChecked(true);
            }
            if (user.getAge() == 0){
                txtAge.setText("");
            }else {
                txtAge.setText(user.age + "");
            }

            if (user.getHeight() == 0){
                txtHeight.setText("");
            }else {
                txtHeight.setText(user.getHeight() + "");
            }

            if (user.getHeight() == 0){
                txtWeight.setText("");
            }else {
                txtWeight.setText(user.getWeight() + "");
            }
        }catch (NullPointerException e){
            txtAge.setText("");
            txtHeight.setText("");
            txtWeight.setText("");
            rdNam.setChecked(true);
            spinner.setSelection(0);
        }

    }

    public void updateInfo(){
        try {
            if(rdNam.isChecked()){
                user.setGender(0);
            }else user.setGender(1);
            user.setHeight(Double.parseDouble(txtHeight.getText().toString()));
            user.setWeight(Double.parseDouble(txtWeight.getText().toString()));
            user.setAge(Integer.parseInt(txtAge.getText().toString()));
            user.setActivity_level(spinner.getSelectedItemPosition());
            user.setFirstName(txtFname.getText().toString());
            user.setLastName(txtLname.getText().toString());
            user.setUpdated(true);
            FirebaseFirestore.getInstance().collection("users")
                    .document(user.getUid()).set(user)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            setUserInfo();
                            Toast.makeText(ChangeAccountInfoActivity.this,"Cập nhật thành công",Toast.LENGTH_SHORT).show();

                        }
                    });
        }catch (NumberFormatException e){
            Toast.makeText(ChangeAccountInfoActivity.this,"Bạn chưa nhập đủ thông tin",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        getUserInfo();
    }
}
