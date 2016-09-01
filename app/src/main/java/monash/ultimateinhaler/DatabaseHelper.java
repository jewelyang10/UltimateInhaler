package monash.ultimateinhaler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by jewel on 8/28/16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "diary.db";
    public static final String DIARY_TABLE = "diary";
    //public static final String DIARY_COL_1 = "diary_id";
    public static final String DIARY_COL_1 = "diary_date";
    public static final String DIARY_COL_2 = "new_with_asthma";
    public static final String DIARY_COL_3 = "attcked_today";
    public static final String DIARY_COL_4 = "attack_times";
    public static final String DIARY_COL_5 = "other";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getReadableDatabase();
    }

    //Create table
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + DIARY_TABLE + " ( " +
//                DIARY_COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                DIARY_COL_1 + " TEXT PRIMARY KEY, "
                + DIARY_COL_2 + " INTEGER, "
                + DIARY_COL_3 + " INTEGER, "
                + DIARY_COL_4 + " INTEGER, "
                + DIARY_COL_5 + " TEXT)")
        ;

    }

    //Upgrade database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DIARY_TABLE);
        onCreate(db);

    }

    //Insert diary into database
    public boolean insertDataToUser(String date, int new_with_asthma, int attacked_today,
                                    int attack_times, String others) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DIARY_COL_1, date);
        contentValues.put(DIARY_COL_2, new_with_asthma);
        contentValues.put(DIARY_COL_3, attacked_today);
        contentValues.put(DIARY_COL_4, attack_times);
        contentValues.put(DIARY_COL_5, others);
        long result = db.insert(DIARY_TABLE, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;

    }

    //Check if the diary exit for a specified date
    public int currentDayDiaryExist(String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + DIARY_TABLE + " where " + DIARY_COL_1 + " = '" + date + "'", null);
        int count = res.getCount();
        db.close();
        res.close();
        return count;

    }

    //Update the current record of a specified date
    public boolean updateCurrentDayRecord(String date, int new_with_asthma, int attacked_today,
                                          int attack_times, String others) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DIARY_COL_2, new_with_asthma);
        contentValues.put(DIARY_COL_3, attacked_today);
        contentValues.put(DIARY_COL_4, attack_times);
        contentValues.put(DIARY_COL_5, others);
        String[] value = {date};
        String selection = "diary_date = ?";

        long result = db.update(DIARY_TABLE, contentValues, selection, value);
        if (result == -1)
            return false;
        else
            return true;

    }

    //Get all diary from database
    public ArrayList<Records> getRecords() {
        ArrayList<Records> list = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + DIARY_TABLE, null);
        res.moveToFirst();
        if (res.moveToFirst()) {
            do {
                Records records = new Records();
                records.setDate(res.getString(0));
                records.setNewWithAsthma(res.getString(1));
                records.setAttcked_today(res.getString(2));
                records.setAttack_times(res.getString(3));
                records.setOther(res.getString(4));
                list.add(records);
                } while (res.moveToNext());

        }
        db.close();
        res.close();
        return list;
    }

    //Get the diary detail with a specified date
    public Records getCurrentDayDiary(String date){
        SQLiteDatabase db = this.getWritableDatabase();
        Records records = new Records();
        Cursor res = db.rawQuery("select * from " + DIARY_TABLE + " where " + DIARY_COL_1 + " = '" + date + "'", null);
        res.moveToFirst();
        if (res.moveToFirst()) {
            do {
                records.setDate(res.getString(0));
                records.setNewWithAsthma(res.getString(1));
                records.setAttcked_today(res.getString(2));
                records.setAttack_times(res.getString(3));
                records.setOther(res.getString(4));
            } while (res.moveToNext());

        }
        db.close();
        res.close();
        return records;
    }
}
