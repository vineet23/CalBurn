package com.paper.squeeze.calburn;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.paper.squeeze.calburn.CustomClass.MainMenus;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView;
    //FloatingActionButton fab;
    TextView weight,height,calories;
    FirebaseAuth auth;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }catch (Exception e){}

        auth = FirebaseAuth.getInstance();

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        TextView name = headerView.findViewById(R.id.name);
        TextView mail = headerView.findViewById(R.id.mailId);

        FirebaseUser user = auth.getCurrentUser();
        name.setText(user.getDisplayName());
        mail.setText(user.getEmail());

        sharedPreferences = getSharedPreferences("shared",MODE_PRIVATE);

        weight = findViewById(R.id.weight_text);
        height = findViewById(R.id.height_text);
        calories = findViewById(R.id.cal_text);

        weight.setText(sharedPreferences.getInt("weight",0)+" kg");
        height.setText(sharedPreferences.getInt("height",0)+" cm");
        calories.setText(sharedPreferences.getInt("calories",0)+" cal");

        /*fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //to start activity for results to calculate calories
                startActivityForResult(new Intent(MainActivity.this,FoodActivity.class),601);
            }
        });*/

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_bmi){
            startActivity(new Intent(MainActivity.this,BMIActivity.class));
        }else if (id == R.id.nav_diet){
            startActivityForResult(new Intent(MainActivity.this,FoodActivity.class),601);
        }else if (id == R.id.nav_cal){
            startActivityForResult(new Intent(MainActivity.this,CaloriesActivity.class),201);
        }else if (id == R.id.nav_weight){
            startActivityForResult(new Intent(MainActivity.this,WeightActivity.class),301);
        }else if (id == R.id.nav_height){
            startActivityForResult(new Intent(MainActivity.this,HeightActivity.class),401);
        }else if (id== R.id.nav_exercise){
            startActivityForResult(new Intent(MainActivity.this,ExerciseActivity.class),501);
        }else if (id == R.id.nav_logout){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Logout");
            builder.setMessage("Do you want to logout?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestIdToken(getString(R.string.clientID))
                            .requestEmail()
                            .build();
                    GoogleSignInClient mGoogleSignInClient= GoogleSignIn.getClient(MainActivity.this, gso);
                    mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                auth.signOut();
                                startActivity(new Intent(MainActivity.this,SplashActivity.class));
                                finish();
                            }
                        }
                    });
                }
            });
            builder.setNegativeButton("No",null);
            builder.show();
        }else if (id == R.id.nav_share){
            String shareBody = "\n https://drive.google.com/open?id=1Tcd3J-et91Q9fJcQG0VogtN-vd6KL2aG \n";
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.sharesubject));
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, getString(R.string.sharetext)+shareBody);
            startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.sharetitle)));
        }else{
            //for feedback
            //open feedback form
            String url = "https://www.papersqueeze.com/form/calburn";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            // Always use string resources for UI text. This says something like "Share this photo with"
            String title = getString(R.string.feedbacktext);
            // Create and start the chooser
            Intent chooser = Intent.createChooser(i, title);
            startActivity(chooser);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==201){
            weight.setText(sharedPreferences.getInt("weight",0)+" kg");
            height.setText(sharedPreferences.getInt("height",0)+" cm");
            calories.setText(sharedPreferences.getInt("calories",0)+" cal");
        }else if (requestCode==301){
            weight.setText(sharedPreferences.getInt("weight",0)+" kg");
            height.setText(sharedPreferences.getInt("height",0)+" cm");
            calories.setText(sharedPreferences.getInt("calories",0)+" cal");
        }else if (requestCode==401){
            weight.setText(sharedPreferences.getInt("weight",0)+" kg");
            height.setText(sharedPreferences.getInt("height",0)+" cm");
            calories.setText(sharedPreferences.getInt("calories",0)+" cal");
        }else if (requestCode==501){
            weight.setText(sharedPreferences.getInt("weight",0)+" kg");
            height.setText(sharedPreferences.getInt("height",0)+" cm");
            calories.setText(sharedPreferences.getInt("calories",0)+" cal");
        }else if (requestCode==601){
            weight.setText(sharedPreferences.getInt("weight",0)+" kg");
            height.setText(sharedPreferences.getInt("height",0)+" cm");
            calories.setText(sharedPreferences.getInt("calories",0)+" cal");
        }
    }
}
