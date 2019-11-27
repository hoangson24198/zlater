package com.example.zlater.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.agik.AGIKSwipeButton.Controller.OnSwipeCompleteListener;
import com.agik.AGIKSwipeButton.View.Swipe_Button_View;

import com.example.zlater.R;

import java.util.Objects;

public class ReminderScreenActivity extends AppCompatActivity {
    LinearLayout viewReminder;
    Swipe_Button_View swipe_button_view;
    LinearLayout layoutClock;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();

        Window wind;
        wind = this.getWindow();
        wind.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        wind.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        wind.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        setContentView(R.layout.activity_reminder_screen);
        viewReminder = findViewById(R.id.viewReminder);
        swipe_button_view = findViewById(R.id.stopReminder);
        swipe_button_view.setEnabled(true);
        layoutClock = findViewById(R.id.layoutClock);
        setAnimations(layoutClock);
        mediaPlayer = MediaPlayer.create(ReminderScreenActivity.this, R.raw.cock_song);
        mediaPlayer.start();
        mediaPlayer.setLooping(true);
/*//        mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.cock_song);
////        mediaPlayer.start();
////        mediaPlayer.setLooping(true);
//        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        thePlayer = MediaPlayer.create(getApplicationContext(),R.raw.cock_song);

//        try {
//            thePlayer.setVolume((float) (audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION) / 7.0),
//                    (float) (audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION) / 7.0));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        thePlayer.start();
        thePlayer.setLooping(true);*/
        swipe_button_view.setOnSwipeCompleteListener_forward_reverse(new OnSwipeCompleteListener() {
            @Override
            public void onSwipe_Forward(Swipe_Button_View swipe_button_view) {
                cancelAlarm();
            }

            @Override
            public void onSwipe_Reverse(Swipe_Button_View swipe_button_view) {
                Toast.makeText(ReminderScreenActivity.this, "Stop", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(ReminderScreenActivity.this, ReminderActivity.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(ReminderScreenActivity.this, 1, intent, 0);
        assert alarmManager != null;
        alarmManager.cancel(pendingIntent);
//        mediaPlayer.stop();
            mediaPlayer.release();
        finish();

    }

    private void setAnimations(LinearLayout layoutClock) {
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.zoom_and_fade);
        layoutClock.setAnimation(animation);
    }

    @Override
    protected void onStop() {
        super.onStop();


    }
}
