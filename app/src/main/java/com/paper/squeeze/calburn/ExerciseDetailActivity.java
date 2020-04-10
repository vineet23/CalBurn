package com.paper.squeeze.calburn;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Locale;

public class ExerciseDetailActivity extends AppCompatActivity {

    ImageView back;
    LottieAnimationView lottieAnimationView;
    TextView name,count;
    FloatingActionButton fab;
    boolean play = false;
    Chronometer chronometer;
    long pauseOffset;

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

        chronometer = findViewById(R.id.time);
        count = findViewById(R.id.calcount);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (play){
                    fab.setImageDrawable(getDrawable(R.drawable.ic_action_play));
                    play = false;
                    chronometer.stop();
                    pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
                }else{
                    fab.setImageDrawable(getDrawable(R.drawable.ic_action_pause));
                    play = true;
                    chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
                    chronometer.start();
                }
            }
        });

        //to count the calories burned
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                long time = SystemClock.elapsedRealtime() - chronometer.getBase();
            }
        });
    }


}
