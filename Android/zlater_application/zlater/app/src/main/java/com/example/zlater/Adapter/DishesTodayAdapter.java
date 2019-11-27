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
import com.example.zlater.Model.Dishes;
import com.example.zlater.R;

import java.util.ArrayList;

public class DishesTodayAdapter  extends RecyclerView.Adapter<DishesTodayViewHolder> {
    private ArrayList<Dishes> dishesArrayList;
    private Context context;
    private com.example.zlater.Interface.ItemClickListener itemClickListener;

    public DishesTodayAdapter(ArrayList<Dishes> dishesArrayList, Context context, ItemClickListener itemClickListener) {
        this.dishesArrayList = dishesArrayList;
        this.context = context;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public DishesTodayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_item_dishes_today, parent, false);
        return new DishesTodayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DishesTodayViewHolder holder, int position) {
        Dishes dishes = dishesArrayList.get(position);
        holder.tv_title.setText(dishesArrayList.get(position).getTitle());
        Glide.with(context).load(dishes.getImage_url()).centerCrop().into(holder.iv_dish);
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

class DishesTodayViewHolder extends RecyclerView.ViewHolder {
    protected ImageView iv_dish;
    protected TextView tv_title;
    public DishesTodayViewHolder(@NonNull View itemView) {
        super(itemView);
        iv_dish = itemView.findViewById(R.id.iv_item_dishes_today);
        tv_title = itemView.findViewById(R.id.tv_title_item_dishes_today);
    }
}
