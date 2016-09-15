package monash.ultimateinhaler;

/**
 * Created by jewel on 9/15/16.
 */

import java.util.UUID;

/**
 * Entidad Lead
 */
public class Lead {
    private String mId;
    private String mName;
    private String mAnswer;

    private int mImage;

    public Lead(String name, String title, String company, int image) {
        mId = UUID.randomUUID().toString();
        mName = name;
        mAnswer = title;

        mImage = image;
    }

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getTitle() {
        return mAnswer;
    }

    public void setTitle(String mAnswer) {
        this.mAnswer = mAnswer;
    }



    public int getImage() {
        return mImage;
    }

    public void setImage(int mImage) {
        this.mImage = mImage;
    }

    @Override
    public String toString() {
        return "Lead{" +
                "ID='" + mId + '\'' +

                ", Question='" + mName + '\'' +
                ", Answer='" + mAnswer + '\'' +
                '}';
    }
}
