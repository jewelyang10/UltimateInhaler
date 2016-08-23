package monash.ultimateinhaler;

/**
 * Created by jewel on 8/23/16.
 */
public class Biodiversity {
    private String latitude;
    private String longtitude;
    private String name;

    public void Biodiversity(String latitude, String longtitude, String name){
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.name = name;
    }

    public void Biodiversity(){

    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(String longtitude) {
        this.longtitude = longtitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
