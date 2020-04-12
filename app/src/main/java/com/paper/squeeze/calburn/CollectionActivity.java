package com.paper.squeeze.calburn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

public class CollectionActivity extends AppCompatActivity {

    TextInputEditText age,weight,height;
    AutoCompleteTextView gender;
    FloatingActionButton fab;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);

        age = findViewById(R.id.age_edit);
        weight = findViewById(R.id.weight_edit);
        height = findViewById(R.id.height_edit);
        gender = findViewById(R.id.gender_edit);
        fab = findViewById(R.id.fab);
        back = findViewById(R.id.back);

        String[] GENDER = new String[] {"Male","Female","Rather Not Say"};

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        getApplicationContext(),
                        R.layout.dropdown_menu_popup_item,
                        GENDER);
        gender.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check()){
                    //to upload to firebase and save data to local
                    SharedPreferences sharedPreferences = getSharedPreferences("shared",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("gender",gender.getText().toString());
                    editor.putInt("weight",Integer.parseInt(weight.getText().toString()));
                    editor.putInt("height",Integer.parseInt(height.getText().toString()));
                    editor.putInt("age",Integer.parseInt(age.getText().toString()));
                    editor.putInt("calories",0);
                    editor.commit();
                    startActivity(new Intent(CollectionActivity.this,MainActivity.class));
                    finish();
                }
            }
        });
    }

    public boolean check(){
        try {
            if (Integer.parseInt(age.getText().toString()) > 0 && Integer.parseInt(age.getText().toString()) < 199) {
                if (Integer.parseInt(weight.getText().toString())>10){
                    if (Integer.parseInt(height.getText().toString())>5){
                        if (!gender.getText().toString().trim().equals("")){
                            return true;
                        }else{
                            Toast.makeText(getApplicationContext(),"Please select a gender",Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    }else{
                        Toast.makeText(getApplicationContext(),"Please fill the height properly",Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Please fill the weight properly",Toast.LENGTH_SHORT).show();
                    return false;
                }
            }else{
                Toast.makeText(getApplicationContext(),"Please fill the age properly",Toast.LENGTH_SHORT).show();
                return false;
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Please fill the data properly!",Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
