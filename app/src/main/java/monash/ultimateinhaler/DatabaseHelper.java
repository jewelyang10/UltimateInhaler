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
    public static final String DIARY_COL_2 = "tight_chest";
    public static final String DIARY_COL_3 = "wheezing";
    public static final String DIARY_COL_4 = "tiredness";
    public static final String DIARY_COL_5 = "inhaler_number";
    public static final String DIARY_COL_6 = "feeling_stressed";

    public static final String WEATHER_TABLE = "weather";
    public static final String WEATHER_COL_1 = "date";
    public static final String WEATHER_COL_2 = "temperature";
    public static final String WEATHER_COL_3 = "humidity";
    public static final String WEATHER_COL_4 = "pressure";
    public static final String WEATHER_COL_5 = "wind";
    public static final String WEATHER_COL_6 = "pollen";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getReadableDatabase();
    }

    //Create table
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + DIARY_TABLE + " ( " +
                DIARY_COL_1 + " TEXT PRIMARY KEY, "
                + DIARY_COL_2 + " TEXT, "
                + DIARY_COL_3 + " TEXT, "
                + DIARY_COL_4 + " TEXT, "
                + DIARY_COL_5 + " TEXT,"
                + DIARY_COL_6 + " TEXT)")
        ;

        db.execSQL("create table " + WEATHER_TABLE + " ( " +
                WEATHER_COL_1 + " TEXT PRIMARY KEY, "
                + WEATHER_COL_2 + " TEXT, "
                + WEATHER_COL_3 + " TEXT, "
                + WEATHER_COL_4 + " TEXT, "
                + WEATHER_COL_5 + " TEXT,"
                + WEATHER_COL_6 + " TEXT)")
        ;

    }

    //Upgrade database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DIARY_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + WEATHER_TABLE);
        onCreate(db);

    }

    //Insert diary into database
    public boolean insertDataToUser(String date, String tight_chest, String wheezing,
                                    String tiredness, String inhaler, String feeling_stressed) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DIARY_COL_1, date);
        contentValues.put(DIARY_COL_2, tight_chest);
        contentValues.put(DIARY_COL_3, wheezing);
        contentValues.put(DIARY_COL_4, tiredness);
        contentValues.put(DIARY_COL_5, inhaler);
        contentValues.put(DIARY_COL_6, feeling_stressed);
        long result = db.insert(DIARY_TABLE, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;

    }

    //Insert weather into weather table
    public boolean insertWeatherIntoDatabase(String date, String temperature, String humidity,
                                             String pressure, String wind, String pollen){
            SQLiteDatabase db = this.getReadableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(WEATHER_COL_1, date);
            contentValues.put(WEATHER_COL_2, temperature);
            contentValues.put(WEATHER_COL_3, humidity);
            contentValues.put(WEATHER_COL_4, pressure);
            contentValues.put(WEATHER_COL_5, wind);
            contentValues.put(WEATHER_COL_6, pollen);
            long result = db.insert(WEATHER_TABLE, null, contentValues);
            if (result == -1)
                return false;
            else
                return true;
    }

    //Ckeck if today's weather condition already exit in database
    public int todayWeatherExist(String date){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + WEATHER_TABLE + " where " + WEATHER_COL_1 + " = '" + date + "'", null);
        int count = res.getCount();
        db.close();
        res.close();
        return count;
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
    public boolean updateCurrentDayRecord(String date, String tight_chest, String wheezing,
                                          String tiredness, String inhaler, String feeling_streesed) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DIARY_COL_2, tight_chest);
        contentValues.put(DIARY_COL_3, wheezing);
        contentValues.put(DIARY_COL_4, tiredness);
        contentValues.put(DIARY_COL_5, inhaler);
        contentValues.put(DIARY_COL_6, feeling_streesed);

        String[] value = {date};
        String selection = "diary_date = ?";

        long result = db.update(DIARY_TABLE, contentValues, selection, value);
        if (result == -1)
            return false;
        else
            return true;

    }

    //Update the current record of a weather
    public boolean updateTodayWeatherRecord(String date, String temperature, String humidity,
                                            String pressure, String wind, String pollen) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(WEATHER_COL_2, temperature);
        contentValues.put(WEATHER_COL_3, humidity);
        contentValues.put(WEATHER_COL_4, pressure);
        contentValues.put(WEATHER_COL_5, wind);
        contentValues.put(WEATHER_COL_6, pollen);

        String[] value = {date};
        String selection = "date = ?";

        long result = db.update(WEATHER_TABLE, contentValues, selection, value);
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
                records.setTight_chest(res.getString(1));
                records.setWheezing(res.getString(2));
                records.setTiredness(res.getString(3));
                records.setInhaler(res.getString(4));
                records.setFeeling_stressed(res.getString(5));
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
                records.setTight_chest(res.getString(1));
                records.setWheezing(res.getString(2));
                records.setTiredness(res.getString(3));
                records.setInhaler(res.getString(4));
                records.setFeeling_stressed(res.getString(5));
            } while (res.moveToNext());

        }
        db.close();
        res.close();
        return records;
    }


    //Get the diary detail with a specified date
    public WeatherCondition getWeatherByDiaryDateTracked(String date){
        SQLiteDatabase db = this.getWritableDatabase();
        WeatherCondition weatherCondition = new WeatherCondition();
        Cursor res = db.rawQuery("select * from " + WEATHER_TABLE + " where " + WEATHER_COL_1 + " = '" + date + "'", null);
        res.moveToFirst();
        if (res.moveToFirst()) {
            do {
                weatherCondition.setDate(res.getString(0));
                weatherCondition.setTemperature(res.getString(1));
                weatherCondition.setHumidity(res.getString(2));
                weatherCondition.setPressure(res.getString(3));
                weatherCondition.setWind(res.getString(4));
                weatherCondition.setPollen(res.getString(5));

            } while (res.moveToNext());

        }
        db.close();
        res.close();
        return weatherCondition;
    }


    //Get all diary from database
    public ArrayList<Records> getSpecifiedMonthRecords(String month) {
        ArrayList<Records> list = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + DIARY_TABLE + " where " + DIARY_COL_1 + " LIKE '__-" + month + "-____'", null);
        res.moveToFirst();
        if (res.moveToFirst()) {
            do {
                Records records = new Records();
                records.setDate(res.getString(0));
                records.setTight_chest(res.getString(1));
                records.setWheezing(res.getString(2));
                records.setTiredness(res.getString(3));
                records.setInhaler(res.getString(4));
                records.setFeeling_stressed(res.getString(5));
                list.add(records);
            } while (res.moveToNext());

        }
        db.close();
        res.close();
        return list;
    }
}
