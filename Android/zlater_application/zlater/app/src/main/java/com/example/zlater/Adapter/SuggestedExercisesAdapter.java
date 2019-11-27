package com.example.zlater.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.zlater.Interface.ItemClickListener;
import com.example.zlater.Model.Exercise;
import com.example.zlater.R;

import java.util.ArrayList;

public class SuggestedExercisesAdapter extends RecyclerView.Adapter<SuggestedExercisesViewHolder> {
    private ArrayList<Exercise> exercises;
    private Context context;
    private com.example.zlater.Interface.ItemClickListener itemClickListener;

    public SuggestedExercisesAdapter(ArrayList<Exercise> exercises, Context context, ItemClickListener itemClickListener) {
        this.exercises = exercises;
        this.context = context;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public SuggestedExercisesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_item_suggested_exercise, parent, false);
        return new SuggestedExercisesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SuggestedExercisesViewHolder holder, int position) {
        if(exercises != null) {
            Exercise exercise = exercises.get(position);
            Glide.with(context).load(exercise.getImage_url()).into(holder.iv_ex);
            holder.tv_content.setText(exercise.getContent());
            holder.tv_title.setText(exercise.getTitle());
            holder.tv_introduction.setText(exercise.getIntroduction());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListener.onClickItem(exercise.getId());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }
}

class SuggestedExercisesViewHolder extends RecyclerView.ViewHolder{
    protected ImageView iv_ex;
    protected TextView tv_title, tv_introduction, tv_content;
    public SuggestedExercisesViewHolder(@NonNull View itemView) {
        super(itemView);
        iv_ex = itemView.findViewById(R.id.iv_suggested_exercise);
        tv_title = itemView.findViewById(R.id.tv_title_suggested_exercise);
        tv_introduction = itemView.findViewById(R.id.tv_introduction_suggested_exercise);
        tv_content = itemView.findViewById(R.id.tv_content_suggested_exercise);
    }
}
