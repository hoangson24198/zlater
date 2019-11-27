package com.example.zlater.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zlater.Activity.ReminderActivity;
import com.example.zlater.Model.Reminder;
import com.example.zlater.R;
import com.example.zlater.Service.local.PolyfitDatabase;
import com.suke.widget.SwitchButton;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ViewHolder> {
    List<Reminder> listReminder;
    Context context;

    public ReminderAdapter(List<Reminder> listReminder, Context context) {
        this.listReminder = listReminder;
        this.context = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.one_item_reminder, parent, false);


        return new ViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if(listReminder.get(position).getHour()<10){
            holder.hours.setText("0"+listReminder.get(position).getHour());
        }else {
            holder.hours.setText(String.valueOf(listReminder.get(position).getHour()));
        }
        if(listReminder.get(position).getMinute()<10){
            holder.minutes.setText( "0"+listReminder.get(position).getMinute());
        }else{
            holder.minutes.setText(String.valueOf(listReminder.get(position).getMinute()));
        }
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int posittion) {
//                Toast.makeText(context, "Click on", Toast.LENGTH_SHORT).show();
            }
        });
        holder.dropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animation;
                if (holder.layoutDay.getVisibility() == View.GONE) {
                    animation = AnimationUtils.loadAnimation(context, R.anim.rotate_bottom);
                    holder.dropdown.startAnimation(animation);
                    holder.layoutDay.setVisibility(View.VISIBLE);
                    holder.layoutDelete.setVisibility(View.VISIBLE);
                } else {
                    animation = AnimationUtils.loadAnimation(context, R.anim.rorate_top);
                    holder.dropdown.startAnimation(animation);
                    holder.layoutDay.setVisibility(View.GONE);
                    holder.layoutDelete.setVisibility(View.GONE);
                }
                if (holder.layoutButton.getVisibility() == View.VISIBLE) {
                    holder.layoutButton.setVisibility(View.GONE);
                }
            }
        });
        if (listReminder.get(position).getMonday() == 1) {
            holder.monday.setImageResource(R.drawable.monday_blue);
        } else {
            holder.monday.setImageResource(R.drawable.monday_white);
        }
        if (listReminder.get(position).getTuesday() == 1) {
            holder.tuesday.setImageResource(R.drawable.tuesday_blue);
        } else {
            holder.tuesday.setImageResource(R.drawable.tuesday_white);
        }
        if (listReminder.get(position).getWednesday() == 1) {
            holder.wednesday.setImageResource(R.drawable.wednesday_blue);
        } else {
            holder.wednesday.setImageResource(R.drawable.wednesday_white);
        }
        if (listReminder.get(position).getThursday() == 1) {
            holder.thursday.setImageResource(R.drawable.thurday_blue);
        } else {
            holder.thursday.setImageResource(R.drawable.thurday_white);
        }
        if (listReminder.get(position).getFriday() == 1) {
            holder.friday.setImageResource(R.drawable.friday_blue);
        } else {
            holder.friday.setImageResource(R.drawable.friday_white);
        }
        if (listReminder.get(position).getSaturday() == 1) {
            holder.saturday.setImageResource(R.drawable.saturday_blue);
        } else {
            holder.saturday.setImageResource(R.drawable.saturday_white);
        }
        if (listReminder.get(position).getSunday() == 1) {
            holder.sunday.setImageResource(R.drawable.sunday_blue);
        } else {
            holder.sunday.setImageResource(R.drawable.sunday_white);
        }if(listReminder.get(position).getTurnOn()==1){
            holder.switchReminder.setChecked(true);
        }else {
            holder.switchReminder.setChecked(false);
        }
        holder.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.layoutDay.getVisibility() == View.VISIBLE) {
                    Animation animation = AnimationUtils.loadAnimation(context, R.anim.rorate_top);
                    holder.dropdown.startAnimation(animation);
                    holder.layoutDay.setVisibility(View.GONE);
                    holder.layoutButton.setVisibility(View.GONE);
                    ((ReminderActivity) context).getListReminder();
                }
//                PolyfitDatabase.getInstance(context).reminderDAO().getReminder();

            }
        });
        holder.btnUpdateAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateReminder(holder,position);
                holder.layoutButton.setVisibility(View.GONE);
            }
        });
        holder.layoutDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PolyfitDatabase.getInstance(context).reminderDAO().delete(listReminder.get(position));
                ((ReminderActivity)context).getListReminder();

            }
        });
        holder.switchReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id=listReminder.get(position).getId();
                Log.e("HS:::","switch");
                if(holder.switchReminder.isChecked()){
                    holder.switchReminder.setChecked(false);
                    PolyfitDatabase.getInstance(context).reminderDAO().switchState(0,id);
                    Log.e("HS:::","Un register");
                }else {
                    holder.switchReminder.setChecked(true);
                    PolyfitDatabase.getInstance(context).reminderDAO().switchState(1,id);
                    Log.e("HS:::","register");
                }
            }
        });
        holder.switchReminder.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                int id=listReminder.get(position).getId();
                Log.e("HS:::","switch");
                if(holder.switchReminder.isChecked()){
                    PolyfitDatabase.getInstance(context).reminderDAO().switchState(1,id);
                    Log.e("HS:::","register");
                }else {
                    PolyfitDatabase.getInstance(context).reminderDAO().switchState(0,id);
                    Log.e("HS:::","Un register");
                }
            }
        });

        changeImage(holder);

    }

    @Override
    public int getItemCount() {
        return listReminder.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView hours;
        TextView minutes;
        ImageView dropdown;
        LinearLayout layoutDay;
        CircleImageView monday, tuesday, wednesday, thursday, friday, saturday, sunday;
        private ItemClickListener itemClickListener;
        LinearLayout layoutButton,layoutDelete;
        CardView btnCancel, btnUpdateAlarm;
        com.suke.widget.SwitchButton switchReminder;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            hours = (TextView) itemView.findViewById(R.id.tv_hours);
            minutes = (TextView) itemView.findViewById(R.id.tv_minutes);
            dropdown = (ImageView) itemView.findViewById(R.id.dropdown);
            layoutDay = (LinearLayout) itemView.findViewById(R.id.layoutDay);
            monday = (CircleImageView) itemView.findViewById(R.id.imv_monday);
            tuesday = (CircleImageView) itemView.findViewById(R.id.imv_tuesday);
            wednesday = (CircleImageView) itemView.findViewById(R.id.imv_wednesday);
            thursday = (CircleImageView) itemView.findViewById(R.id.imv_thursday);
            friday = (CircleImageView) itemView.findViewById(R.id.imv_friday);
            saturday = (CircleImageView) itemView.findViewById(R.id.imv_saturday);
            sunday = (CircleImageView) itemView.findViewById(R.id.imv_sunday);
            layoutButton = (LinearLayout) itemView.findViewById(R.id.layoutButton);
            btnCancel = (CardView) itemView.findViewById(R.id.btnCancel);
            btnUpdateAlarm = (CardView) itemView.findViewById(R.id.btnUpdateAlarm);
            layoutDelete=(LinearLayout) itemView.findViewById(R.id.layoutDelete);
            switchReminder=(com.suke.widget.SwitchButton) itemView.findViewById(R.id.switchReminder);

        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition());

        }
    }

    private void changeImage(ViewHolder holder) {
        holder.monday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.layoutButton.getVisibility() == View.GONE) {
                    holder.layoutButton.setVisibility(View.VISIBLE);
                }
                if (holder.monday.getDrawable().getConstantState() ==
                        ContextCompat.getDrawable(context, R.drawable.monday_blue).getConstantState()) {
                    holder.monday.setImageResource(R.drawable.monday_white);
                } else {
                    holder.monday.setImageResource(R.drawable.monday_blue);
                }
            }
        });

        holder.tuesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.layoutButton.getVisibility() == View.GONE) {
                    holder.layoutButton.setVisibility(View.VISIBLE);
                }
                if (holder.tuesday.getDrawable().getConstantState() ==
                        ContextCompat.getDrawable(context, R.drawable.tuesday_blue).getConstantState()) {
                    holder.tuesday.setImageResource(R.drawable.tuesday_white);
                } else {
                    holder.tuesday.setImageResource(R.drawable.tuesday_blue);
                }
            }
        });

        holder.wednesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.layoutButton.getVisibility() == View.GONE) {
                    holder.layoutButton.setVisibility(View.VISIBLE);
                }
                if (holder.wednesday.getDrawable().getConstantState() ==
                        ContextCompat.getDrawable(context, R.drawable.wednesday_blue).getConstantState()) {
                    holder.wednesday.setImageResource(R.drawable.wednesday_white);
                } else {
                    holder.wednesday.setImageResource(R.drawable.wednesday_blue);
                }
            }
        });

        holder.thursday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.layoutButton.getVisibility() == View.GONE) {
                    holder.layoutButton.setVisibility(View.VISIBLE);
                }
                if (holder.thursday.getDrawable().getConstantState() ==
                        ContextCompat.getDrawable(context, R.drawable.thurday_blue).getConstantState()) {
                    holder.thursday.setImageResource(R.drawable.thurday_white);
                } else {
                    holder.thursday.setImageResource(R.drawable.thurday_blue);
                }
            }
        });

        holder.friday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.layoutButton.getVisibility() == View.GONE) {
                    holder.layoutButton.setVisibility(View.VISIBLE);
                }
                if (holder.friday.getDrawable().getConstantState() ==
                        ContextCompat.getDrawable(context, R.drawable.friday_blue).getConstantState()) {
                    holder.friday.setImageResource(R.drawable.friday_white);
                } else {
                    holder.friday.setImageResource(R.drawable.friday_blue);
                }
            }
        });

        holder.saturday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.layoutButton.getVisibility() == View.GONE) {
                    holder.layoutButton.setVisibility(View.VISIBLE);
                }
                if (holder.saturday.getDrawable().getConstantState() ==
                        ContextCompat.getDrawable(context, R.drawable.saturday_blue).getConstantState()) {
                    holder.saturday.setImageResource(R.drawable.saturday_white);
                } else {
                    holder.saturday.setImageResource(R.drawable.saturday_blue);
                }
            }
        });

        holder.sunday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.layoutButton.getVisibility() == View.GONE) {
                    holder.layoutButton.setVisibility(View.VISIBLE);
                }
                if (holder.sunday.getDrawable().getConstantState() ==
                        ContextCompat.getDrawable(context, R.drawable.sunday_blue).getConstantState()) {
                    holder.sunday.setImageResource(R.drawable.sunday_white);
                } else {
                    holder.sunday.setImageResource(R.drawable.sunday_blue);
                }
            }
        });


    }

    private void updateReminder(ViewHolder holder, int position) {
        int hours = 0, minutes = 0, monday = 0, tuesday = 0, wednesday = 0, thursday = 0, friday = 0, saturday = 0, sunday = 0;
        hours = Integer.parseInt(holder.hours.getText().toString());
        minutes = Integer.parseInt(holder.minutes.getText().toString());
        if (holder.monday.getDrawable().getConstantState() ==
                ContextCompat.getDrawable(context, R.drawable.monday_blue).getConstantState()) {
            monday = 1;
        }
        if (holder.tuesday.getDrawable().getConstantState() ==
                ContextCompat.getDrawable(context, R.drawable.tuesday_blue).getConstantState()) {
            tuesday = 1;
        }
        if (holder.wednesday.getDrawable().getConstantState() ==
                ContextCompat.getDrawable(context, R.drawable.wednesday_blue).getConstantState()) {
            wednesday = 1;
        }
        if (holder.thursday.getDrawable().getConstantState() ==
                ContextCompat.getDrawable(context, R.drawable.thurday_blue).getConstantState()) {
            thursday = 1;
        }
        if (holder.friday.getDrawable().getConstantState() ==
                ContextCompat.getDrawable(context, R.drawable.friday_blue).getConstantState()) {
            friday = 1;
        }
        if (holder.saturday.getDrawable().getConstantState() ==
                ContextCompat.getDrawable(context, R.drawable.saturday_blue).getConstantState()) {
            saturday = 1;
        }
        if (holder.sunday.getDrawable().getConstantState() ==
                ContextCompat.getDrawable(context, R.drawable.sunday_blue).getConstantState()) {
            sunday = 1;
        }

        Reminder reminder = new Reminder();
        reminder.setId(listReminder.get(position).getId());
        reminder.setHour(hours);
        reminder.setMinute(minutes);
        reminder.setMonday(monday);
        reminder.setTuesday(tuesday);
        reminder.setWednesday(wednesday);
        reminder.setThursday(thursday);
        reminder.setFriday(friday);
        reminder.setSaturday(saturday);
        reminder.setSunday(sunday);
        PolyfitDatabase.getInstance(context).reminderDAO().update(reminder);

    }


}
