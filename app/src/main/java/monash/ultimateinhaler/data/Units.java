package monash.ultimateinhaler.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jewel on 8/17/16.
 */
public class Units implements JSONPopulator {
    private String temperature;

    public String getTemperature() {
        return temperature;
    }

    @Override
    public void populate(JSONArray data) throws JSONException {

    }

    @Override
    public void populate(JSONObject data) {
        temperature = data.optString("temperature");

    }
}