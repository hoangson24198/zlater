package com.example.zlater.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class Reminder extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Intent intentToSaveEnergy = new Intent();
            intentToSaveEnergy.setClassName("com.example.zlater", "package com.example.zlater.Activity.Reminder");
            intentToSaveEnergy.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intentToSaveEnergy);
            Intent intent1 = new Intent("ffffff");
            context.sendBroadcast(intent1);
        }

}
