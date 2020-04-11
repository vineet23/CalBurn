package com.paper.squeeze.calburn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.MenuItem;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.paper.squeeze.calburn.CustomAdapter.ExerciseAdapter;
import com.paper.squeeze.calburn.CustomClass.Exercise;
import com.paper.squeeze.calburn.CustomInterface.ExerciseInterface;

import java.util.ArrayList;

public class ExerciseActivity extends AppCompatActivity implements ExerciseInterface {

    Toolbar toolbar;
    RecyclerView recyclerView;
    ArrayList<Exercise> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        arrayList.add(new Exercise(getString(R.string.run),getString(R.string.runsub),getResources().getIdentifier("manrun","raw",getPackageName())));
        arrayList.add(new Exercise(getString(R.string.cycle),getString(R.string.cyclesub),getResources().getIdentifier("bicycleorcycling","raw",getPackageName())));
        arrayList.add(new Exercise(getString(R.string.pranayam),getString(R.string.pranayamsub),getResources().getIdentifier("pranayamyoga","raw",getPackageName())));
        arrayList.add(new Exercise(getString(R.string.pullups),getString(R.string.pullupssub),getResources().getIdentifier("pullups","raw",getPackageName())));
        arrayList.add(new Exercise(getString(R.string.pushups),getString(R.string.pushupssub),getResources().getIdentifier("pushups","raw",getPackageName())));
        arrayList.add(new Exercise(getString(R.string.situps),getString(R.string.situpssub),getResources().getIdentifier("situps","raw",getPackageName())));
        arrayList.add(new Exercise(getString(R.string.skipping),getString(R.string.skippingsub),getResources().getIdentifier("skippinggirl","raw",getPackageName())));

        recyclerView = findViewById(R.id.recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),linearLayoutManager.getOrientation()));
        recyclerView.setHasFixedSize(true);
        ExerciseAdapter adapter = new ExerciseAdapter(arrayList,this);
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
    public void onClick(int position, TextView name, LottieAnimationView view) {
        Pair statusAnim = Pair.create(name, "name");
        Pair driverBundleAnim = Pair.create(view, "image");
        ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(ExerciseActivity.this, statusAnim, driverBundleAnim);
        Intent intent = new Intent(ExerciseActivity.this,ExerciseDetailActivity.class);
        intent.putExtra("name",arrayList.get(position).getName());
        intent.putExtra("image",arrayList.get(position).getRaw());
        startActivity(intent, transitionActivityOptions.toBundle());
    }
}
