package ru.bda.icrm.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import java.util.Calendar;
import java.util.List;

import ru.bda.icrm.R;
import ru.bda.icrm.database.DBController;
import ru.bda.icrm.model.Event;
import ru.bda.icrm.services.NotificationService;
import ru.bda.icrm.view.activities.LoginActivity;

public class TimeReceiver extends BroadcastReceiver {

    int count = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("log_timereceiver", "Start!!!!!!!");
        /**Intent serviceIntent = new Intent(context, NotificationService.class);
        context.startService(serviceIntent);*/
        checkAlarm(context);
    }

    private void checkAlarm(Context context) {
        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.set(Calendar.SECOND, 0);
        currentCalendar.set(Calendar.MILLISECOND, 0);

        DBController dbController = new DBController(context);
        List<Event> eventList = dbController.getEvent();
        if (eventList != null && eventList.size() > 0) {
            for (Event event : eventList) {
                if (event.getTimeBegin() == currentCalendar.getTime().getTime()) {
                    startNotification(event, context);
                }
            }
        }
        dbController.closeDb();
    }

    private void startNotification(Event event, Context context) {
        Log.d("alarm_log", "start_notif = " + event.getTimeBegin());
        Intent notifIntent = new Intent (context, LoginActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                0, notifIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        Resources res = context.getResources();

        Notification.Builder builder = new Notification.Builder(context);

        builder.setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.ic_today_white_24dp)
                .setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.ic_launcher))
                .setTicker("Событие")
                .setWhen(System.currentTimeMillis())
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true)
                .setContentTitle("Напоминание")
                .setContentText(event.getMessage());

        Notification notification = null;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            notification = builder.getNotification();
        } else {
            notification = builder.build();
        }

        NotificationManager notifManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        //int count = AppPref.getInstance().getNotifCount(this);
        //notifManager.notify(count, notification);
        notifManager.notify(0, notification);
        //AppPref.getInstance().setNotifCount(count + 1, this);
        count++;
    }
}
