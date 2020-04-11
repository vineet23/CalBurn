
package com.paper.squeeze.calburn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.paper.squeeze.calburn.CustomAdapter.FoodSubAdapter;
import com.paper.squeeze.calburn.CustomClass.Third;
import com.paper.squeeze.calburn.CustomInterface.FoodSubInterface;
import com.paper.squeeze.calburn.R;

import java.util.ArrayList;

public class FoodDetailActivity extends AppCompatActivity implements FoodSubInterface {

    Toolbar toolbar;
    ArrayList<Third> arrayList;
    RecyclerView recyclerView;
    int calories = 0;
    FloatingActionButton fab;
    FoodSubAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getIntent().getStringExtra("title"));
        }
        try {
            Intent intent = getIntent();
            Bundle args = intent.getBundleExtra("BUNDLE");
            arrayList = (ArrayList<Third>) args.getSerializable("LIST");
        }catch (Exception e){
            finishAfterTransition();
        }

        recyclerView = findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),layoutManager.getOrientation()));
        adapter = new FoodSubAdapter(arrayList,this);
        recyclerView.setAdapter(adapter);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //to save the calories changes
                Toast.makeText(getApplicationContext(),calories+"",Toast.LENGTH_SHORT).show();
                finishAfterTransition();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //for back arrow at toolbar
        if (item.getItemId() == android.R.id.home){
            finishAfterTransition();
        }
        return  super.onOptionsItemSelected(item);
    }

    @Override
    public void click(int cal,boolean add) {
        if (add){
            calories = calories + cal;
        }else{
            calories = calories - cal;
        }
    }
}
