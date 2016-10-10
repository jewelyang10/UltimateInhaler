package monash.ultimateinhaler;

import android.app.Application;

import monash.ultimateinhaler.service.NetworkService;

/**
 * Created by jewel on 10/3/16.
 */
public class MyApplication extends Application {

    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(NetworkService.ConnectivityReceiverListener listener) {
        NetworkService.connectivityReceiverListener = listener;
    }
}
