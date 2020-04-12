package com.paper.squeeze.calburn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mAuth = FirebaseAuth.getInstance();

        //todo check if user is logged in and decide
        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        FirebaseUser currentUser = mAuth.getCurrentUser();
                        if(currentUser==null)
                            startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                        else
                            startActivity(new Intent(SplashActivity.this,MainActivity.class));
                        finish();
                    }
                }, 3000);
    }
}
