package monash.ultimateinhaler;

/**
 * Created by jewel on 9/12/16.
 */
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
/**
 * Created by jewel on 4/15/16.
 */
public class News implements Parcelable,Serializable{
    public static final String TABLE_NAME = "newses";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_LINK = "link";
    public static final String COLUMN_DESCP = "descp";
    public static final String COLUMN_IMAGE= "image";
    URL url = null;
    HttpURLConnection conn = null;

    // Table create statement
    public static final String CREATE_STATEMENT =
            "CREATE TABLE " + TABLE_NAME + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    COLUMN_TITLE + " TEXT NOT NULL, " +
                    COLUMN_LINK + " TEXT NOT NULL, " +
                    COLUMN_DESCP + " TEXT NOT NULL," +
                    COLUMN_IMAGE + " TEXT NOT NULL" +
                    ")";
    private long _id;
    private String title;
    private String link;
    private String description;


    public News(String title, String link, String description) {
        this.title = title;
        this.link = link;
        this.description = description;
        // this.bitmap = bitmap;
    }

    public News(long id,String title, String link, String description){
        this._id = id;
        this.title = title;
        this.link = link;
        this.description = description;
    }




    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


//    public AsyncTask<String, String, Bitmap> getBitmap() {
//        return bitmap;
//    }
//
//    public void setBitmap(AsyncTask<String, String, Bitmap> bitmap) {
//        this.bitmap = bitmap;
//    }

    //    public Bitmap getBitmap() {
//        URL url = null;
//        try {
//            url = new URL(imageUrl);
//            conn = (HttpURLConnection) url.openConnection();
//            InputStream instream = new BufferedInputStream(conn.getInputStream());
//            this.bitmap = BitmapFactory.decodeStream(instream);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return bitmap;
//    }



    public News(Parcel in) {
        // Note: You must read values in same order as they are written
        // See: writeToParcel method below.
        this._id = in.readLong();
        this.title = in.readString();
        this.link = in.readString();
        this.description = in.readString();
    }

    // Generates a Parcelable instance of this class from a Parcel
    public static final Parcelable.Creator<News> CREATOR = new Parcelable.Creator<News>() {
        @Override
        public News createFromParcel(Parcel in) {
            return new News(in);
        }

        @Override
        public News[] newArray(int size) {
            return new News[size];
        }
    };

    // Used to describe special objects, not modified very often.
    @Override
    public int describeContents() {
        return 0;
    }

    // Outputs the format the parcel writes values
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeLong(_id);
        parcel.writeString(title);
        parcel.writeString(link);
        parcel.writeString(description);
    }


//    private class LoadImage extends AsyncTask<String, String, Bitmap> {
//        @Override
//
//        protected Bitmap doInBackground(String... args) {
//            try {
//                bitmap = BitmapFactory.decodeStream((InputStream) new URL(args[0]).getContent());
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return bitmap;
//        }
//
//    }

}

