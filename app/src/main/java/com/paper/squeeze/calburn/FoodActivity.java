package com.paper.squeeze.calburn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.card.MaterialCardView;
import com.paper.squeeze.calburn.CustomAdapter.FoodAdapter;
import com.paper.squeeze.calburn.CustomClass.First;
import com.paper.squeeze.calburn.CustomClass.Second;
import com.paper.squeeze.calburn.CustomClass.Third;
import com.paper.squeeze.calburn.CustomInterface.FoodInterface;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;

public class FoodActivity extends AppCompatActivity implements FoodInterface {

    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;
    RecyclerView recyclerView;
    public static ArrayList<First> DietList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        collapsingToolbarLayout = findViewById(R.id.collapsingToolbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);

        //to load only once
        if (DietList.isEmpty()) {
            InputStream is = getResources().openRawResource(R.raw.diet);
            Writer writer = new StringWriter();
            char[] buffer = new char[1024];
            try {
                Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } catch (Exception e) {
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            String jsonString = writer.toString();

            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray jsonArray = jsonObject.getJSONArray("Diet");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject first = jsonArray.getJSONObject(i);
                    String firstName = first.getString("name");
                    ArrayList<Second> secondarray = new ArrayList<>();
                    JSONArray secondData = first.getJSONArray("data");
                    for (int j = 0; j < secondData.length(); j++) {
                        JSONObject second = secondData.getJSONObject(j);
                        String secondDescription = second.getString("description");
                        String secondTitle = second.getString("title");
                        String secondImage = second.getString("image");
                        ArrayList<Third> thirdarray = new ArrayList<>();
                        JSONObject secondD = second.getJSONObject("data");
                        String secondDetail = secondD.getString("detail");
                        JSONArray thirdData = secondD.getJSONArray("list");
                        for (int k = 0; k < thirdData.length(); k++) {
                            JSONObject third = thirdData.getJSONObject(k);
                            String thirdName = third.getString("name");
                            String thirdKcal = third.getString("kcal");
                            String thirdServing = third.getString("serving_100");
                            Third thirdObj = new Third(thirdKcal, thirdName, thirdServing);
                            thirdarray.add(thirdObj);
                        }
                        Second secondObj = new Second(secondDetail, secondDescription, secondImage, secondTitle, thirdarray);
                        secondarray.add(secondObj);
                    }
                    First firstObj = new First(firstName, secondarray);
                    DietList.add(firstObj);
                }
            } catch (Exception e) {
            }
        }

        recyclerView = findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        FoodAdapter adapter = new FoodAdapter(DietList,this);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //for back arrow at toolbar
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return  super.onOptionsItemSelected(item);
    }

    @Override
    public void foodClick(int position, MaterialCardView view) {
        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this,view,"title");
        Intent in = new Intent(FoodActivity.this,FoodDetailActivity.class);
        ArrayList<Third> thirdArrayList = new ArrayList<>();
        First first = DietList.get(position);
        for (Second second:first.getData()){
            thirdArrayList.addAll(second.getList());
        }
        Bundle args = new Bundle();
        args.putSerializable("LIST",(Serializable)thirdArrayList);
        in.putExtra("BUNDLE",args);
        in.putExtra("title",DietList.get(position).getName());
        startActivity(in,activityOptionsCompat.toBundle());
    }
}
