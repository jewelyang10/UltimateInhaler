package monash.ultimateinhaler.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jewel on 8/17/16.
 */
public class Channel implements JSONPopulator {
    private Units units;
    private Item item;
    private Wind wind;
    private Astronomy astronomy;
    private Atmosphere atmosphere;

    public Units getUnits() {
        return units;
    }

    public Item getItem() {
        return item;
    }

    public Wind getWind() {
        return wind;
    }

    public Astronomy getAstronomy() {
        return astronomy;
    }

    public Atmosphere getAtmosphere() {
        return atmosphere;
    }

    @Override
    public void populate(JSONArray data) throws JSONException {

    }

    @Override
    public void populate(JSONObject data) {

        units = new Units();
        units.populate(data.optJSONObject("units"));

        wind = new Wind();
        wind.populate(data.optJSONObject("wind"));

        astronomy = new Astronomy();
        astronomy.populate(data.optJSONObject("astronomy"));

        item = new Item();
        item.populate(data.optJSONObject("item"));

        atmosphere = new Atmosphere();
        atmosphere.populate(data.optJSONObject("atmosphere"));
    }

}
