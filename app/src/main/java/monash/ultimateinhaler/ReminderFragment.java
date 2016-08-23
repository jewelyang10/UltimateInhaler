package monash.ultimateinhaler;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.GregorianCalendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReminderFragment extends Fragment {
    Button showNotificationBtn, closeNotificationBtn, alertBtn;
    NotificationManager notificationManager;
    boolean isNotificActive = false;
    int notifID = 33;
    View rootView;

    public ReminderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_reminder, container, false);

        showNotificationBtn = (Button)rootView.findViewById(R.id.button_showNotice);
        closeNotificationBtn = (Button)rootView.findViewById(R.id.button_closeNotice);
        alertBtn = (Button)rootView.findViewById(R.id.button_alert);

        showNotificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationCompat.Builder notificBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(getContext())
                        .setContentTitle("Message")
                        .setContentText("New Message")
                        .setTicker("Alert New Message")
                        .setSmallIcon(R.drawable.header_drawer);

                Intent moreInfoIntent = new Intent(getContext(),MoreInfoNotification.class);
                TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(getContext());
                taskStackBuilder.addParentStack(MoreInfoNotification.class);
                taskStackBuilder.addNextIntent(moreInfoIntent);
                PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0,PendingIntent.FLAG_CANCEL_CURRENT);
                notificBuilder.setContentIntent(pendingIntent);
                notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(notifID, notificBuilder.build());
                isNotificActive = true;
            }
        });

        closeNotificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNotificActive){
                    notificationManager.cancel(notifID);
                }
            }
        });

        alertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Long alertTime = new GregorianCalendar().getTimeInMillis()+5*1000;
                Intent alertIntent =  new Intent(getContext(),AlertReceiver.class);
                AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, alertTime,
                        PendingIntent.getBroadcast(getContext(),1,alertIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT));
            }
        });
        return rootView;
    }

}
