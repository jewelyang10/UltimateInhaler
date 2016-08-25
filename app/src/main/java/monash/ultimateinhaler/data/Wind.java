package monash.ultimateinhaler.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jewel on 8/24/16.
 */
public class Wind implements JSONPopulator {
    String speed;

    public String getSpeed() {
        return speed;
    }

    @Override
    public void populate(JSONArray data) throws JSONException {

    }

    @Override
    public void populate(JSONObject data) {
        speed = data.optString("speed");
    }
}
