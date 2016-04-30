package ie.thecoolkids.moviedb;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import java.util.Calendar;
import java.util.Date;

/**
 * WakefulBroadcastReceiver used to receive intents fired
 * from the AlarmManager for showing notifications
 * and from the notification itself if it is deleted.
 * Passes the work onto a service.
 */
public class NotificationEventReceiver extends WakefulBroadcastReceiver{
    private static final String START_NOTIFICATION_SERVICE = "START_NOTIFICATION_SERVICE";
    private static final String DELETE_NOTIFICATION = "DELETE_NOTIFICATION";
    private static final long INTERVAL = 1000 * 60 * 60 * 2;

    public static void setupAlarm(Context context){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                getTriggerAt(new Date()),
                INTERVAL,
                getPendingIntent(context)
        );
    }

    public static void cancelAlarm(Context context){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(getPendingIntent(context));
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Intent serviceIntent = null;
        if(action.equals(START_NOTIFICATION_SERVICE)){
            serviceIntent = NotificationService.startNotificationService(context);
        }
        else if(action.equals(DELETE_NOTIFICATION)){
            serviceIntent = NotificationService.deleteNotification(context);
        }
        if (serviceIntent != null) {
            // keep the device awake while it is launching.
            startWakefulService(context, serviceIntent);
        }
    }

    private static long getTriggerAt(Date now) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.HOUR, 2);
        return calendar.getTimeInMillis();
    }

    private static PendingIntent getPendingIntent(Context context){
        return PendingIntent.getBroadcast(
                context,
                0,
                new Intent(context, NotificationEventReceiver.class).setAction(START_NOTIFICATION_SERVICE),
                PendingIntent.FLAG_UPDATE_CURRENT
        );
    }

    public static PendingIntent getDeleteIntent(Context context) {
        return PendingIntent.getBroadcast(
                context,
                0,
                new Intent(context, NotificationEventReceiver.class).setAction(DELETE_NOTIFICATION),
                PendingIntent.FLAG_UPDATE_CURRENT
        );
    }
}