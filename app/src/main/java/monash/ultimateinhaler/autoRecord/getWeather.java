package monash.ultimateinhaler.autoRecord;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by magician-harry on 2016/9/19.
 */
public class getWeather {
    public getWeather() {
    }

    public static String weatherDetail() {

        URL url;
        HttpURLConnection conn = null;
        String resmsg = "";
        // Making HTTP request
        try {
            url = new URL("https://query.yahooapis.com/v1/public/yql?q=select+*+from+weather.forecast+where+woeid+in+%28select+woeid+from+geo.places%281%29+where+text%3D%22Melbourne%2C+Australia%22%29&format=json");
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            //make some HTTP headers
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            //Read response
            InputStream instream = new BufferedInputStream(conn.getInputStream());
            BufferedReader buffer = new BufferedReader(new InputStreamReader(instream));
            String s;
            while ((s = buffer.readLine()) != null) {
                resmsg += s;
            }
        } catch (Exception e) {
            Log.e("JSON", e.getMessage());

        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return resmsg;
    }

}
