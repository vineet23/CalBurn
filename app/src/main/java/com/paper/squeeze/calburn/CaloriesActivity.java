package com.paper.squeeze.calburn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

public class CaloriesActivity extends AppCompatActivity {

    ImageView back;
    TextInputEditText calories;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calories);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        final SharedPreferences sharedPreferences = getSharedPreferences("shared",MODE_PRIVATE);

        calories = findViewById(R.id.calories_edit);
        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (sharedPreferences.getInt("calories", 0) != Integer.parseInt(calories.getText().toString())
                            && Integer.parseInt(calories.getText().toString())>0) {
                        //todo update at firebase and save at preference
                    } else {
                        finish();
                    }
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),"Please fill the calories properly",Toast.LENGTH_SHORT).show();
                }
            }
        });

        calories.setText(sharedPreferences.getInt("calories",0)+"");
    }
}
