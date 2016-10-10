package monash.ultimateinhaler;


import android.app.ProgressDialog;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;

import monash.ultimateinhaler.service.YahooWeatherService;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {
    View rootView;
    TableLayout tableLayout;
    ArrayList<Records> history;
    private YahooWeatherService service;
    private ProgressDialog dialog;
    DatabaseHelper myDb;
    private boolean myAsyncTaskIsRunning = true;
    SQLiteDatabase sqLiteDatabase;
    TextView monthly_textView;
    private String todayDate, temperatureDb, humidityDb, pressureDb, windDb, pollenDb;
    String month;

    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_history, container, false);
        monthly_textView = (TextView) rootView.findViewById(R.id.Monthly_textView);

        //Configure the action bar
        setHasOptionsMenu(true);

        //Open database
        myDb = new DatabaseHelper(this.getContext());

        StartActivity startActivity = (StartActivity) getActivity();

        // Set title bar
        startActivity.setToolBar("TRACKING OVERVIEW", null);

        //Get the date from calendar
        Bundle args = getArguments();
        month = (String) args.getSerializable("month");
        Log.v("passedMonth", month);

        displayHistory();
        return rootView;
    }


    @SuppressWarnings("deprecation")
    public void displayHistory(){

        Date today = new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTime(today);
        cal.add(Calendar.DAY_OF_MONTH, -30);
        Date today30 = cal.getTime();

//        history = myDb.getSpecifiedMonthRecords(month);
        history = myDb.getRecords();
//        Log.v("month",month);
//        Log.v("history as month",Integer.toString(history.size()));

//        Integer mongthToInt = 0;
//        if(month.charAt(0) == '0') {
//            month = month.substring(1, 2);
//            mongthToInt = Integer.valueOf(month);
//        }else{
//            mongthToInt = Integer.valueOf(month);
//        }
//        String mongthName = new DateFormatSymbols().getMonths()[mongthToInt-1];
//        monthly_textView.setText(mongthName);
//        Typeface ty1 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/LS-Light.otf");
//        monthly_textView.setTypeface(ty1);
//        monthly_textView.setTextColor(Color.parseColor("#FFFFFF"));

//        Log.v("mongthName",mongthName);

        monthly_textView.setText("Last 30 days");
        Typeface ty1 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/LS-Light.otf");
        monthly_textView.setTypeface(ty1);
        monthly_textView.setTextColor(Color.parseColor("#FFFFFF"));

        Collections.sort(history, new Comparator<Records>() {
            @Override
            public int compare(Records lhs, Records rhs) {
                Calendar calendarlhs = Calendar.getInstance();
                Calendar calendarrhs = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy"); // HH:mm:ss
                try {
                    calendarlhs.setTime(dateFormat.parse(lhs.getDate()));

                    calendarrhs.setTime(dateFormat.parse(rhs.getDate()));

                } catch (Exception e) {
                    Log.v("History date", e.getMessage());
                }
                if (calendarlhs.getTimeInMillis() > calendarrhs.getTimeInMillis()) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });

        boolean before = false;
        for (int i = 0; i < history.size() ; i++) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy"); // HH:mm:ss
            try {
                Date date = dateFormat.parse(history.get(i).getDate());

                if(date.before(today30))
                    before = true;
                else
                    before = false;

                Log.v("before", "30days");
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (before) {
                Log.v("before", "true");

                continue;

            } else {
                tableLayout = (TableLayout) rootView.findViewById(R.id.history_table);

                TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);

                layoutParams.setMargins(10, 10, 10, 10);

                TableRow tableRow = new TableRow(getContext());

                //Pupulate the date for history
                TextView diaryDate = new TextView(getContext());


                diaryDate.setText(history.get(i).getDate());
                diaryDate.setGravity(Gravity.CENTER);
                diaryDate.setTextSize(19);
                diaryDate.setLayoutParams(layoutParams);
                diaryDate.setTextColor(Color.parseColor("#FFFFFF"));


                //Pupolate the stressed extent

                //Set the rating bar style
                ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(getContext(), R.style.foodRatingBar);
                RatingBar ratingBar = new RatingBar(contextThemeWrapper, null, 0);

//            SimpleRatingBar ratingBar = new SimpleRatingBar(getContext());

                ratingBar.setClickable(false);
                ratingBar.setFocusable(false);
                ratingBar.setIsIndicator(true);
                if (Integer.valueOf(history.get(i).getInhaler()) == 0) {
                    ratingBar.setRating(0);
                } else if (Integer.valueOf(history.get(i).getInhaler()) >= 1 &&
                        Integer.valueOf(history.get(i).getInhaler()) <= 5) {
                    ratingBar.setRating(1);
                } else if (Integer.valueOf(history.get(i).getInhaler()) >= 6 &&
                        Integer.valueOf(history.get(i).getInhaler()) <= 12) {
                    ratingBar.setRating(3);
                } else if (Integer.valueOf(history.get(i).getInhaler()) >= 13 &&
                        Integer.valueOf(history.get(i).getInhaler()) <= 20) {
                    ratingBar.setRating(4);
                } else {
                    ratingBar.setRating(0);
                }

                ratingBar.setLayoutParams(layoutParams);

                //Create a temperature textview
                TextView temperature = new TextView(getContext());
                temperature.setTextSize(19);
                temperature.setGravity(Gravity.CENTER);
                temperature.setLayoutParams(layoutParams);

                //Create a pollen textview

                TextView pollen = new TextView(getContext());
                pollen.setGravity(Gravity.CENTER);
                pollen.setTextSize(19);
                pollen.setLayoutParams(layoutParams);

                //Get the weather condition for that date
                WeatherCondition weatherCondition = myDb.getWeatherByDiaryDateTracked(history.get(i).getDate());
                if (weatherCondition.getDate() != null) {

                    //Populate the temperature
                    temperature.setText(weatherCondition.getTemperature());
//                Log.v("temperature",weatherCondition.getTemperature());
                    temperature.setTextColor(Color.parseColor("#FFFFFF"));

                    //Populate the pollen count
                    pollen.setText(weatherCondition.getPollen());
//                Log.v("pollen", weatherCondition.getPollen());
                    pollen.setTextColor(Color.parseColor("#FFFFFF"));

                } else {
                    //Populate the temperature
                    temperature.setText("N/A");
                    temperature.setTextColor(Color.parseColor("#FFFFFF"));


                    //Populate the pollen count
                    pollen.setText("N/A");
                    pollen.setTextColor(Color.parseColor("#FFFFFF"));
                }

                tableRow.addView(diaryDate, 0);
                tableRow.addView(ratingBar, 1);
                tableRow.addView(temperature, 2);
                tableRow.addView(pollen, 3);
                tableRow.setBackground(getResources().getDrawable(R.drawable.tablerow_border));

                tableLayout.addView(tableRow);
            }
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_back_diary, menu);  // Use filter.xml from step 1
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {   int id = item.getItemId();
        if (id == R.id.action_exit_diary) {

            //Go back to calendar fragment
            CalendarFragment fragment = new CalendarFragment();
            FragmentTransaction fragmentTransaction =
                    getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_containerStart, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        }
        return true;

    }


//    @Override
//    public void serviceSuccess(Channel channel) {
//        dialog.hide();
//
//        Item item = channel.getItem();
//
//        int resourceId = getResources().getIdentifier("drawable/icon_" + item.getCondition().getCode(), null, getContext().getPackageName());
//        int tomorrowId = getResources().getIdentifier("drawable/icon_" + item.getForecast().getCode(), null, getContext().getPackageName());
//
//
//        @SuppressWarnings("deprecation")
//        Drawable weatherIconDrawable = getResources().getDrawable(resourceId);
//        @SuppressWarnings("deprecation")
//        Drawable weatherIconDrawable_next = getResources().getDrawable(tomorrowId);
//
//        int f = item.getCondition().getTemperature();
//        double cn = (f - 32) *  5 / 9;
//        int ctn = (int) Math.floor(cn);
//
//
//        //Get the current system date
//
//        //Get today date
//        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
//        Date date = new Date();
//        todayDate = dateFormat.format(date);
//
//        //Get the weather condition and save into database
//        temperatureDb = Integer.toString(ctn);
//        humidityDb = channel.getAtmosphere().getHumidity();
//        pressureDb = channel.getAtmosphere().getPressure() + " in";
//        windDb = channel.getWind().getSpeed() + " mph";
//
////        insertWeatherIntoDatabase(todayDate, temperatureDb, humidityDb, pressureDb, windDb, pollenDb);
//
//
//    }
//
//    @Override
//    public void serviceFailure(Exception exception) {
//        dialog.hide();
//        Log.v("Weather exception", exception.getMessage());
//    }
//
//    public void insertWeatherIntoDatabase(String date, String temperature, String humidity,
//                                          String pressure, String wind, String pollen){
//        try{
//            if (myDb.todayWeatherExist(date) == 0){
//                myDb.insertWeatherIntoDatabase(date,temperature,humidity,pressure,wind,pollen);
//            }else
//            {
//                myDb.updateTodayWeatherRecord(date,temperature,humidity,pressure,wind,pollen);
//            }
//        }catch (Exception e){
////            Toast.makeText(getContext(),"Weather Database error",Toast.LENGTH_LONG).show();
//            Log.v("Weather databse error", e.getMessage());
//        }
//
//
//    }


        /*
    GetPollenCount
    Get the pollen count

     */

}
