package monash.ultimateinhaler;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.joshdholtz.sentry.Sentry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import monash.ultimateinhaler.data.Channel;
import monash.ultimateinhaler.data.Item;
import monash.ultimateinhaler.service.WeatherServiceCallback;
import monash.ultimateinhaler.service.YahooWeatherService;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements WeatherServiceCallback {
    View rootView;
    private ImageView weatherIconImageView, weatherIconImageView_next;
    private TextView temperatureTextView, temperatureTextView_next, lowtempTextView,
            windTextView,sunriseTextView,
            sunsetTextView, dateTextView_now;
    private TextView conditionTextView, conditionTextView_next,recommendation;
    private TextView locationTextView, locationTextView_next, dateTextView_tomorrow;

    private String todayDate, temperatureDb, humidityDb, pressureDb, windDb, pollenDb;
    private YahooWeatherService service;
    private ProgressDialog dialog;

    ProgressBar myprogressBar;
    TextView progressingTextView;
    Handler progressHandler = new Handler();
    int i = 0;
    private GetPollenCount myAsyncTask = null;
    private boolean myAsyncTaskIsRunning = true;
    private int imageConditionCode;

    private ViewPager mViewPager;

    private CardPagerAdapter mCardAdapter;
    private ShadowTransformer mCardShadowTransformer;
    private CardFragmentPagerAdapter mFragmentCardAdapter;
    private ShadowTransformer mFragmentCardShadowTransformer;
    DatabaseHelper myDb;
    SQLiteDatabase sqLiteDatabase;
    ArrayList<Records> history;
//    SimpleRatingBar simpleRatingBar;
    RatingBar simpleRatingBar;
    TextView today_predict;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);

        myDb = new DatabaseHelper(this.getContext());
//        sqLiteDatabase = myDb.getWritableDatabase();
//        myDb.onUpgrade(sqLiteDatabase,2,3);
//        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe);
//        swipeRefreshLayout.setColorSchemeColors(android.R.color.holo_blue_dark, android.R.color.holo_green_light, android.R.color.holo_green_dark);
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                swipeRefreshLayout.setRefreshing(true);
//                Log.d("Swipe", "Refreshing");
//                ( new Handler()).postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        swipeRefreshLayout.setRefreshing(false);
//                        dialog.setMessage("Loading...");
//                        dialog.show();
//                        service.refreshWeather("Melbourne, Australia");
//
//                    }
//                }, 3000);
//            }
//        });

//        simpleRatingBar = (SimpleRatingBar) rootView.findViewById(R.id.myRatingBar);
        simpleRatingBar = (RatingBar) rootView.findViewById(R.id.myRatingBar);
        mViewPager = (ViewPager) rootView.findViewById(R.id.viewPager);

        List<Drawable> drawables = new ArrayList<>();
        Resources res = getResources();

        @SuppressWarnings("deprecation") Drawable drawableFlower = res.getDrawable(R.drawable.flower50);
        drawables.add(drawableFlower);

        @SuppressWarnings("deprecation") Drawable drawableMask = res.getDrawable(R.drawable.escapemask);
        drawables.add(drawableMask);

//        mCardAdapter = new CardPagerAdapter(this.getContext(),2,"ddd",drawables);
        mCardAdapter = new CardPagerAdapter(this.getContext(), 2, "Recommendation & Recommendation",drawables);

        mFragmentCardAdapter = new CardFragmentPagerAdapter(getFragmentManager(),
                dpToPixels(2, getActivity()));
        mCardShadowTransformer = new ShadowTransformer(mViewPager, mCardAdapter);
        mFragmentCardShadowTransformer = new ShadowTransformer(mViewPager, mFragmentCardAdapter);
        mViewPager.setAdapter(mCardAdapter);
        mViewPager.setPageTransformer(false, mCardShadowTransformer);
        mViewPager.setOffscreenPageLimit(3);

        //call function to display the last state
        getTheLastDiaryStressedSate();
        StartActivity startActivity = (StartActivity) getActivity();

        // Set title bar
        startActivity.setToolBar("Ultimate Inhaler", null);

        if(savedInstanceState!=null) {
            myAsyncTaskIsRunning = savedInstanceState.getBoolean("myAsyncTaskIsRunning");
        }
        if(myAsyncTaskIsRunning) {

                //Configure Progress Bar for pollen count
//                myprogressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
//
//
                progressingTextView = (TextView) rootView.findViewById(R.id.pollen_count_text);
                myAsyncTask = new GetPollenCount();
                myAsyncTask.execute();


                service = new YahooWeatherService(this);
                dialog = new ProgressDialog(this.getActivity());
                dialog.setMessage("Loading...");
                dialog.show();
                service.refreshWeather("Melbourne, Australia");


        }


//        //Configure Progress Bar for pollen count
//        myprogressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
//        progressingTextView = (TextView) rootView.findViewById(R.id.pollen_count_text);
//        GetPollenCount getPollenCount = new GetPollenCount();
//        getPollenCount.execute();

        //Configure recommendation
       // recommendation = (TextView) rootView.findViewById(R.id.recommendation);

        //Configure weather information
//        dateTextView_now= (TextView) rootView.findViewById(R.id.textView_today);
        weatherIconImageView = (ImageView) rootView.findViewById(R.id.weatherIconImageView);
        temperatureTextView = (TextView) rootView.findViewById(R.id.temperatureTextView);
//        conditionTextView = (TextView) rootView.findViewById(R.id.conditionTextView);
        locationTextView = (TextView) rootView.findViewById(R.id.locationTextView);
        today_predict = (TextView) rootView.findViewById(R.id.today_predict);

//        windTextView = (TextView) rootView.findViewById(R.id.windtextView_now);
//        sunriseTextView = (TextView) rootView.findViewById(R.id.sunrisetextView);
//        sunsetTextView = (TextView) rootView.findViewById(R.id.sunsettextView);

//        dateTextView_tomorrow = (TextView) rootView.findViewById(R.id.textView_tomorrow);
//        weatherIconImageView_next = (ImageView) rootView.findViewById(R.id.weatherIconImageView_next);
//        lowtempTextView = (TextView) rootView.findViewById(R.id.lowtemperaturetextView);
//        temperatureTextView_next = (TextView) rootView.findViewById(R.id.temperatureTextView_next);
//        conditionTextView_next = (TextView) rootView.findViewById(R.id.conditionTextView_next);
//        locationTextView_next = (TextView) rootView.findViewById(R.id.locationTextView_next);

//        service = new YahooWeatherService(this);
//        dialog = new ProgressDialog(this.getActivity());
//        dialog.setMessage("Loading...");
//        dialog.show();
//        service.refreshWeather("Melbourne, Australia");



        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void serviceSuccess(Channel channel) {
        dialog.hide();

        Item item = channel.getItem();

        int resourceId = getResources().getIdentifier("drawable/icon_" + item.getCondition().getCode(), null, getContext().getPackageName());
        int tomorrowId = getResources().getIdentifier("drawable/icon_" + item.getForecast().getCode(), null, getContext().getPackageName());

        imageConditionCode = resourceId;

        @SuppressWarnings("deprecation")
        Drawable weatherIconDrawable = getResources().getDrawable(resourceId);
        @SuppressWarnings("deprecation")
        Drawable weatherIconDrawable_next = getResources().getDrawable(tomorrowId);

        weatherIconImageView.setImageDrawable(weatherIconDrawable);
        int f = item.getCondition().getTemperature();
        double cn = (f - 32) *  5 / 9;
        int ctn = (int) Math.floor(cn);




        //Get the current system date
//        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
//        Date date = new Date();
//        dateTextView_now.setText(" Now, " + dateFormat.format(date));
//        dateTextView_now.setText(" Today's Weather");

        //Populate the weather info for today
//        temperatureTextView.setText(" " + item.getCondition().getTemperature() + "\u00B0" + channel.getUnits().getTemperature()
//        + "/ " + Integer.toString(ctn) + "\u00B0" + "C");
        temperatureTextView.setText("  " + Integer.toString(ctn) + "\u00B0" + "C");
//        conditionTextView.setText(" " + item.getCondition().getDescription());
        locationTextView.setText(" " + service.getLocation());
//        windTextView.setText(" Wind: " + channel.getWind().getSpeed() + " mph");
//        sunriseTextView.setText(" Sunrise: " + channel.getAstronomy().getSunrise());
//        sunsetTextView.setText(" Sunset: " + channel.getAstronomy().getSunset());


        //Get today date
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        todayDate = dateFormat.format(date);

        //Get the weather condition and save into database
        temperatureDb = Integer.toString(ctn);
        humidityDb = channel.getAtmosphere().getHumidity();
        pressureDb = channel.getAtmosphere().getPressure() + " in";
        windDb = channel.getWind().getSpeed() + " mph";

//        insertWeatherIntoDatabase(todayDate,temperatureDb,humidityDb,pressureDb,windDb,pollenDb);

        int[] foggy = {19,20,21,22};
        int[] thunderstorm = {0,1,2,3,4,5,6,35,37,38,39,40,45,47};
        int[] snow = {8,9,10,11,12,46};
        int[] heavySnow = {7,13,14,15,16,17,18,41,42,43};
        int[] windy = {23,24};
        int[] night = {27,29,31,33};
        int[] day = {25,26,28,30,32,34,36,44};
        if (Arrays.toString(foggy).matches(".*[\\[ ]" + imageConditionCode + "[\\],].*")) {

            List<Drawable> drawables = new ArrayList<>();
            Resources res = getResources();
            @SuppressWarnings("deprecation") Drawable drawableRain = res.getDrawable(R.drawable.torrentialrain48);
            drawables.add(drawableRain);

            @SuppressWarnings("deprecation") Drawable drawableCar = res.getDrawable(R.drawable.carpool50);
            drawables.add(drawableCar);

            @SuppressWarnings("deprecation") Drawable drawableMask = res.getDrawable(R.drawable.escapemask);
            drawables.add(drawableMask);

            String recom = "•\tBetter to stay indoors. \n" + "&" +
                    "•\tWhen travelling in the car, keep the windows shut and use recirculating air conditioning (if possible).\n"
                    + "&"
                    + "•\tConsider wearing a facemask in certain situations when allergy is severe and exposure to high amounts of pollen is unavoidable.\n";

            mCardAdapter = new CardPagerAdapter(this.getContext(),3,recom, drawables);
            today_predict.setText("You are Safe!");
            today_predict.setTextColor(Color.parseColor("#28CB1D"));

        }else if(Arrays.toString(thunderstorm).matches(".*[\\[ ]" + imageConditionCode + "[\\],].*")){
            List<Drawable> drawables = new ArrayList<>();
            Resources res = getResources();
            @SuppressWarnings("deprecation") Drawable drawableRain = res.getDrawable(R.drawable.torrentialrain48);
            drawables.add(drawableRain);

            String recom = "•\tIt is better to stay indoors during and after thunderstorms.\n";

            mCardAdapter = new CardPagerAdapter(this.getContext(),1,recom,drawables);

            today_predict.setText("Not suggest togo outside!");
            today_predict.setTextColor(Color.parseColor("#F96651"));

        }else if(Arrays.toString(snow).matches(".*[\\[ ]" + imageConditionCode + "[\\],].*")){

            List<Drawable> drawables = new ArrayList<>();
            Resources res = getResources();
            @SuppressWarnings("deprecation") Drawable drawableCar = res.getDrawable(R.drawable.carpool50);
            drawables.add(drawableCar);

            String recom = "•\tWhen travelling in the car, keep the windows shut and use recirculating air conditioning (if possible).\n";

            mCardAdapter = new CardPagerAdapter(this.getContext(),1,recom,drawables);
            today_predict.setText("You are Safe!");
            today_predict.setTextColor(Color.parseColor("#28CB1D"));

        }else if(Arrays.toString(heavySnow).matches(".*[\\[ ]" +imageConditionCode + "[\\],].*")){
            List<Drawable> drawables = new ArrayList<>();
            Resources res = getResources();
            @SuppressWarnings("deprecation") Drawable drawableRain = res.getDrawable(R.drawable.mobilehome50);
            drawables.add(drawableRain);

            String recom = "•\tBetter to stay indoors.\n";

            mCardAdapter = new CardPagerAdapter(this.getContext(),1,recom,drawables);

            today_predict.setText("Not suggest togo outside!");
            today_predict.setTextColor(Color.parseColor("#F96651"));

        }else if(Arrays.toString(windy).matches(".*[\\[ ]" + imageConditionCode + "[\\],].*")){

            List<Drawable> drawables = new ArrayList<>();
            Resources res = getResources();


            @SuppressWarnings("deprecation") Drawable drawableGlass = res.getDrawable(R.drawable.glasses48);
            drawables.add(drawableGlass);

            @SuppressWarnings("deprecation") Drawable drawableWashHand = res.getDrawable(R.drawable.washyourhands48);
            drawables.add(drawableWashHand);

            @SuppressWarnings("deprecation") Drawable drawableCarpet = res.getDrawable(R.drawable.carpetcleaning50);
            drawables.add(drawableCarpet);

            @SuppressWarnings("deprecation") Drawable drawableMask = res.getDrawable(R.drawable.escapemask);
            drawables.add(drawableMask);

            String recom = "•\tWear sunglasses when you are outside to help prevent pollen allergen from getting into your eyes.\n" + "&"
                    + "•\tIf you’ve been outside, wash your hands and face when you return home to reduce the amount of pollen allergen on your skin.\n" + "&"
                    + "•\tDry your bed linen and clothes indoors during the pollen season, if possible.\n" + "&"
                    + "•\tConsider wearing a facemask in certain situations when allergy is severe and exposure to high amounts of pollen is unavoidable.\n";

            mCardAdapter = new CardPagerAdapter(this.getContext(),4,recom,drawables);


            today_predict.setText("Not suggest togo outside!");
            today_predict.setTextColor(Color.parseColor("#F96651"));

        }else if(Arrays.toString(night).matches(".*[\\[ ]" + imageConditionCode + "[\\],].*"))
        {
            List<Drawable> drawables = new ArrayList<>();
            Resources res = getResources();

            @SuppressWarnings("deprecation") Drawable drawableFlower = res.getDrawable(R.drawable.flower50);
            drawables.add(drawableFlower);

            String recom = "•\t The evening — between 4pm and 6pm — can be the time of day with the greatest amount of pollen in the air. In other areas, the morning may be worse and should be a time when you try to stay indoors.\n";

            mCardAdapter = new CardPagerAdapter(this.getContext(),1,recom,drawables);

            today_predict.setText("You are Safe!");
            today_predict.setTextColor(Color.parseColor("#28CB1D"));

        }else if(Arrays.toString(day).matches(".*[\\[ ]" + imageConditionCode + "[\\],].*")){


            List<Drawable> drawables = new ArrayList<>();
            Resources res = getResources();

            @SuppressWarnings("deprecation") Drawable drawableMask = res.getDrawable(R.drawable.escapemask);
            drawables.add(drawableMask);

            @SuppressWarnings("deprecation") Drawable drawableCar = res.getDrawable(R.drawable.carpool50);
            drawables.add(drawableCar);

            @SuppressWarnings("deprecation") Drawable drawableCarpet = res.getDrawable(R.drawable.carpetcleaning50);
            drawables.add(drawableCarpet);


            String recom = "•\tConsider wearing a facemask in certain situations when allergy is severe and exposure to high amounts of pollen is unavoidable.\n" + "&"
                    + "•\tWhen travelling in the car, keep the windows shut and use recirculating air conditioning (if possible).\n" + "&"
                    + "•\tDry your bed linen and clothes indoors during the pollen season, if possible.\n";
            mCardAdapter = new CardPagerAdapter(this.getContext(),3,recom,drawables);

            today_predict.setText("You are Safe!");
            today_predict.setTextColor(Color.parseColor("#28CB1D"));
        }else {

            List<Drawable> drawables = new ArrayList<>();
            Resources res = getResources();

            @SuppressWarnings("deprecation") Drawable drawableMask = res.getDrawable(R.drawable.escapemask);
            drawables.add(drawableMask);

            @SuppressWarnings("deprecation") Drawable drawableCar = res.getDrawable(R.drawable.carpool50);
            drawables.add(drawableCar);

            @SuppressWarnings("deprecation") Drawable drawableCarpet = res.getDrawable(R.drawable.carpetcleaning50);
            drawables.add(drawableCarpet);


            String recom = "•\tConsider wearing a facemask in certain situations when allergy is severe and exposure to high amounts of pollen is unavoidable.\n" + "&"
                    + "•\tWhen travelling in the car, keep the windows shut and use recirculating air conditioning (if possible).\n" + "&"
                    + "•\tDry your bed linen and clothes indoors during the pollen season, if possible.\n";
            mCardAdapter = new CardPagerAdapter(this.getContext(),3,recom,drawables);

            today_predict.setText("You are Safe!");
            today_predict.setTextColor(Color.parseColor("#28CB1D"));
        }

        mCardAdapter.notifyDataSetChanged();
        mCardShadowTransformer = new ShadowTransformer(mViewPager, mCardAdapter);

        mViewPager.setAdapter(mCardAdapter);
        mViewPager.setPageTransformer(false, mCardShadowTransformer);


    }

    @Override
    public void serviceFailure(Exception exception) {
        dialog.hide();
        Log.v("Weather exception", exception.getMessage());
    }


    public static float dpToPixels(int dp, Context context) {
        return dp * (context.getResources().getDisplayMetrics().density);
    }

    /*
    GetPollenCount
    Get the pollen count

     */

    //Get the pollen count
    private class GetPollenCount extends AsyncTask<String, Void, String> {

        @Override
        /*
        doInBackground
        @

         */
        protected String doInBackground(String... args) {
            URL url;
            HttpURLConnection conn = null;
            String resmsg = "";
            // Making HTTP request
            try {
                url = new URL("https://socialpollencount.co.uk/api/forecast?location=[51.7546407,-1.2510746]");
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                //make some HTTP headers
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                //Read response
                InputStream instream = new BufferedInputStream(conn.getInputStream());
                BufferedReader buffer = new BufferedReader(new InputStreamReader(instream));
                String s;
                while ((s = buffer.readLine()) != null) {
                    resmsg += s;
                }
            } catch (Exception e) {
                Log.e("JSON", e.getMessage());
                Sentry.captureException(e);

            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
            return resmsg;
        }

        @Override
        protected void onPostExecute(String s) {
            JSONArray count = null;
            String pollencount = null;
            try {
                JSONObject raw = new JSONObject(s);
                count = raw.getJSONArray("forecast");
                pollencount = count.getJSONObject(0).getString("pollen_count");
                if (pollencount.equals("Low")){
                    progressingTextView.setText("Low");
//                    myprogressBar.setProgress(30);
                }else if (pollencount.equals("Moderate")){
                    progressingTextView.setText("Moderate");
//                    myprogressBar.setProgress(50);
                }else if (pollencount.equals("High")){
                    progressingTextView.setText("High");
//                    myprogressBar.setProgress(80);
                }else {
                    progressingTextView.setText("No information");
//                    myprogressBar.setProgress(100);
                }

                getResources().getString(R.string.app_name);

                myAsyncTaskIsRunning = false;
                myAsyncTask = null;
            } catch (JSONException e) {
                e.printStackTrace();
                Sentry.captureException(e);

            }
            pollencount = "Low";
            progressingTextView.setText(pollencount);

            pollenDb = pollencount;

        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("myAsyncTaskIsRunning",myAsyncTaskIsRunning);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(myAsyncTask!=null) myAsyncTask.cancel(true);
        myAsyncTask = null;

    }


    public void insertWeatherIntoDatabase(String date, String temperature, String humidity,
                                          String pressure, String wind, String pollen){
        try{
            if (myDb.todayWeatherExist(date) == 0){
                myDb.insertWeatherIntoDatabase(date,temperature,humidity,pressure,wind,pollen);
            }else
            {
                myDb.updateTodayWeatherRecord(date,temperature,humidity,pressure,wind,pollen);
            }
        }catch (Exception e){
//            Toast.makeText(getContext(),"Weather Database error",Toast.LENGTH_LONG).show();
            Log.v("Weather databse error", e.getMessage());
        }


    }

    public void getTheLastDiaryStressedSate(){

        history = myDb.getRecords();
        TextView valueTV = new TextView(getContext());

        if (history.size() != 0){
            simpleRatingBar.setVisibility(View.VISIBLE);
            valueTV.setVisibility(View.GONE);
            //Order the records by DESC
            Collections.sort(history, new Comparator<Records>() {
                @Override
                public int compare(Records lhs, Records rhs) {
                    Calendar calendarlhs = Calendar.getInstance();
                    Calendar calendarrhs = Calendar.getInstance();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // HH:mm:ss
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

            simpleRatingBar.setClickable(false);
            simpleRatingBar.setFocusable(false);
            if (Integer.valueOf(history.get(history.size() - 1).getInhaler()) == 0) {
                simpleRatingBar.setRating(0);
            } else if (Integer.valueOf(history.get(history.size() - 1).getInhaler()) >= 1 &&
                    Integer.valueOf(history.get(history.size() - 1).getInhaler()) <= 5) {
                simpleRatingBar.setRating(1);
            } else if (Integer.valueOf(history.get(history.size() - 1).getInhaler()) >= 6 &&
                    Integer.valueOf(history.get(history.size() - 1).getInhaler()) <= 12) {
                simpleRatingBar.setRating(3);
            } else if (Integer.valueOf(history.get(history.size() - 1).getInhaler()) >= 13 &&
                    Integer.valueOf(history.get(history.size() - 1).getInhaler()) <= 20) {
                simpleRatingBar.setRating(4);
            } else {
                simpleRatingBar.setRating(0);
            }
        }else{

            LinearLayout linearLayout = (LinearLayout) rootView.findViewById(R.id.stateOverView);
            valueTV.setText("Track your attack now!");
            //noinspection ResourceType
            valueTV.setId(5);
            valueTV.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));

            ((LinearLayout) linearLayout).addView(valueTV);
            simpleRatingBar.setVisibility(View.GONE);
        }



    }




}
