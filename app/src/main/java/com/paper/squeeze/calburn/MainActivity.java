package com.paper.squeeze.calburn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.paper.squeeze.calburn.CustomClass.MainMenus;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FloatingActionButton fab;
    NavigationView navigationView;
    RecyclerView recyclerView;
    ArrayList<MainMenus> mainMenus = new ArrayList<>();
    MainAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }catch (Exception e){}

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);

        recyclerView = findViewById(R.id.recyclerView);
        fab = findViewById(R.id.fab);

        mainMenus.add(new MainMenus(getString(R.string.bmi),getDrawable(R.drawable.bmi),getResources().getColor(R.color.bmi)));
        mainMenus.add(new MainMenus(getString(R.string.diet),getDrawable(R.drawable.food),getResources().getColor(R.color.diet)));
        mainMenus.add(new MainMenus(getString(R.string.calories),getDrawable(R.drawable.cal),getResources().getColor(R.color.cal)));
        mainMenus.add(new MainMenus(getString(R.string.weight),getDrawable(R.drawable.weight),getResources().getColor(R.color.weight)));
        mainMenus.add(new MainMenus(getString(R.string.height),getDrawable(R.drawable.height),getResources().getColor(R.color.height)));
        mainMenus.add(new MainMenus(getString(R.string.exercise),getDrawable(R.drawable.excer),getResources().getColor(R.color.exercise)));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),RecyclerView.HORIZONTAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new MainAdapter(mainMenus);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_bmi){

        }else if (id == R.id.nav_diet){

        }else if (id == R.id.nav_cal){

        }else if (id == R.id.nav_weight){

        }else if (id == R.id.nav_height){

        }else if (id== R.id.nav_exercise){

        }else if (id == R.id.nav_share){

        }else{
            //for feedback
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
