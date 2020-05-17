package com.example.zlater.Service.local.step;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.Vibrator;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.zlater.Activity.MainActivity;
import com.example.zlater.Helper.DateTimeHelper;
import com.example.zlater.Model.step.StepModel;
import com.example.zlater.R;
import com.example.zlater.Service.ForegroundServices;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.realm.Realm;

public class StepService extends Service {

    private StepThread thread;
    private PowerManager.WakeLock mWakeLock;
    int id=99;
    NotificationCompat.Builder notification;
    PendingIntent pendingIntent;
    NotificationManager notificationManager;
    Vibrator vibrator;
    long numSteps;
    String steps;
    EventBus bus;


    @Override
    public void onCreate() {
        super.onCreate();
        thread = new StepThread(this);
        bus = EventBus.getDefault();
        bus.register(this);

        Realm realm = Realm.getDefaultInstance();
        StepModel result = realm.where(StepModel.class)
                .equalTo("date", DateTimeHelper.getToday())
                .findFirst();
        numSteps = result == null ? 0 : result.getNumSteps();
        bus.post(true);
        updateShowSteps();
        realm.close();
        myStartForeground();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("service", "service start()");
        ForegroundServices app = (ForegroundServices) getApplication();
        app.setServiceRun(true);

        if (intent.getBooleanExtra("isActivity", false))
            thread.setActivity(true);
        String s = intent.getStringExtra("restart");
        if (s != null) {
            Log.d("restart", s);
        }
        if (thread.getState() == Thread.State.NEW)
            thread.start();
        SharedPreferences sharedPreferences = getSharedPreferences("conf", MODE_PRIVATE);
        boolean foreground_model = sharedPreferences.getBoolean("foreground_model", false);

        if (foreground_model) {
            myStartForeground();
            mWakeLock(this);
        } else {
            stopForeground(true);
            if (mWakeLock != null) {
                if (mWakeLock.isHeld())
                    mWakeLock.release();
                mWakeLock = null;
            }
        }


        return START_STICKY;
    }

    public void myStartForeground() {

        Intent notificationIntent = new Intent(this, MainActivity.class);
        pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        notification = new NotificationCompat.Builder(this, ForegroundServices.CHANNEL_ID)
                .setContentTitle("Step count service ")
                .setContentText("Your step today " + steps )
                .setSmallIcon(R.drawable.logo)
                .setContentIntent(pendingIntent);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert notificationManager != null;
        notificationManager.notify(id,notification.build());
        startForeground(id, notification.build());
    }


    @Override
    public void onDestroy() {
        Log.d("service", "service stop()");

        stopForeground(true);
        thread.mystop();
        ForegroundServices app = (ForegroundServices) getApplication();
        app.setServiceRun(false);
        boolean temp = getSharedPreferences("conf", MODE_PRIVATE).getBoolean("switch_on", false);
        if (temp) {
            Log.d("restart", "auto restart");
            Intent intent = new Intent("service restart");
            sendBroadcast(intent);
        }
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    synchronized private PowerManager.WakeLock mWakeLock(Context context) {
        if (mWakeLock != null) {
            if (mWakeLock.isHeld())
                mWakeLock.release();
            mWakeLock = null;
        }

        if (mWakeLock == null) {
            PowerManager mgr = (PowerManager) context
                    .getSystemService(Context.POWER_SERVICE);
            mWakeLock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    StepService.class.getName());
            mWakeLock.setReferenceCounted(true);
            mWakeLock.acquire();
        }
        return (mWakeLock);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateSteps(Long num) {
        numSteps = num;
        updateShowSteps();
    }

    private void updateShowSteps() {
        float stepsCount = (float) numSteps;
        steps = "" + numSteps;
    }
}

