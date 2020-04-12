package com.paper.squeeze.calburn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

public class HeightActivity extends AppCompatActivity {

    ImageView back;
    TextInputEditText height;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_height);

        height = findViewById(R.id.height_edit);
        fab = findViewById(R.id.fab);

        final SharedPreferences sharedPreferences = getSharedPreferences("shared",MODE_PRIVATE);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (sharedPreferences.getInt("height", 0) != Integer.parseInt(height.getText().toString())
                    && Integer.parseInt(height.getText().toString())>5) {
                        //todo update at firebase and save at preference
                    } else {
                        finish();
                    }
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),"Please fill the height properly",Toast.LENGTH_SHORT).show();
                }
            }
        });

        height.setText(sharedPreferences.getInt("height",0)+"");

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
