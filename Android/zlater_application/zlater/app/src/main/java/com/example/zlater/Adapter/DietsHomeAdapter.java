package com.example.zlater.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.zlater.Activity.MealsActivity;
import com.example.zlater.Model.Diet;
import com.example.zlater.R;

import java.util.ArrayList;

public class DietsHomeAdapter extends RecyclerView.Adapter<DietsHomeViewHolder> {
    private ArrayList<Diet> diets;
    private Context context;

    public DietsHomeAdapter(ArrayList<Diet> diets, Context context) {
        this.diets = diets;
        this.context = context;
    }

    @NonNull
    @Override
    public DietsHomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_item_diet_home, parent, false);
        return new DietsHomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DietsHomeViewHolder holder, int position) {
        Diet diet = diets.get(position);
//        holder.iv_img.setImageResource(diet.getImage_url());
        Glide.with(context).load(diet.getImage_url()).centerCrop().into(holder.iv_img);
        holder.tv_title.setText(diet.getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, MealsActivity.class);
                i.putExtra("title", diet.getTitle());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return diets.size();
    }
}

class DietsHomeViewHolder extends RecyclerView.ViewHolder {
    protected ImageView iv_img;
    protected TextView tv_title;
    public DietsHomeViewHolder(@NonNull View itemView) {
        super(itemView);
        iv_img = itemView.findViewById(R.id.iv_item_diet_home);
        tv_title = itemView.findViewById(R.id.tv_item_title_diet_home);
    }
}
