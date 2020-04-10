package com.paper.squeeze.calburn;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ExerciseDetailActivity extends AppCompatActivity {

    ImageView back;
    LottieAnimationView lottieAnimationView;
    TextView name;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_detail);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                supportFinishAfterTransition();
            }
        });

        lottieAnimationView = findViewById(R.id.lottie);
        name = findViewById(R.id.name);

        try {
            lottieAnimationView.setAnimation(getIntent().getIntExtra("image", getResources().getIdentifier("flying", "raw", getPackageName())));
            lottieAnimationView.loop(true);
            lottieAnimationView.playAnimation();
            name.setText(getIntent().getStringExtra("name"));
        }catch (Exception e){
            finish();
        }

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
