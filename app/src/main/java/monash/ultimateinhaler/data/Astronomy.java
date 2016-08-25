package monash.ultimateinhaler.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jewel on 8/24/16.
 */
public class Astronomy implements JSONPopulator {
    private String sunrise;
    private String sunset;

    public String getSunrise() {
        return sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    @Override
    public void populate(JSONArray data) throws JSONException {

    }

    @Override
    public void populate(JSONObject data) {
        sunrise = data.optString("sunrise");
        sunset = data.optString("sunset");
    }
}
