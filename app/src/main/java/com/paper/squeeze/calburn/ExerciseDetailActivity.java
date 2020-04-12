package com.paper.squeeze.calburn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ExerciseDetailActivity extends AppCompatActivity {

    ImageView back;
    LottieAnimationView lottieAnimationView;
    TextView name,count;
    FloatingActionButton fab;
    boolean play = false;
    Chronometer chronometer;
    long pauseOffset;
    long time;
    AlertDialog alertDialog;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseFirestore db;
    int offset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_detail);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exit();
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
                    lottieAnimationView.pauseAnimation();
                    pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
                }else{
                    fab.setImageDrawable(getDrawable(R.drawable.ic_action_pause));
                    play = true;
                    chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
                    chronometer.start();
                    lottieAnimationView.playAnimation();
                }
            }
        });

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        setOffset();

        //to count the calories burned
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                time = SystemClock.elapsedRealtime() - chronometer.getBase();
                int calBurn = (int)(time/offset);
                count.setText(calBurn+"");
            }
        });
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    public void setOffset(){
        String exercise = name.getText().toString();
        if (exercise.equals(getString(R.string.run))){
            offset = 5000;
        }else if (exercise.equals(getString(R.string.cycle))){
            offset = 6000;
        }else if (exercise.equals(getString(R.string.pranayam))){
            offset = 60000;
        }else if (exercise.equals(getString(R.string.pushups))){
            offset = 8000;
        }else if (exercise.equals(getString(R.string.pullups))){
            offset = 7000;
        }else if (exercise.equals(getString(R.string.situps))){
            offset = 10000;
        }else{
            //skipping
            offset = 4000;
        }
    }

    public void exit(){
        if (time<120000){
            AlertDialog.Builder builder = new AlertDialog.Builder(ExerciseDetailActivity.this);
            builder.setTitle("Alert");
            builder.setMessage("You must exercise for minimum 2 minutes to make the changes to calories");
            builder.setCancelable(false);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    alertDialog.dismiss();
                    supportFinishAfterTransition();
                }
            });
            alertDialog = builder.create();
            alertDialog.show();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(ExerciseDetailActivity.this);
            builder.setTitle("Wait");
            builder.setMessage("Updating calories ...");
            builder.setCancelable(false);
            alertDialog = builder.create();
            alertDialog.show();
            db.collection("users")
                    .whereEqualTo("id",user.getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful() && !task.getResult().isEmpty()){
                                DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                                Map<String, Object> userp = new HashMap<>();
                                int actualCal = getSharedPreferences("shared",MODE_PRIVATE).getInt("calories",0);
                                int burCal = Integer.parseInt(count.getText().toString());
                                final int updateCal = actualCal-burCal;
                                final int fupdateCal = Math.max(updateCal, 0);
                                userp.put("calories", fupdateCal);
                                db.collection("users")
                                        .document(documentSnapshot.getId())
                                        .update(userp)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    SharedPreferences sharedPreferences = getSharedPreferences("shared",MODE_PRIVATE);
                                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                                    editor.putInt("calories",fupdateCal);
                                                    editor.commit();
                                                    Toast.makeText(getApplicationContext(),"Updated!",Toast.LENGTH_SHORT).show();
                                                    alertDialog.dismiss();
                                                    supportFinishAfterTransition();
                                                }else{
                                                    Toast.makeText(getApplicationContext(),getString(R.string.error),Toast.LENGTH_SHORT).show();
                                                    alertDialog.dismiss();
                                                    supportFinishAfterTransition();
                                                }
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(),getString(R.string.network),Toast.LENGTH_SHORT).show();
                                        alertDialog.dismiss();
                                        supportFinishAfterTransition();
                                    }
                                });
                            }else{
                                Toast.makeText(getApplicationContext(),getString(R.string.error),Toast.LENGTH_SHORT).show();
                                alertDialog.dismiss();
                                supportFinishAfterTransition();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(),getString(R.string.network),Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                    supportFinishAfterTransition();
                }
            });
        }
    }
}
