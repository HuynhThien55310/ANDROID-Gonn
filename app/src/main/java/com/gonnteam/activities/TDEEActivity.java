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
import com.gonnteam.models.User;

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
                User user = new User();
                    double bmr, tdee;
                try {
                    if(rdNam.isChecked()){
                        user.setGender(0);
                    }else user.setGender(1);
                    user.setHeight(Double.parseDouble(txtHeight.getText().toString()));
                    user.setWeight(Double.parseDouble(txtWeight.getText().toString()));
                    user.setAge(Integer.parseInt(txtAge.getText().toString()));
                    bmr = user.getBMR();
                    tdee = user.getTDEE(bmr);
                    txtResult.setText("Lượng calo cần thiết 1 ngày của bạn là: " + Math.round(tdee));
                }catch (NumberFormatException e){
                    Toast.makeText(TDEEActivity.this,"Bạn chưa nhập đủ thông tin",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
