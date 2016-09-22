package monash.ultimateinhaler;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.kyleduo.switchbutton.SwitchButton;
import com.rey.material.widget.CompoundButton;

import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReminderFragment extends Fragment {
    Button showNotificationBtn, closeNotificationBtn, alertBtn;
    NotificationManager notificationManager;
    boolean isNotificActive = false;
    int notifID = 33;
    View rootView;
    private SwitchButton mListenerSb;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    DatabaseHelper myDb;
    SQLiteDatabase sqLiteDatabase;

    public ReminderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_reminder, container, false);
        //Configure the action bar
        setHasOptionsMenu(true);
        myDb = new DatabaseHelper(this.getContext());
//        sqLiteDatabase = myDb.getWritableDatabase();
//        myDb.onUpgrade(sqLiteDatabase,2,3);
        //Set the title of this fragment
        StartActivity startActivity = (StartActivity) getActivity();

        // Set title bar
        startActivity.setToolBar("NOTIFICATION", null);


        mListenerSb = (SwitchButton) rootView.findViewById(R.id.sb_use_listener);

        //Get the status from database and set the switch button
        getTheSwitchButtonStatusFromDb();



        // work with listener
        mListenerSb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(android.widget.CompoundButton buttonView, boolean isChecked) {
                String status = null;
                if(mListenerSb.isChecked())
                    status = "1";
                else
                    status = "0";

                if(myDb.checkNotifyTableEmpty() == 0){

                    if(myDb.insertStatus(status)) {
                        String c = "1";
                        Log.v("insertSuccessful", c);
                    }else{
                        Log.v("insertError", "0");

                    }

                }else{
                    myDb.updateNotifyStatus(status);
                }

                if(mListenerSb.isChecked()) {
                    // Set the alarm to start at 8:00 a.m.

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    calendar.set(Calendar.HOUR_OF_DAY, 8);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.SECOND, 0);
                    Intent intent = new Intent(getContext(), Notification_receiver.class);
                    pendingIntent = PendingIntent.getBroadcast(getContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                    // setRepeating() lets you specify a precise custom interval--in this case, a day
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
                    isNotificActive = true;
                }else{
                    if(isNotificActive){

                        Intent intent = new Intent(getContext(), Notification_receiver.class);
                        pendingIntent = PendingIntent.getBroadcast(getContext(),100,intent,PendingIntent.FLAG_CANCEL_CURRENT);

                    }
                    if (alarmManager!= null) {
                        alarmManager.cancel(pendingIntent);
                    }
                }


            }

        });


//        showNotificationBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                NotificationCompat.Builder notificBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(getContext())
//                        .setContentTitle("Message")
//                        .setContentText("New Message")
//                        .setTicker("Alert New Message")
//                        .setSmallIcon(R.drawable.purple_header);
//
//                Intent moreInfoIntent = new Intent(getContext(),MoreInfoNotification.class);
//                TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(getContext());
//                taskStackBuilder.addParentStack(MoreInfoNotification.class);
//                taskStackBuilder.addNextIntent(moreInfoIntent);
//                PendingIntent pendingIntent2 = taskStackBuilder.getPendingIntent(0,PendingIntent.FLAG_CANCEL_CURRENT);
//                notificBuilder.setContentIntent(pendingIntent2);
//                notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
//                notificationManager.notify(notifID, notificBuilder.build());
//                isNotificActive = true;
//            }
//        });

        return rootView;
    }

    public void getTheSwitchButtonStatusFromDb(){
        int exist = myDb.checkNotifyTableEmpty();
        if (exist == 0){
            mListenerSb.setChecked(false);
        }else{
            if (myDb.getStatus().equals("1")){
                mListenerSb.setChecked(true);
                Log.v("SwitchOpened", myDb.getStatus());
            }
            else {
                mListenerSb.setChecked(false);
                Log.v("SwitchClosed", myDb.getStatus());

            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_back_diary, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_exit_diary) {
            //
//

            MoreFragment fragment6 = new MoreFragment();
            FragmentTransaction fragmentTransaction6 =
                    getFragmentManager().beginTransaction();
            fragmentTransaction6.replace(R.id.fragment_containerStart, fragment6);
            fragmentTransaction6.addToBackStack(null);
            fragmentTransaction6.commit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
