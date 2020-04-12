package com.paper.squeeze.calburn.CustomAdapter;

import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.paper.squeeze.calburn.CustomClass.Third;
import com.paper.squeeze.calburn.CustomInterface.FoodSubInterface;
import com.paper.squeeze.calburn.R;

import java.util.ArrayList;

public class FoodSubAdapter extends RecyclerView.Adapter<FoodSubAdapter.MyViewHolder> {

    ArrayList<Third> arrayList;
    FoodSubInterface foodSubInterface;
    private SparseBooleanArray sparseBooleanArray=new SparseBooleanArray();

    public FoodSubAdapter(ArrayList<Third> arrayList,FoodSubInterface foodSubInterface) {
        this.arrayList = arrayList;
        this.foodSubInterface = foodSubInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.food_sub_item,parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder,int position) {
        final Third third = arrayList.get(position);
        holder.checkBox.setOnCheckedChangeListener(null);
        if (sparseBooleanArray.get(position)) {
            holder.checkBox.setChecked(true);
        }else {
            holder.checkBox.setChecked(false);
        }
        holder.title.setText(third.getName());
        holder.power.setText(third.getKcal()+"/"+third.getServing());
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        TextView title,power;
        ConstraintLayout constraintLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            constraintLayout = itemView.findViewById(R.id.constraint);
            checkBox = itemView.findViewById(R.id.checkbox);
            title = itemView.findViewById(R.id.title);
            power = itemView.findViewById(R.id.power);
        }

        public void bind(){
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    foodSubInterface.click(Integer.parseInt(arrayList.get(getAdapterPosition()).getKcal().split(" ")[0]) ,b);
                    if (sparseBooleanArray.get(getAdapterPosition(),false)) {
                        sparseBooleanArray.delete(getAdapterPosition());
                        checkBox.setChecked(false);
                    }else{
                        sparseBooleanArray.put(getAdapterPosition(), true);
                        checkBox.setChecked(true);
                    }
                }
            });
        }
    }

}
