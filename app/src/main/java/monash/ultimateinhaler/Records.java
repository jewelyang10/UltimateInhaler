package monash.ultimateinhaler;

/**
 * Created by jewel on 8/28/16.
 */
public class Records {
    private String date;
    private String newWithAsthma;
    private String attcked_today;
    private String attack_times;
    private String other;

    public Records(){

    }

    public Records(String date, String newWithAsthma, String attcked_today, String attack_times, String other) {
        this.date = date;
        this.newWithAsthma = newWithAsthma;
        this.attcked_today = attcked_today;
        this.attack_times = attack_times;
        this.other = other;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNewWithAsthma() {
        return newWithAsthma;
    }

    public void setNewWithAsthma(String newWithAsthma) {
        this.newWithAsthma = newWithAsthma;
    }

    public String getAttcked_today() {
        return attcked_today;
    }

    public void setAttcked_today(String attcked_today) {
        this.attcked_today = attcked_today;
    }

    public String getAttack_times() {
        return attack_times;
    }

    public void setAttack_times(String attack_times) {
        this.attack_times = attack_times;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }
}
