package com.gonnteam.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gonnteam.R;

public class BMIActivity extends AppCompatActivity {
    private Button btnBMI;
    private TextView txtBMI, txtResult;
    private EditText txtHeight, txtWeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);
        addControls();
        addEvents();
    }

    private void addControls() {
        btnBMI = findViewById(R.id.btnBMI);
        txtBMI = findViewById(R.id.txtBMI);
        txtResult = findViewById(R.id.txtResult);
        txtHeight = findViewById(R.id.txtHeight);
        txtWeight = findViewById(R.id.txtWeight);
    }

    private void addEvents() {
        btnBMI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double height, weight, bmi;
                try {
                    height = Double.parseDouble(txtHeight.getText().toString()) / 100;
                    weight = Double.parseDouble(txtWeight.getText().toString());
                    bmi = calBMI(height,weight);
                    if (bmi < 18.5){
                        // gầy
                        txtBMI.setText("BMI: " + bmi);
                        txtResult.setText("Bạn thuộc nhóm người gầy, bạn nên cung cấp thêm dinh dưỡng cho cơ thể nhé");
                    }else if (bmi > 24.9){
                        // mập
                        txtBMI.setText("BMI: " + bmi);
                        txtResult.setText("Bạn thuộc nhóm người béo, bạn nên hạn chế khẩu phần ăn và tập luyện thêm nhé");
                    } else {
                        // cân đối
                        txtBMI.setText("BMI: " + bmi);
                        txtResult.setText("Bạn thuộc nhóm người cân đối, cố gắng giữ vững phong độ nhé");
                    }
                }catch (NumberFormatException e){
                    Toast.makeText(BMIActivity.this,"Bạn chưa nhập đủ thông tin",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public double calBMI(double height, double weight){
        return Math.floor(weight / (height*height) * 10)/10;
    }
}
