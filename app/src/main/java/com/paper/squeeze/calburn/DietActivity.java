package com.paper.squeeze.calburn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.paper.squeeze.calburn.CustomAdapter.FoodAdapter;
import com.paper.squeeze.calburn.CustomAdapter.FoodSubAdapter;
import com.paper.squeeze.calburn.CustomClass.First;
import com.paper.squeeze.calburn.CustomClass.Second;
import com.paper.squeeze.calburn.CustomClass.Third;
import com.paper.squeeze.calburn.CustomInterface.FoodInterface;
import com.paper.squeeze.calburn.CustomInterface.FoodSubInterface;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class DietActivity extends AppCompatActivity implements FoodSubInterface {

    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;
    RecyclerView recyclerView;
    ArrayList<Third> diet = new ArrayList<>();
    float BMI;
    int calories = 0;
    int total;
    AlertDialog alertDialog;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseFirestore db;
    FoodSubAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        collapsingToolbarLayout = findViewById(R.id.collapsingToolbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);

        recyclerView = findViewById(R.id.recyclerview);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        SharedPreferences sharedPreferences = getSharedPreferences("shared", MODE_PRIVATE);

        //to get the diet if present else create on
        if (sharedPreferences.contains("diet")) {
            Gson gson = new Gson();
            String json = sharedPreferences.getString("diet",null);
            Type type = new TypeToken<ArrayList<Third>>(){}.getType();
            diet = gson.fromJson(json,type);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setHasFixedSize(true);
            recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation()));
            adapter = new FoodSubAdapter(diet, this);
            recyclerView.setAdapter(adapter);
        } else {
            try {
                refreshDiet();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), getString(R.string.error), Toast.LENGTH_SHORT).show();
            }
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshDiet();
            }
        });

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
    public void click(int cal, boolean add) {
        if (add){
            calories = calories + cal;
        }else{
            calories = calories - cal;
        }
    }

    @Override
    public void onBackPressed() {
        if (calories!=0) {
            //update the calories
            AlertDialog.Builder builder = new AlertDialog.Builder(DietActivity.this);
            builder.setTitle("Wait");
            builder.setMessage("Updating calories ...");
            builder.setCancelable(false);
            alertDialog = builder.create();
            alertDialog.show();
            db.collection("users")
                    .whereEqualTo("id", user.getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                                Map<String, Object> userp = new HashMap<>();
                                int actualCal = getSharedPreferences("shared", MODE_PRIVATE).getInt("calories", 0);
                                final int updateCal = actualCal + calories;
                                final int fupdateCal = Math.max(updateCal, 0);
                                userp.put("calories", fupdateCal);
                                db.collection("users")
                                        .document(documentSnapshot.getId())
                                        .update(userp)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    SharedPreferences sharedPreferences = getSharedPreferences("shared", MODE_PRIVATE);
                                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                                    editor.putInt("calories", fupdateCal);
                                                    editor.commit();
                                                    Toast.makeText(getApplicationContext(), "Updated!", Toast.LENGTH_SHORT).show();
                                                    alertDialog.dismiss();
                                                    finish();
                                                } else {
                                                    Toast.makeText(getApplicationContext(), getString(R.string.error), Toast.LENGTH_SHORT).show();
                                                    alertDialog.dismiss();
                                                    finish();
                                                }
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(), getString(R.string.network), Toast.LENGTH_SHORT).show();
                                        alertDialog.dismiss();
                                        finish();
                                    }
                                });
                            } else {
                                Toast.makeText(getApplicationContext(), getString(R.string.error), Toast.LENGTH_SHORT).show();
                                alertDialog.dismiss();
                                finish();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), getString(R.string.network), Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                    finish();
                }
            });
        }else{
            finish();
        }
    }

    //to get the new diet
    public void refreshDiet(){
        Toast.makeText(getApplicationContext(),getString(R.string.dietcreate),Toast.LENGTH_LONG).show();
        diet.clear();
        SharedPreferences sharedPreferences = getSharedPreferences("shared",MODE_PRIVATE);
        //get the bmi
        int weight = sharedPreferences.getInt("weight", 0);
        float height = (float) sharedPreferences.getInt("height", 0) * 0.01f;
        BMI = (float) weight / (height * height);
        if (BMI < 18.5f)
            total = 300;
        else if (BMI > 25f)
            total = 200;
        else
            total = 250;
        //read the json file and add all the food items
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
        ArrayList<First> DietList = new ArrayList<>();
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

            //plan the diet
            for (First first : DietList) {
                //get the list of items
                ArrayList<Third> currentMainItem = new ArrayList<>();
                for (Second second : first.getData()) {
                    currentMainItem.addAll(second.getList());
                }
                int offsetTotal = total;
                while (offsetTotal > 0) {
                    if (!currentMainItem.isEmpty()) {
                        //randomly select an item
                        int r = new Random().nextInt(currentMainItem.size());
                        Third third = currentMainItem.get(r);
                        currentMainItem.remove(r);
                        if (Integer.parseInt(third.getKcal().split(" ")[0]) >= offsetTotal) {
                            offsetTotal = offsetTotal - Integer.parseInt(third.getKcal().split(" ")[0]);
                            diet.add(third);
                        }
                    } else {
                        offsetTotal = 0;
                    }
                }
            }
            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setHasFixedSize(true);
            recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation()));
            adapter = new FoodSubAdapter(diet, this);
            recyclerView.setAdapter(adapter);

            //store the diet in shared preference
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Gson gson = new Gson();
            String json = gson.toJson(diet);
            editor.putString("diet", json);
            editor.apply();
        } catch (Exception e) {
        }
    }

}
