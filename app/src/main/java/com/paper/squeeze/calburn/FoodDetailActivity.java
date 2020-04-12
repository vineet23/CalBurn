
package com.paper.squeeze.calburn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.paper.squeeze.calburn.CustomAdapter.FoodSubAdapter;
import com.paper.squeeze.calburn.CustomClass.Third;
import com.paper.squeeze.calburn.CustomInterface.FoodSubInterface;
import com.paper.squeeze.calburn.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FoodDetailActivity extends AppCompatActivity implements FoodSubInterface {

    Toolbar toolbar;
    ArrayList<Third> arrayList;
    RecyclerView recyclerView;
    int calories = 0;
    FloatingActionButton fab;
    FoodSubAdapter adapter;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseFirestore db;

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

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

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
                AlertDialog.Builder builder = new AlertDialog.Builder(FoodDetailActivity.this);
                builder.setTitle("Wait");
                builder.setMessage("Updating ...");
                builder.setCancelable(false);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();
                db.collection("users")
                        .whereEqualTo("id",user.getUid())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()){
                                    if (!task.getResult().isEmpty()) {
                                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                                        final SharedPreferences sharedPreferences = getSharedPreferences("shared",MODE_PRIVATE);
                                        Map<String, Object> userp = new HashMap<>();
                                        final int updateCal = calories+sharedPreferences.getInt("calories",0);
                                        userp.put("calories", updateCal);
                                        db.collection("users")
                                                .document(documentSnapshot.getId())
                                                .update(userp)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()){
                                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                                            editor.putInt("calories",updateCal);
                                                            editor.commit();
                                                            alertDialog.dismiss();
                                                            finishAfterTransition();
                                                        }else{
                                                            Toast.makeText(getApplicationContext(),getString(R.string.network),Toast.LENGTH_SHORT).show();
                                                            alertDialog.dismiss();
                                                            finishAfterTransition();
                                                        }
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getApplicationContext(),getString(R.string.network),Toast.LENGTH_SHORT).show();
                                                alertDialog.dismiss();
                                                finishAfterTransition();
                                            }
                                        });
                                    }else {
                                        Toast.makeText(getApplicationContext(),getString(R.string.network),Toast.LENGTH_SHORT).show();
                                        alertDialog.dismiss();
                                        finishAfterTransition();
                                    }
                                }else{
                                    Toast.makeText(getApplicationContext(),getString(R.string.network),Toast.LENGTH_SHORT).show();
                                    alertDialog.dismiss();
                                    finishAfterTransition();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),getString(R.string.network),Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                        finishAfterTransition();
                    }
                });
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
