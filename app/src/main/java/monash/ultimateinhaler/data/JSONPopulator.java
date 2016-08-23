package monash.ultimateinhaler.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jewel on 8/17/16.
 */
public interface JSONPopulator {
    void populate(JSONArray data) throws JSONException;

    void populate(JSONObject data);

}
