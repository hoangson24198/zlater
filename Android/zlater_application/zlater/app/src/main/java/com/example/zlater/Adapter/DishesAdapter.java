package com.example.zlater.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.zlater.Interface.ItemClickListener;
import com.example.zlater.Model.Dishes;
import com.example.zlater.R;

import java.util.ArrayList;

public class DishesAdapter  extends RecyclerView.Adapter<DishesViewHolder> {
    private ArrayList<Dishes> dishesArrayList;
    private Context context;
    private com.example.zlater.Interface.ItemClickListener itemClickListener;

    public DishesAdapter(ArrayList<Dishes> dishesArrayList, Context context, ItemClickListener itemClickListener) {
        this.dishesArrayList = dishesArrayList;
        this.context = context;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public DishesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_item_dishes, parent, false);
        return new DishesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DishesViewHolder holder, int position) {
        Dishes dishes = dishesArrayList.get(position);
        holder.title_dishes.setText(dishesArrayList.get(position).getTitle());
        Glide.with(context).load(dishes.getImage_url()).centerCrop().into(holder.iv_bg_dishes);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onClickItem(position);
            }
        });

        //TODO: holder.iv_image use for set image for lat
    }

    @Override
    public int getItemCount() {
        return dishesArrayList.size();
    }
}