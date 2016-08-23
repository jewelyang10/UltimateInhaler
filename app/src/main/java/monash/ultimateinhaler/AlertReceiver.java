package monash.ultimateinhaler;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

/**
 * Created by jewel on 8/23/16.
 */
public class AlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        createNotification(context,"Times Up", "5 seconds has passed","Alert");
    }

    public void createNotification(Context context, String msg, String msgText, String msgAlert){
        PendingIntent notificIntent =  PendingIntent.getActivity(context, 0 ,new Intent(context, MainActivity.class),0);
        NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.header_drawer)
                .setContentTitle(msg)
                .setTicker(msgAlert)
                .setContentText(msgText);
        mBuilder.setContentIntent(notificIntent);
        mBuilder.setDefaults(NotificationCompat.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1,mBuilder.build());


    }

}

