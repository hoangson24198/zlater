package com.example.zlater.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.zlater.Activity.MealsActivity;
import com.example.zlater.Model.Diet;
import com.example.zlater.R;

import java.util.ArrayList;

public class DietFragmentPagerAdapter extends RecyclerView.Adapter<DietFragmentViewHolder> {
    private ArrayList<Diet> diets;
    private Context context;

    public DietFragmentPagerAdapter(ArrayList<Diet> diets, Context context) {
        this.diets = diets;
        this.context = context;
    }

    @NonNull
    @Override
    public DietFragmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_card_diet, parent, false);
        view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return new DietFragmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DietFragmentViewHolder holder, int position) {
        Diet diet = diets.get(position);
        if(diet != null) {
            Glide.with(context).load(diet.getImage_url()).centerCrop().into(holder.iv_diet);
            holder.tv_title.setText(diet.getTitle());
            holder.tv_description.setText(diet.getDescription());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, MealsActivity.class);
                    i.putExtra("title", diet.getTitle());
                    context.startActivity(i);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return diets.size();
    }
}

class DietFragmentViewHolder extends RecyclerView.ViewHolder {
    protected ImageView iv_diet;
    protected TextView tv_title, tv_description;
    public DietFragmentViewHolder(@NonNull View itemView) {
        super(itemView);
        iv_diet = itemView.findViewById(R.id.iv_item_card_diet);
        tv_title = itemView.findViewById(R.id.tv_item_card_diet_title);
        tv_description = itemView.findViewById(R.id.tv_item_card_diet_description);
    }
}
