package com.example.zlater.Adapter.Home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.zlater.Activity.ExercisesActivity;
import com.example.zlater.Model.BodyParts;
import com.example.zlater.R;

import java.util.ArrayList;

public class HomeBodypartsAdapter extends RecyclerView.Adapter<HomeBodypartsViewHolder> {
    private ArrayList<BodyParts> bodyParts;
    private Context context;
    private String[] bParts = {"Tay", "Chân", "Bụng", "Ngực", "Vai & Lưng"};

    public HomeBodypartsAdapter(ArrayList<BodyParts> bodyParts, Context context) {
        this.bodyParts = bodyParts;
        this.context = context;
    }

    @NonNull
    @Override
    public HomeBodypartsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_item_bodypart_home, parent, false);
        return new HomeBodypartsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeBodypartsViewHolder holder, int position) {
        BodyParts bodypart = bodyParts.get(position);
        if(bodypart.getId() == 0) {
            holder.tv_title.setGravity(Gravity.CENTER);
            holder.tv_title.setText("Các bộ phận");
            holder.tv_title.setTextColor(context.getResources().getColor(R.color.title));
            holder.tv_description.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            holder.tv_description.setTextColor(Color.parseColor("#407088"));
            holder.tv_description.setText("Các bài tập này tập trung vào các nhóm cơ cụ thể.");
        } else {
            Glide.with(context).load(bodypart.getImage_url()).centerCrop().into(holder.iv_bg);
            holder.tv_title.setText(bodypart.getTitle());
            setMetadata(bodypart.getTitle(), holder);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, ExercisesActivity.class);
                    i.putExtra("id", bodypart.getId());
                    i.putExtra("title", bodypart.getTitle());
                    context.startActivity(i);
                }
            });
        }
    }

    private void setMetadata(String title, HomeBodypartsViewHolder holder) {
        switch (title) {
            case "Tay":
                holder.tv_description.setText("Bài tập cho cơ tay sau, tay trước");
                break;
            case "Chân":
                holder.tv_description.setText("Phát triển toàn diện cơ chân, đùi và mông");
                break;
            case "Bụng":
                holder.tv_description.setText("Trở nên 6 múi cùng chúng tôi");
                break;
            case "Ngực":
                holder.tv_description.setText("Phát triển tối đa cơ ngực");
                break;
            default:
                holder.tv_description.setText("Bài tập cho cơ lưng, xô và vai");
        }
    }

    @Override
    public int getItemCount() {
        return bodyParts.size();
    }
}

class HomeBodypartsViewHolder extends RecyclerView.ViewHolder {
    protected ImageView iv_bg;
    protected TextView tv_title, tv_description;

    public HomeBodypartsViewHolder(@NonNull View itemView) {
        super(itemView);
        iv_bg = itemView.findViewById(R.id.iv_item_bodypart_home);
        tv_title = itemView.findViewById(R.id.tv_title_bodypart_home);
        tv_description = itemView.findViewById(R.id.tv_description_bodypart_home);
    }
}
