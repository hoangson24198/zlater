package com.example.zlater.Service.Reminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;

public class ReminderServices extends BroadcastReceiver {
    MediaPlayer mediaPlayer;
        @Override
        public void onReceive(Context context, Intent intent) {
//            mediaPlayer = MediaPlayer.create(context, R.raw.cock_song);
//            mediaPlayer.start();
//            mediaPlayer.setLooping(true);
            Intent intentToScreenReminder = new Intent("reminderServices");
            intentToScreenReminder.setClassName("com.example.zlater", "ReminderScreenActivity");
            intentToScreenReminder.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intentToScreenReminder);
            Intent intent1 = new Intent("registerReminder");
            context.sendBroadcast(intent1);
        }

}
