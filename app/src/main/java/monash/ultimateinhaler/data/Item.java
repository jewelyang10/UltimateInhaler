package monash.ultimateinhaler.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jewel on 8/17/16.
 */
public class Item implements JSONPopulator {
    private Condition condition;
    private  Forecast forecast;

    public Forecast getForecast() {
        return forecast;
    }

    public Condition getCondition() {
        return condition;
    }

    @Override
    public void populate(JSONArray data) throws JSONException {

    }

    @Override
    public void populate(JSONObject data) {
        condition = new Condition();
        condition.populate(data.optJSONObject("condition"));
        forecast = new Forecast();
        try {
            forecast.populate(data.getJSONArray("forecast"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}