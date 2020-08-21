package com.example.zlater.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.zlater.Adapter.ReminderAdapter;
import com.example.zlater.Model.Reminder;
import com.example.zlater.R;

import com.example.zlater.Service.Broadcast.BootBroadcastReceiver;
import com.example.zlater.Service.local.ZlaterDatabase;
import com.example.zlater.Utils.CheckInternetConnection;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReminderActivity extends AppCompatActivity implements View.OnClickListener {
    private FloatingActionButton fab;
    private Dialog dialog;
    private TextView textTime;
    private CircleImageView monday, tuesday, wednesday, thursday, friday, saturday, sunday;
    private CheckBox everyday;
    private TextView btnSaveAlarm;
    private int hours, minutes;
    private int hoursSet, minutesSet;
    private RecyclerView viewReminder;
    private List<Reminder> listReminder;
    private ReminderAdapter reminderAdapter;
    private ImageView backReminder;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_reminder);
        connectView();
        new CheckInternetConnection(this).checkConnection();
        getListReminder();
        setupReminder();
        registerReceiver(mMessageReceiver, new IntentFilter("registerReminder"));
    }

    private void connectView() {
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.reminder);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        viewReminder = findViewById(R.id.viewReminder);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                showDialog();
                break;
            case R.id.cardSetTime:
                dialogSetTime();
                break;
            case R.id.everyday:
                ifEveryDayChecked();
                break;
            case R.id.monday:
                if (monday.getDrawable().getConstantState() ==
                        ContextCompat.getDrawable(ReminderActivity.this, R.drawable.monday_white).getConstantState()) {
                    monday.setImageResource(R.drawable.monday_blue);
                } else {
                    monday.setImageResource(R.drawable.monday_white);
                    if (everyday.isChecked()) {
                        everyday.setChecked(false);
                    }
                }
                break;
            case R.id.tuesday:
                if (tuesday.getDrawable().getConstantState() ==
                        ContextCompat.getDrawable(ReminderActivity.this, R.drawable.tuesday_white).getConstantState()) {
                    tuesday.setImageResource(R.drawable.tuesday_blue);
                } else {
                    tuesday.setImageResource(R.drawable.tuesday_white);
                    if (everyday.isChecked()) {
                        everyday.setChecked(false);
                    }
                }
                break;

            case R.id.wednesday:
                if (wednesday.getDrawable().getConstantState() ==
                        ContextCompat.getDrawable(ReminderActivity.this, R.drawable.wednesday_white).getConstantState()) {
                    wednesday.setImageResource(R.drawable.wednesday_blue);
                } else {
                    wednesday.setImageResource(R.drawable.wednesday_white);
                    if (everyday.isChecked()) {
                        everyday.setChecked(false);
                    }
                }
                break;
            case R.id.thursday:
                if (thursday.getDrawable().getConstantState() ==
                        ContextCompat.getDrawable(ReminderActivity.this, R.drawable.thurday_white).getConstantState()) {
                    thursday.setImageResource(R.drawable.thurday_blue);
                } else {
                    thursday.setImageResource(R.drawable.thurday_white);
                    if (everyday.isChecked()) {
                        everyday.setChecked(false);
                    }
                }
                break;
            case R.id.friday:
                if (friday.getDrawable().getConstantState() ==
                        ContextCompat.getDrawable(ReminderActivity.this, R.drawable.friday_white).getConstantState()) {
                    friday.setImageResource(R.drawable.friday_blue);
                } else {
                    friday.setImageResource(R.drawable.friday_white);
                    if (everyday.isChecked()) {
                        everyday.setChecked(false);
                    }
                }
                break;

            case R.id.saturday:
                if (saturday.getDrawable().getConstantState() ==
                        ContextCompat.getDrawable(ReminderActivity.this, R.drawable.saturday_white).getConstantState()) {
                    saturday.setImageResource(R.drawable.saturday_blue);
                } else {
                    saturday.setImageResource(R.drawable.saturday_white);
                    if (everyday.isChecked()) {
                        everyday.setChecked(false);
                    }
                }
                break;
            case R.id.sunday:
                if (sunday.getDrawable().getConstantState() ==
                        ContextCompat.getDrawable(ReminderActivity.this, R.drawable.sunday_white).getConstantState()) {
                    sunday.setImageResource(R.drawable.sunday_blue);
                } else {
                    sunday.setImageResource(R.drawable.sunday_white);
                    if (everyday.isChecked()) {
                        everyday.setChecked(false);
                    }
                }
                break;

            case R.id.btnSaveAlarm:
                if (!textTime.getText().equals("Set time")) {
                    saveReminder();
                    setupReminder();
                    registerReceiver(mMessageReceiver, new IntentFilter("registerReminder"));
                    dialog.dismiss();
                } else {
                    Toast.makeText(this, "Please set time for remind !!!", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    private void showDialog() {
        dialog = new Dialog(ReminderActivity.this);
        dialog.setContentView(R.layout.dialog_set_reminder);
        CardView cardSetTime = dialog.findViewById(R.id.cardSetTime);
        textTime = dialog.findViewById(R.id.tv_setTime);
        monday = dialog.findViewById(R.id.monday);
        tuesday = dialog.findViewById(R.id.tuesday);
        wednesday = dialog.findViewById(R.id.wednesday);
        thursday = dialog.findViewById(R.id.thursday);
        friday = dialog.findViewById(R.id.friday);
        saturday = dialog.findViewById(R.id.saturday);
        sunday = dialog.findViewById(R.id.sunday);
        everyday = dialog.findViewById(R.id.everyday);
        btnSaveAlarm = dialog.findViewById(R.id.btnSaveAlarm);
        everyday.setOnClickListener(this);
        monday.setOnClickListener(this);
        tuesday.setOnClickListener(this);
        wednesday.setOnClickListener(this);
        thursday.setOnClickListener(this);
        friday.setOnClickListener(this);
        saturday.setOnClickListener(this);
        sunday.setOnClickListener(this);
        cardSetTime.setOnClickListener(this);
        btnSaveAlarm.setOnClickListener(this);
        dialog.show();
    }

    private void dialogSetTime() {
        final Calendar c = Calendar.getInstance();
        hours = c.get(Calendar.HOUR_OF_DAY);
        minutes = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        hoursSet = hourOfDay;
                        minutesSet = minute;
                        if (hourOfDay < 10) {
                            textTime.setText("0" + hourOfDay + ":" + minute);
                        } else if (minute < 10) {
                            textTime.setText(hourOfDay + ":" + "0" + minute);
                        } else {
                            textTime.setText(hourOfDay + ":" + minute);
                        }
                        if (hourOfDay < 10 && minute < 10) {
                            textTime.setText("0" + hourOfDay + ":" + "0" + minute);
                        }

                    }
                }, hours, minutes, true);

        timePickerDialog.show();

    }

    private void ifEveryDayChecked() {
       if(everyday.isChecked()){
           monday.setImageResource(R.drawable.monday_blue);
           tuesday.setImageResource(R.drawable.tuesday_blue);
           wednesday.setImageResource(R.drawable.wednesday_blue);
           thursday.setImageResource(R.drawable.thurday_blue);
           friday.setImageResource(R.drawable.friday_blue);
           saturday.setImageResource(R.drawable.saturday_blue);
           sunday.setImageResource(R.drawable.sunday_blue);
       }else {
           monday.setImageResource(R.drawable.monday_white);
           tuesday.setImageResource(R.drawable.tuesday_white);
           wednesday.setImageResource(R.drawable.wednesday_white);
           thursday.setImageResource(R.drawable.thurday_white);
           friday.setImageResource(R.drawable.friday_white);
           saturday.setImageResource(R.drawable.saturday_white);
           sunday.setImageResource(R.drawable.sunday_white);
       }

    }

    private void saveReminder() {
        Log.e("HS:::", "Save data");
//                saveReminder();
        Reminder reminder = new Reminder();
        reminder.setHour(hoursSet);
        reminder.setMinute(minutesSet);
        reminder.setTurnOn(1);
        if (monday.getDrawable().getConstantState() ==
                ContextCompat.getDrawable(ReminderActivity.this, R.drawable.monday_blue).getConstantState()) {
            reminder.setMonday(1);
        } else {
            reminder.setMonday(0);
        }
        if (tuesday.getDrawable().getConstantState() ==
                ContextCompat.getDrawable(ReminderActivity.this, R.drawable.tuesday_blue).getConstantState()) {
            reminder.setTuesday(1);
        } else {
            reminder.setTuesday(0);
        }
        if (wednesday.getDrawable().getConstantState() ==
                ContextCompat.getDrawable(ReminderActivity.this, R.drawable.wednesday_blue).getConstantState()) {
            reminder.setWednesday(1);
        } else {
            reminder.setWednesday(0);
        }
        if (thursday.getDrawable().getConstantState() ==
                ContextCompat.getDrawable(ReminderActivity.this, R.drawable.thurday_blue).getConstantState()) {
            reminder.setThursday(1);
        } else {
            reminder.setThursday(0);
        }
        if (friday.getDrawable().getConstantState() ==
                ContextCompat.getDrawable(ReminderActivity.this, R.drawable.friday_blue).getConstantState()) {
            reminder.setFriday(1);
        } else {
            reminder.setFriday(0);
        }
        if (saturday.getDrawable().getConstantState() ==
                ContextCompat.getDrawable(ReminderActivity.this, R.drawable.saturday_blue).getConstantState()) {
            reminder.setSaturday(1);
        } else {
            reminder.setSaturday(0);
        }
        if (sunday.getDrawable().getConstantState() ==
                ContextCompat.getDrawable(ReminderActivity.this, R.drawable.sunday_blue).getConstantState()) {
            reminder.setSunday(1);
        } else {
            reminder.setSunday(0);
        }
        if (everyday.isChecked()) {
            reminder.setMonday(1);
            reminder.setTuesday(1);
            reminder.setWednesday(1);
            reminder.setThursday(1);
            reminder.setFriday(1);
            reminder.setSaturday(1);
            reminder.setSunday(1);
        }
//        reminderService.insert(reminder);
        ZlaterDatabase.getInstance(ReminderActivity.this).reminderDAO().insert(reminder);
        getListReminder();

    }

    public void getListReminder() {
        listReminder = new ArrayList<>();
        listReminder = ZlaterDatabase.getInstance(ReminderActivity.this).reminderDAO().getReminder();
        viewReminder.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ReminderActivity.this, LinearLayoutManager.VERTICAL, false);
        viewReminder.setLayoutManager(layoutManager);
        reminderAdapter = new ReminderAdapter(listReminder, ReminderActivity.this);
        viewReminder.setAdapter(reminderAdapter);
    }

    private android.content.BroadcastReceiver mMessageReceiver = new android.content.BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            Log.e("HS:::", "Finish");
        }

    };

    //Setup reminder
    private void setupReminder() {
        List<Reminder> reminderList = ZlaterDatabase.getInstance(ReminderActivity.this).reminderDAO().getReminder();
        for (int i = 0; i < reminderList.size(); i++) {
            if (listReminder.get(i).getTurnOn() == 0) {
                Log.e("Alarm", "Cancel");
            } else {
                int hours = listReminder.get(i).getHour();
                int minutes = listReminder.get(i).getMinute();
                int monday = listReminder.get(i).getMonday();
                int tuesday = listReminder.get(i).getTuesday();
                int wednesday = listReminder.get(i).getWednesday();
                int thursday = listReminder.get(i).getThursday();
                int friday = listReminder.get(i).getFriday();
                int saturday = listReminder.get(i).getSaturday();
                int sunday = listReminder.get(i).getSunday();
                List<Integer> list = new ArrayList<>();
                if (monday == 1) {
                    monday = 2;
                    list.add(monday);
                }
                if (tuesday == 1) {
                    tuesday = 3;
                    list.add(tuesday);
                }
                if (wednesday == 1) {
                    wednesday = 4;
                    list.add(wednesday);

                }
                if (thursday == 1) {
                    thursday = 5;
                    list.add(thursday);
                }
                if (friday == 1) {
                    friday = 6;
                    list.add(friday);
                }
                if (saturday == 1) {
                    saturday = 7;
                    list.add(saturday);
                }
                if (sunday == 1) {
                    list.add(sunday);
                }
                for (int z = 0; z < list.size(); z++) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY, hours);
                    calendar.set(Calendar.MINUTE, minutes);
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.DAY_OF_WEEK, list.get(z));
                    Log.e("HS:::",list.get(z).toString());
                    Log.e("HS:::", hours + ":" + minutes + " : Day = "+list.get(z));
                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    Intent intent = new Intent(ReminderActivity.this, BootBroadcastReceiver.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(ReminderActivity.this, 1, intent, 0);
                    if (calendar.before(Calendar.getInstance())) {
//                        Toast.makeText(this, "Next day", Toast.LENGTH_SHORT).show();
//                        PendingIntent pIntent = PendingIntent.getBroadcast(getApplicationContext(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//                        AlarmManager alarmManager1 = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
//                        assert alarmManager1 != null;
//                        alarmManager1.cancel(pIntent);
                        Log.d("HS:::", "setupReminder: before time");
                        calendar.add(Calendar.DATE, 7);
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        assert alarmManager != null;
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                    }
                }

            }
        }


    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mMessageReceiver);
    }
}
