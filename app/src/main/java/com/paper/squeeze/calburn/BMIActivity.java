package com.paper.squeeze.calburn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class BMIActivity extends AppCompatActivity {

    ImageView back;
    TextInputEditText bmi;
    float BMI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b_m_i);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        bmi = findViewById(R.id.bmi_edit);

        try {
            SharedPreferences sharedPreferences = getSharedPreferences("shared", MODE_PRIVATE);
            int weight = sharedPreferences.getInt("weight", 0);
            float height = (float) sharedPreferences.getInt("height", 0)*0.01f;
            BMI = (float) weight / (height * height);
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),getString(R.string.error),Toast.LENGTH_SHORT).show();
        }

        bmi.setText(String.format("%.2f", BMI));
        bmi.setTextColor(getResources().getColor(R.color.colorPrimary));
    }
}
