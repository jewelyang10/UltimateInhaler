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
public class getPollenCount {
    public getPollenCount() {
    }

    public static String pollenCount() {
        URL url;
        HttpURLConnection conn = null;
        String resmsg = "";
        // Making HTTP request
        try {
            url = new URL("https://socialpollencount.co.uk/api/forecast?location=[51.7546407,-1.2510746]");
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
