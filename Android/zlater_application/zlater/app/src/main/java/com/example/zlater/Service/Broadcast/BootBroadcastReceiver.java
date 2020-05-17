package com.example.zlater.Service.Broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.util.Log;

import com.example.zlater.Activity.ReminderScreenActivity;
import com.example.zlater.Service.local.step.StepService;
//import com.example.zlater.Service.local.step.StepCountServices;

public class BootBroadcastReceiver extends android.content.BroadcastReceiver {
    MediaPlayer mediaPlayer;
        @Override
        public void onReceive(Context context, Intent intent) {
//            mediaPlayer = MediaPlayer.create(context, R.raw.cock_song);
//            mediaPlayer.start();
//            mediaPlayer.setLooping(true);
            Intent intentToScreenReminder = new Intent(context, ReminderScreenActivity.class);
            intentToScreenReminder.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intentToScreenReminder);
            Intent intent1 = new Intent("registerReminder");
            Log.d("HS:::", "reminder: on ");
            context.sendBroadcast(intent1);

            SharedPreferences sharedPreferences = context.getSharedPreferences("conf", context.MODE_PRIVATE);
            boolean temp=sharedPreferences.getBoolean("switch_on", false);
            if (!temp)
                return;
            Intent serviceIntent = new Intent(context, StepService.class);
            serviceIntent.putExtra("restart",intent.getAction());
            context.startService(serviceIntent);
        }

}
