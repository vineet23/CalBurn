package com.paper.squeeze.calburn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //todo check if user is logged in and decide
        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                    }
                }, 3000);
    }
}
