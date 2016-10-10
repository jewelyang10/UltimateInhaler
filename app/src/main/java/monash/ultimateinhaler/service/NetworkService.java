package monash.ultimateinhaler.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import monash.ultimateinhaler.MyApplication;

/**
 * Created by jewel on 10/3/16.
 */
public class NetworkService
            extends BroadcastReceiver {

        public static ConnectivityReceiverListener connectivityReceiverListener;

        public NetworkService() {
            super();
        }

        @Override
        public void onReceive(Context context, Intent arg1) {
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null
                    && activeNetwork.isConnectedOrConnecting();

            if (connectivityReceiverListener != null) {
                connectivityReceiverListener.onNetworkConnectionChanged(isConnected);
            }
        }

        public static boolean isConnected() {
            ConnectivityManager
                    cm = (ConnectivityManager) MyApplication.getInstance().getApplicationContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null
                    && activeNetwork.isConnectedOrConnecting();
        }


        public interface ConnectivityReceiverListener {
            void onNetworkConnectionChanged(boolean isConnected);
        }

    }