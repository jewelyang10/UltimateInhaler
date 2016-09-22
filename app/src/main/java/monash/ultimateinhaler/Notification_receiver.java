package monash.ultimateinhaler;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

/**
 * Created by jewel on 9/21/16.
 */
public class Notification_receiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        boolean alarmRunning = (PendingIntent.getBroadcast(context, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT) != null);
        if(alarmRunning == true) {
            Intent repeating_intent = new Intent(context, StartActivity.class);
            repeating_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 100, repeating_intent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                    .setContentIntent(pendingIntent)
                    .setContentTitle("Asthma Predict Today")
                    .setContentText("Track your asthma today and Get today prediction!")
                    .setSmallIcon(R.drawable.purple_header)
                    .setAutoCancel(true);
            notificationManager.notify(100, builder.build());
            Log.v("Opened", "opened");

        }
        else{
            notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(100);
            Log.v("Closed","closed");
        }

    }

}
