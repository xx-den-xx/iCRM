package ru.bda.icrm.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import ru.bda.icrm.services.NotificationService;

public class TimeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("log_timereceiver", "Start!!!!!!!");
        Intent serviceIntent = new Intent(context, NotificationService.class);
        context.startService(serviceIntent);
    }
}
