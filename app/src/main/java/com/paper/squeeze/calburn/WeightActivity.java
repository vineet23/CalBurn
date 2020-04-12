package com.paper.squeeze.calburn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

public class WeightActivity extends AppCompatActivity {

    ImageView back;
    TextInputEditText weight;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight);

        weight = findViewById(R.id.weight_edit);
        fab = findViewById(R.id.fab);

        final SharedPreferences sharedPreferences = getSharedPreferences("shared",MODE_PRIVATE);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (sharedPreferences.getInt("weight", 0) != Integer.parseInt(weight.getText().toString())
                            && Integer.parseInt(weight.getText().toString())>0) {
                        //todo update at firebase and save at preference
                    } else {
                        finish();
                    }
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),"Please fill the weight properly",Toast.LENGTH_SHORT).show();
                }
            }
        });

        weight.setText(sharedPreferences.getInt("weight",0)+"");

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
