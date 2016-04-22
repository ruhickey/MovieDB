package ie.thecoolkids.moviedb;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

public class NotificationService extends IntentService{
    private static final int NOTIFICATION_ID = 1;
    private static final String ACTION_START = "ACTION_START";
    private static final String ACTION_DELETE = "ACTION_DELETE";

    public NotificationService() {
        super(NotificationService.class.getSimpleName());
    }

    public static Intent startNotificationService(Context context){
        return (new Intent(context, NotificationService.class)).setAction(ACTION_START);
    }

    public static Intent deleteNotification(Context context){
        return (new Intent(context, NotificationService.class)).setAction(ACTION_DELETE);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try{
            if(ACTION_START.equals(intent.getAction())){
                processStartNotification();
            }
        }finally {
            WakefulBroadcastReceiver.completeWakefulIntent(intent);
        }
    }

    private void processStartNotification(){
        final Notification.Builder builder = new Notification.Builder(this);

        builder.setContentTitle("MovieDB").setAutoCancel(true)
                .setContentText("Come Visit MovieDB")
                .setSmallIcon(R.mipmap.ic_launcher);

        builder.setContentIntent(
                PendingIntent.getActivity(
                        this,
                        NOTIFICATION_ID,
                        new Intent(this, MainActivity.class),
                        PendingIntent.FLAG_UPDATE_CURRENT
                )
        );

        builder.setDeleteIntent(NotificationEventReceiver.getDeleteIntent(this));

        final NotificationManager manager = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NOTIFICATION_ID, builder.build());
    }
}
