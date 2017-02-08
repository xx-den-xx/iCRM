package ru.bda.icrm.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ru.bda.icrm.services.NotificationService;

/**
 * Created by User on 01.02.2017.
 */

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intentService = new Intent(context, NotificationService.class);
        context.startService(intentService);
    }
}
