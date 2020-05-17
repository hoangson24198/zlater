//package com.hcmunre.polyfit_app.Adapter;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import Reminder;
//import com.hcmunre.polyfit_app.R;
//
//import java.util.List;
//
//public class DayReminderAdapter extends RecyclerView.Adapter<DayReminderAdapter.ViewHolder>  {
//    List<Reminder> listReminder;
//    Context context;
//
//    public DayReminderAdapter(List<Reminder> listReminder, Context context) {
//        this.listReminder = listReminder;
//        this.context = context;
//
//    }
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
//        View itemView = layoutInflater.inflate(R.layout.one_item_day, parent, false);
//
//
//        return new ViewHolder(itemView);
//    }
//
//    @Override
//    public void onBindViewHolder(ViewHolder holder, final int position) {
//        holder.tv_day.setText(String.valueOf(listReminder.get(position).get));
//        holder.setItemClickListener(new ItemClickListener() {
//            @Override
//            public void onClick(View view, int posittion) {
//                Toast.makeText(context, "Click on", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return listReminder.size();
//    }
//
//
//
//    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//        TextView tv_day;
//        private ItemClickListener itemClickListener;
//
//
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            itemView.setOnClickListener(this);
//            tv_day=(TextView) itemView.findViewById(R.id.tv_day);
//
//        }
//        public void setItemClickListener(ItemClickListener itemClickListener){
//            this.itemClickListener=itemClickListener;
//        }
//        @Override
//        public void onClick(View v) {
//            itemClickListener.onClick(v,getAdapterPosition());
//
//        }
//    }
//
//}
