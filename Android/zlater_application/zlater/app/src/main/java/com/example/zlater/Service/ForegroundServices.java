package com.example.zlater.Service;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class ForegroundServices extends Application {
    public static final String CHANNEL_ID = "Zlater";
    private static final String DB_NAME = "zlater_db";

    private boolean serviceRun;

    private boolean isShowToast=false;


    public boolean isShowToast() {
        return isShowToast;
    }

    public void setShowToast(boolean showToast) {
        isShowToast = showToast;
    }

    public boolean getServiceRun() {
        return serviceRun;
    }

    public void setServiceRun(boolean serviceRun) {
        this.serviceRun = serviceRun;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        serviceRun=false;
        createNotificationChannel();
        initRealm();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Zlater",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    private void initRealm(){
        Realm.init(this);
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .name(DB_NAME)
                .build();
        Realm.setDefaultConfiguration(configuration);
    }
}
