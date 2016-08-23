package monash.ultimateinhaler.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jewel on 8/19/16.
 */
public class Forecast implements JSONPopulator{
    private int code;
    private String date;
    private String day;
    private String high;
    private String low;
    private String text;

    public int getCode() {
        return code;
    }

    public String getDate() {
        return date;
    }

    public String getDay() {
        return day;
    }

    public String getHigh() {
        return high;
    }

    public String getLow() {
        return low;
    }

    public String getText() {
        return text;
    }

    @Override
    public void populate(JSONArray data) throws JSONException {
        code = data.getJSONObject(1).optInt("code");
        date = data.getJSONObject(1).optString("date");
        day = data.getJSONObject(1).optString("day");
        high = data.getJSONObject(1).optString("high");
        low = data.getJSONObject(1).optString("low");
        text = data.getJSONObject(1).optString("text");

    }

    @Override
    public void populate(JSONObject data) {

    }

}
