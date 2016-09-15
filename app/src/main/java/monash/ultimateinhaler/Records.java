package monash.ultimateinhaler;

import java.io.Serializable;

/**
 * Created by jewel on 8/28/16.
 */
public class Records implements Serializable {
    private String date;
    private String tight_chest;
    private String wheezing;
    private String tiredness;
    private String inhaler;
    private String feeling_stressed;

    public Records(){

    }

    public Records(String date, String tight_chest, String wheezing, String tiredness, String inhaler, String feeling_stressed) {
        this.date = date;
        this.tight_chest = tight_chest;
        this.wheezing = wheezing;
        this.tiredness = tiredness;
        this.inhaler = inhaler;
        this.feeling_stressed = feeling_stressed;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTight_chest() {
        return tight_chest;
    }

    public void setTight_chest(String tight_chest) {
        this.tight_chest = tight_chest;
    }

    public String getWheezing() {
        return wheezing;
    }

    public void setWheezing(String wheezing) {
        this.wheezing = wheezing;
    }

    public String getTiredness() {
        return tiredness;
    }

    public void setTiredness(String tiredness) {
        this.tiredness = tiredness;
    }

    public String getInhaler() {
        return inhaler;
    }

    public void setInhaler(String inhaler) {
        this.inhaler = inhaler;
    }

    public String getFeeling_stressed() {
        return feeling_stressed;
    }

    public void setFeeling_stressed(String feeling_stressed) {
        this.feeling_stressed = feeling_stressed;
    }
}
