package com.example.zlater.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.zlater.Activity.ExerciseDetailsActivity;
import com.example.zlater.Model.BodyParts;
import com.example.zlater.Model.Exercise;
import com.example.zlater.R;

import java.util.ArrayList;

public class BodyPartsExercisesAdapter extends RecyclerView.Adapter<BodyPartsExercisesViewHolder> {
    private ArrayList<BodyParts> bodyParts;
    private Context context;

    public BodyPartsExercisesAdapter(ArrayList<BodyParts> bodyParts, Context context) {
        this.bodyParts = bodyParts;
        this.context = context;
    }

    @NonNull
    @Override
    public BodyPartsExercisesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_bodyparts_exercises, parent, false);
        return new BodyPartsExercisesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BodyPartsExercisesViewHolder holder, int position) {
        BodyParts bodyPart = bodyParts.get(position);
        holder.tv_title_bodypart.setText(bodyPart.getTitle());
        holder.rv_bodyparts_exercises.setHasFixedSize(true);
        holder.rv_bodyparts_exercises.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
        ItemExerciseOfBodypartsExercisesAdapter itemExerciseOfBodypartsExercisesAdapter = new ItemExerciseOfBodypartsExercisesAdapter(bodyPart.getExercises(), context, bodyPart);
        holder.rv_bodyparts_exercises.setAdapter(itemExerciseOfBodypartsExercisesAdapter);
    }

    @Override
    public int getItemCount() {
        return bodyParts.size();
    }
}

class ItemExerciseOfBodypartsExercisesAdapter extends RecyclerView.Adapter<ItemExericiseOfBodypartsExercisesViewHolder> {
    private ArrayList<Exercise> exercises;
    private Context context;
    private BodyParts bodyParts;

    public ItemExerciseOfBodypartsExercisesAdapter(ArrayList<Exercise> exercises, Context context, BodyParts bodyParts) {
        this.exercises = exercises;
        this.context = context;
        this.bodyParts = bodyParts;
    }

    @NonNull
    @Override
    public ItemExericiseOfBodypartsExercisesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_exercises_of_bodypart_exercises, parent, false);
        return new ItemExericiseOfBodypartsExercisesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemExericiseOfBodypartsExercisesViewHolder holder, int position) {
        Exercise exercise = exercises.get(position);
        if(exercise != null) {
            Glide.with(context).load(exercise.getImage_url()).centerCrop().into(holder.iv_ex);
            holder.tv_title_ex.setText(exercise.getTitle());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, ExerciseDetailsActivity.class);
                    i.putExtra("id", exercise.getId());
                    i.putExtra("part", bodyParts.getTitle());
                    i.putExtra("bodyPartId", bodyParts.getId());
                    context.startActivity(i);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }
}

class ItemExericiseOfBodypartsExercisesViewHolder extends RecyclerView.ViewHolder {
    protected ImageView iv_ex;
    protected TextView tv_title_ex;
    public ItemExericiseOfBodypartsExercisesViewHolder(@NonNull View itemView) {
        super(itemView);
        iv_ex = itemView.findViewById(R.id.iv_item_exercise_of_bodyparts_exercises);
        tv_title_ex = itemView.findViewById(R.id.tv_item_exercise_of_bodyparts_exercises);
    }
}

class BodyPartsExercisesViewHolder extends RecyclerView.ViewHolder {
    protected TextView tv_title_bodypart;
    protected RecyclerView rv_bodyparts_exercises;
    public BodyPartsExercisesViewHolder(@NonNull View itemView) {
        super(itemView);
        tv_title_bodypart = itemView.findViewById(R.id.tv_item_title_bodyparts_exercises);
        rv_bodyparts_exercises = itemView.findViewById(R.id.rv_item_bodyparts_exercises);
    }
}
