package com.gonnteam.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gonnteam.R;

public class TDEEActivity extends AppCompatActivity {
    private Button btnTDEE;
    private TextView txtResult;
    private EditText txtHeight, txtWeight, txtAge;
    private RadioButton rdNam;
    private Spinner spinner;
    private String arr[]
            = {"Không vận động", "Vận động nhẹ",
            "Vận động vừa phải", "Vận động nhiều",
            "Vận động nặng"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tdee);
        addControls();
        addEvents();
    }

    private void addControls() {
        btnTDEE = findViewById(R.id.btnTDEE);
        txtResult = findViewById(R.id.txtResult);
        txtHeight = findViewById(R.id.txtHeight);
        txtWeight = findViewById(R.id.txtWeight);
        txtAge = findViewById(R.id.txtAge);
        rdNam = findViewById(R.id.rdNam);
        rdNam.setChecked(true);
        spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, arr);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);

    }

    private void addEvents() {
        btnTDEE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double height, weight,bmr, tdee;
                int age, gender;
                try {
                    if(rdNam.isChecked()){
                        gender = 0;
                    }else gender = 1;
                    height = Double.parseDouble(txtHeight.getText().toString());
                    weight = Double.parseDouble(txtWeight.getText().toString());
                    age = Integer.parseInt(txtAge.getText().toString());
                    bmr = getBMR(height,weight,gender,age);
                    tdee = getTDEE(bmr,spinner.getSelectedItemPosition());
                    txtResult.setText("Lượng calo cần thiết 1 ngày của bạn là: " + Math.round(tdee));
                }catch (NumberFormatException e){
                    Toast.makeText(TDEEActivity.this,"Bạn chưa nhập đủ thông tin",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private double getBMR(double height, double weight, int gender, int age){
        if (gender == 0){
            // nam
            return (13.397 * weight + 4.799 * height) - (5.677 * age) + 88.362;
        }else {
            return (9.247 * weight + 3.098 * height) - (4.330 * age) + 447.593;
        }
    }

    private double getTDEE(double bmr, int activity_level){
        double TDEE = 0;
        switch (activity_level){
            case 0:
                TDEE = bmr * 1.2;
                break;
            case 1:
                TDEE = bmr * 1.375;
                break;
            case 2:
                TDEE = bmr * 1.55;
                break;
            case 3:
                TDEE = bmr * 1.725;
                break;
            case 4:
                TDEE = bmr * 1.9;
                break;
        }
        return  TDEE;
    }
}
