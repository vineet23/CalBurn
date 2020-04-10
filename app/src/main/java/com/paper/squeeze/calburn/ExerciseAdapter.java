package com.paper.squeeze.calburn;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.paper.squeeze.calburn.CustomClass.Exercise;

import java.util.ArrayList;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.MyViewHolder> {

    ArrayList<Exercise> arrayList;
    ExerciseInterface exerciseInterface;

    public ExerciseAdapter(ArrayList<Exercise> arrayList,ExerciseInterface exerciseInterface) {
        this.arrayList = arrayList;
        this.exerciseInterface = exerciseInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.exercise_item,parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        LottieAnimationView lottieAnimationView;
        TextView name,sub;
        View view;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            lottieAnimationView = itemView.findViewById(R.id.lottie);
            name = itemView.findViewById(R.id.name);
            sub = itemView.findViewById(R.id.sub);
            view = itemView;
        }

        public void bind(final int position){
            Exercise exercise = arrayList.get(position);
            lottieAnimationView.setAnimation(exercise.getRaw());
            name.setText(exercise.getName());
            sub.setText(exercise.getSub());

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   exerciseInterface.onClick(position,name,lottieAnimationView);
                }
            });
        }
    }
}
