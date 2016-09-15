package monash.ultimateinhaler;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jewel on 8/19/16.
 */
public class DatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseAccess instance;

    /**
     * Private constructor to aboid object creation from outside classes.
     *
     * @param context
     */
    private DatabaseAccess(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    /**
     * Return a singleton instance of DatabaseAccess.
     *
     * @param context the Context
     * @return the instance of DabaseAccess
     */
    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    /**
     * Open the database connection.
     */
    public void open() {

        this.database = openHelper.getWritableDatabase();
    }

    /**
     * Close the database connection.
     */
    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    /**
     * Read all quotes from the database.
     *
     * @return a List of quotes
     */
    public List<String> getQuotes(String latitude, String longitude) {
        List<String> list = new ArrayList<>();
//        Cursor cursor = database.rawQuery("SELECT DISTINCT (Common.Name), Latitude, Longitude FROM biodiversity where latitude like '" + latitude + "%' " +
//                "and longitude like '" + longitude + "%' GROUP BY Common.Name, Latitude, Longitude ORDER BY Common.Name DESC LIMIT 10", null);
        Cursor cursor = database.rawQuery("SELECT * FROM biodiversity where latitude BETWEEN "
                + (Double.valueOf(latitude) - 1) + " AND " + (Double.valueOf(latitude) + 1) +
                " and longitude BETWEEN " + (Double.valueOf(longitude) -1) + " AND " + (Double.valueOf(longitude) +1) + " LIMIT 10", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
//            list.add(cursor.getString(1) + "," + cursor.getString(3) + ","+
//                            cursor.getString(4) + "," + cursor.getString(5)
//                                    +"," + cursor.getString(6) + "," + cursor.getString(7)
//                                    +"," + cursor.getString(8) +"," + cursor.getString(15)
//                            + "," + cursor.getString(16)
            list.add(cursor.getString(0) + "," + cursor.getString(1) + ","+
                            cursor.getString(4) + "," + cursor.getString(5)
            );
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }
}
