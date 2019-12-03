package com.example.zlater.Service.local;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import android.os.Build;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.zlater.Activity.MainActivity;
import com.example.zlater.Model.Routine;
import com.example.zlater.Model.StepCount;
import com.example.zlater.R;
import com.example.zlater.Utils.Constants;
import com.example.zlater.Service.ForegroundServices;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StepCountServices extends Service implements SensorEventListener {
    boolean isRunning = false;
    int step = 0;
    int id=99;
    double MagnitudePrevious = 0;
    SensorManager sensorManager;
    NotificationCompat.Builder notification;
    PendingIntent pendingIntent;
    NotificationManager notificationManager;
    Vibrator vibrator;
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        notification = new NotificationCompat.Builder(this, ForegroundServices.CHANNEL_ID)
                .setContentTitle("Step count service ")
                .setContentText("Your step today " + step)
                .setSmallIcon(R.drawable.logo)
                .setContentIntent(pendingIntent);

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert notificationManager != null;
        notificationManager.notify(id,notification.build());
        isRunning = true;
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (countSensor != null) {
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            Log.e("HS:::", "not found sensor!!!");
        }
        startForeground(id, notification.build());
        return START_STICKY;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (isRunning) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];
            double Magnitude = Math.sqrt(x * x + y * y + z * z);
            double MagnitudeDenta = Magnitude - MagnitudePrevious;
            if (MagnitudeDenta > 20) {
                step++;
            }
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            SimpleDateFormat dateFormatSave = new SimpleDateFormat("yyyy-MM-dd");
//            Log.e("currentTime", dateFormat.format(date));
            String timeForUploadData = dateFormat.format(date);
            //Save routine
            if (timeForUploadData.equals("23:59:00")) {
                Log.e("currentTime","It time");
                SharedPreferences sharedPreferences=getSharedPreferences(Constants.LOGIN,MODE_PRIVATE);
                Routine routine=new Routine(step,dateFormatSave.format(date)+" 00:00:00",null,"2","5",sharedPreferences.getInt("id",0));
                ZlaterDatabase.getInstance(getApplicationContext()).routineDAO().insert(routine);
            }

            if(timeForUploadData.equals("05:00:00")){
                updateStepCount(5,step);
                Log.e("currentTime","It time");
            }
            if (timeForUploadData.equals("07:00:00")){
                updateStepCount(7,step);
                Log.e("currentTime","It time");
            }
            if (timeForUploadData.equals("09:00:00")){
                updateStepCount(9,step);
                Log.e("currentTime","It time");
            }
            if (timeForUploadData.equals("11:00:00")){
                updateStepCount(11,step);
                Log.e("currentTime","It time");
            }
            if (timeForUploadData.equals("13:00:00")){
                updateStepCount(13,step);
                Log.e("currentTime","It time");
            }
            if (timeForUploadData.equals("15:00:00")){
                updateStepCount(15,step);
                Log.e("currentTime","It time");
            }
            if (timeForUploadData.equals("17:00:00")){
                updateStepCount(17,step);
                Log.e("currentTime","It time");
            }
            if (timeForUploadData.equals("19:00:00")){
                updateStepCount(19,step);
                Log.e("currentTime","It time");
            }
            if (timeForUploadData.equals("21:00:00")){
                updateStepCount(21,step);
                Log.e("currentTime","It time");
            }
                notification.setContentText("Your step today " + step);
                notification.setSmallIcon(R.drawable.logo);
                notificationManager.notify(id,notification.build());

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
    private void disableVibrate(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(ForegroundServices.CHANNEL_ID,"Zlater", NotificationManager.IMPORTANCE_HIGH);
            //Disabling vibration!
            notificationChannel.enableVibration(false);
            notificationManager.createNotificationChannel(notificationChannel);
            notification = new NotificationCompat.Builder(this, ForegroundServices.CHANNEL_ID);

        } else {
            notification = new NotificationCompat.Builder(this);
            notification.setVibrate(new long[]{0L});
        }
    }
    private void updateStepCount(int hour,int step){
        StepCount stepCount=new StepCount(hour,step);
        ZlaterDatabase.getInstance(getApplicationContext()).stepDAO().insert(stepCount);
    }



}
