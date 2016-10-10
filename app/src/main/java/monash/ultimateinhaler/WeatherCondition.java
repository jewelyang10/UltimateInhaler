package monash.ultimateinhaler;

import java.io.Serializable;

/**
 * Created by jewel on 9/14/16.
 */
public class WeatherCondition implements Serializable {
    private String date;
    private String temperature;
    private String humidity;
    private String pressure;
    private String wind;
    private String pollen;
    private String tomorrow_pollen;


    public WeatherCondition() {
    }

    public WeatherCondition(String date, String temperature, String humidity, String pressure, String wind, String pollen, String tomorrow_pollen) {
        this.date = date;
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        this.wind = wind;
        this.pollen = pollen;
        this.tomorrow_pollen = tomorrow_pollen;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getPollen() {
        return pollen;
    }

    public void setPollen(String pollen) {
        this.pollen = pollen;
    }

    public String getTomorrow_pollen() {
        return tomorrow_pollen;
    }

    public void setTomorrow_pollen(String tomorrow_pollen) {
        this.tomorrow_pollen = tomorrow_pollen;
    }
}
