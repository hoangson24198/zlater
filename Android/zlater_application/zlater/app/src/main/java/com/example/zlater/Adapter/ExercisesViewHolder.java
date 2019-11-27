package com.example.zlater.Adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.zlater.R;

public class ExercisesViewHolder extends RecyclerView.ViewHolder {
    public TextView tv_title;
    public ImageView iv_image;
    public TextView tv_placeholder;

    public ExercisesViewHolder(@NonNull View itemView) {
        super(itemView);
        tv_title = itemView.findViewById(R.id.tv_item_title_ex);
        iv_image = itemView.findViewById(R.id.iv_item_image_ex);
        tv_placeholder = itemView.findViewById(R.id.tv_item_placeholder_ex_home);
    }
}
