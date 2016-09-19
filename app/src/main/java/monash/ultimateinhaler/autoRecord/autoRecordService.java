package monash.ultimateinhaler.autoRecord;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by magician-harry on 2016/9/19.
 */
public class autoRecordService extends IntentService {

    public static String serviceTag = "monash.ultimateinhaler.autoRecord";
    Context currentContext = null;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public autoRecordService(String name) {
        super(serviceTag);
    }

    public autoRecordService(Context context) {
        super("autoRecordMainService");
        currentContext = context;
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(serviceTag, "start running");

        String weatherInfo = getWeather.weatherDetail();
        String pollenInfo = getPollenCount.pollenCount();

        databaseWrite singleInstance = new databaseWrite(currentContext);
        singleInstance.insertData(weatherInfo, pollenInfo);

        Log.i(serviceTag, "finish");
    }

}
