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
import android.text.Html;
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
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
    TextView predict;
    private String todayDate, temperatureDb, humidityDb, pressureDb, windDb, pollenDb;
    private YahooWeatherService service;
    private ProgressDialog dialog;

    String tomorrowPollen, tomorrowWeather;
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
    RatingBar simpleRatingBar;
    TextView today_predict;
    private int count;


    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        if (savedInstanceState == null){
            count = 0;
        }else{
            count = savedInstanceState.getInt("count");
            Log.v("savedState",Integer.toString(count));
        }
            rootView = inflater.inflate(R.layout.fragment_main, container, false);
        predict = (TextView) rootView.findViewById(R.id.tomorrow_predict);

            myDb = new DatabaseHelper(this.getContext());
//            sqLiteDatabase = myDb.getWritableDatabase();
//            myDb.onUpgrade(sqLiteDatabase,2,3);

            simpleRatingBar = (RatingBar) rootView.findViewById(R.id.myRatingBar);
            mViewPager = (ViewPager) rootView.findViewById(R.id.viewPager);

            List<Drawable> drawables = new ArrayList<>();
            Resources res = getResources();

            @SuppressWarnings("deprecation") Drawable drawableFlower = res.getDrawable(R.drawable.flower50);
            drawables.add(drawableFlower);

            @SuppressWarnings("deprecation") Drawable drawableMask = res.getDrawable(R.drawable.escapemask);
            drawables.add(drawableMask);

//        mCardAdapter = new CardPagerAdapter(this.getContext(),2,"ddd",drawables);
            mCardAdapter = new CardPagerAdapter(this.getContext(), 2, "Recommendation & Recommendation", drawables);

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

//        if(savedInstanceState!=null) {
//            myAsyncTaskIsRunning = savedInstanceState.getBoolean("myAsyncTaskIsRunning");
//        }
//        if(myAsyncTaskIsRunning) {

            //Configure Progress Bar for pollen count
//                myprogressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
//
//
            progressingTextView = (TextView) rootView.findViewById(R.id.pollen_count_text);


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

            //Get today date
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date date = new Date();
            todayDate = dateFormat.format(date);

            //Check today weather already exist or not

            if (myDb.todayWeatherExist(todayDate) == 0) {

                myAsyncTask = new GetPollenCount();
                try {
                    myAsyncTask.execute(generateKey("FGMG6acd"));
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                service = new YahooWeatherService(this);
                dialog = new ProgressDialog(this.getActivity());
                dialog.setMessage("Loading...");
                dialog.show();
                service.refreshWeather("Melbourne, Australia");

            } else {
                WeatherCondition weatherCondition = myDb.getWeatherByDiaryDateTracked(todayDate);

                //Today pollen info
                progressingTextView.setText(weatherCondition.getPollen());

                //Set the tomorrow prediction
                String text ="";
                //Set the prediction for tomorrow likelihood
                if (weatherCondition.getTomorrow_pollen().equals("Low")) {
                    text = "Tomorrow episode:  <font color=\"blue\">Low</font>";
                } else if (weatherCondition.getTomorrow_pollen().equals("Medium")) {
                    text = "Tomorrow episode:  <font color=\"yellow\">Medium</font>";
                } else if (weatherCondition.getTomorrow_pollen().equals("High")) {
                    text = "Tomorrow episode:  <font color=\"red\">High</font>";
                } else {
                    text = "Tomorrow episode:  <font color=\"red\">Low</font>";
                }
                predict.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);

                pollenDb = weatherCondition.getPollen();
                tomorrowPollen = weatherCondition.getTomorrow_pollen();

                service = new YahooWeatherService(this);
                dialog = new ProgressDialog(this.getActivity());
                dialog.setMessage("Loading...");
                dialog.show();
                service.refreshWeather("Melbourne, Australia");



            }

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putInt("count", count);
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

        insertWeatherIntoDatabase(todayDate,temperatureDb,humidityDb,pressureDb,windDb,pollenDb,tomorrowPollen);

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
//            today_predict.setText("Attack Likelihood: High");

            String text = "Asthma episode: <font color=\"red\">High</font>";
            today_predict.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
//            today_predict.setTextColor(Color.parseColor("#EADD3E"));

        }else if(Arrays.toString(thunderstorm).matches(".*[\\[ ]" + imageConditionCode + "[\\],].*")){
            List<Drawable> drawables = new ArrayList<>();
            Resources res = getResources();
            @SuppressWarnings("deprecation") Drawable drawableRain = res.getDrawable(R.drawable.torrentialrain48);
            drawables.add(drawableRain);

            String recom = "•\tIt is better to stay indoors during and after thunderstorms.\n";

            mCardAdapter = new CardPagerAdapter(this.getContext(),1,recom,drawables);

//            today_predict.setText("Attack Likelihood: High");

            String text = "Asthma episode: <font color=\"red\">High</font>";
            today_predict.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
//            today_predict.setTextColor(Color.parseColor("#F96651"));

        }else if(Arrays.toString(snow).matches(".*[\\[ ]" + imageConditionCode + "[\\],].*")){

            List<Drawable> drawables = new ArrayList<>();
            Resources res = getResources();
            @SuppressWarnings("deprecation") Drawable drawableCar = res.getDrawable(R.drawable.carpool50);
            drawables.add(drawableCar);

            String recom = "•\tWhen travelling in the car, keep the windows shut and use recirculating air conditioning (if possible).\n";

            mCardAdapter = new CardPagerAdapter(this.getContext(),1,recom,drawables);
//            today_predict.setText("Attack Likelihood: Low");

            String text = "Asthma episode: <font color=\"blue\">Low</font>";
            today_predict.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
//            today_predict.setTextColor(Color.parseColor("#EADD3E"));

        }else if(Arrays.toString(heavySnow).matches(".*[\\[ ]" +imageConditionCode + "[\\],].*")){
            List<Drawable> drawables = new ArrayList<>();
            Resources res = getResources();
            @SuppressWarnings("deprecation") Drawable drawableRain = res.getDrawable(R.drawable.mobilehome50);
            drawables.add(drawableRain);

            String recom = "•\tBetter to stay indoors.\n";

            mCardAdapter = new CardPagerAdapter(this.getContext(),1,recom,drawables);

//            today_predict.setText("Attack Likelihood: High");
            String text = "Asthma episode: <font color=\"blue\">Low</font>";
            today_predict.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);

//            today_predict.setTextColor(Color.parseColor("#F96651"));

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


//            today_predict.setText("Attack Likelihood: High");
            String text = "Asthma episode: <font color=\"red\">High</font>";
            today_predict.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);

//            today_predict.setTextColor(Color.parseColor("#F96651"));

        }else if(Arrays.toString(night).matches(".*[\\[ ]" + imageConditionCode + "[\\],].*"))
        {
            List<Drawable> drawables = new ArrayList<>();
            Resources res = getResources();

            @SuppressWarnings("deprecation") Drawable drawableFlower = res.getDrawable(R.drawable.flower50);
            drawables.add(drawableFlower);

            String recom = "•\t The evening — between 4pm and 6pm — can be the time of day with the greatest amount of pollen in the air. In other areas, the morning may be worse and should be a time when you try to stay indoors.\n";

            mCardAdapter = new CardPagerAdapter(this.getContext(),1,recom,drawables);

//            today_predict.setText("Attack Likelihood: Low");
            String text = "Asthma episode: <font color=\"blue\">Low</font>";
            today_predict.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);

//            today_predict.setTextColor(Color.parseColor("#EADD3E"));

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

            String text = "Asthma episode: <font color=\"blue\">Low</font>";
            today_predict.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);

//            today_predict.setText("Attack Likelihood: Low");
//            today_predict.setTextColor(Color.parseColor("#EADD3E"));
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

//            today_predict.setText("Attack Likelihood: Medium");
            String text = "Asthma episode: <font color=\"yellow\">Medium</font>";
            today_predict.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);

//            today_predict.setTextColor(Color.parseColor("#EADD3E"));
        }

        mCardAdapter.notifyDataSetChanged();
        mCardShadowTransformer = new ShadowTransformer(mViewPager, mCardAdapter);

        mViewPager.setAdapter(mCardAdapter);
        mViewPager.setPageTransformer(false, mCardShadowTransformer);


//        if(count == 0){
//            tr1 = new ViewTarget(R.id.today_predict, getActivity());
//            tr2 = new ViewTarget(R.id.temperatureTextView, getActivity());
//            tr3 = new ViewTarget(R.id.viewPager, getActivity());
//
//            showcaseView = new ShowcaseView.Builder(getActivity())
//                    .setTarget(Target.NONE)
//                    .setOnClickListener(this)
//                    .setContentTitle("Home Page")
//                    .setContentText("Provide your prediction and recommendations with current weather, pollen and last state.")
//                    .setStyle(R.style.CustomShowcaseTheme)
//                    .build();
//            Log.v("showcase times", "1");
//            showcaseView.setButtonText("OK");
//            count = 1;
//        }

    }

    @Override
    public void serviceFailure(Exception exception) {
        dialog.hide();
        Log.v("Weather exception", exception.getMessage());
    }


    public static float dpToPixels(int dp, Context context) {
        return dp * (context.getResources().getDisplayMetrics().density);
    }



    @Override
    public void onSaveInstanceSate(Bundle outState){
        super.onSaveInstanceState(outState);
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
                url = new URL("http://ws.weatherzone.com.au/?lt=twcid&lc=9477&locdet=1&fc=1&pollen=1&rollover=18&format=json&u=100205-867&k=" + args[0]);
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
                count = raw.getJSONArray("countries");
                pollencount = count.getJSONObject(0).getJSONArray("locations").getJSONObject(0).getJSONObject("local_forecasts").getJSONArray("forecasts").getJSONObject(0).getString("pollen_text");
                if (pollencount.equals("Low")) {
                    progressingTextView.setText("Low");
//                    myprogressBar.setProgress(30);
                } else if (pollencount.equals("Moderate")) {
                    progressingTextView.setText("Moderate");
//                    myprogressBar.setProgress(50);
                } else if (pollencount.equals("High")) {
                    progressingTextView.setText("High");
//                    myprogressBar.setProgress(80);
                } else {
                    progressingTextView.setText("No information");
//                    myprogressBar.setProgress(100);
                }

                getResources().getString(R.string.app_name);

//                myAsyncTaskIsRunning = false;
//                myAsyncTask = null;

                tomorrowPollen = count.getJSONObject(0).getJSONArray("locations").getJSONObject(0).getJSONObject("local_forecasts").getJSONArray("forecasts").getJSONObject(1).getString("pollen_text");
                tomorrowWeather = count.getJSONObject(0).getJSONArray("locations").getJSONObject(0).getJSONObject("local_forecasts").getJSONArray("forecasts").getJSONObject(1).getString("rain_prob");

            } catch (JSONException e) {
                e.printStackTrace();
                Sentry.captureException(e);

            }

            progressingTextView.setText(pollencount);
            String text = "";
            //Set the prediction for tomorrow likelihood
            if (tomorrowPollen != null) {
                if (tomorrowPollen.equals("Low")) {
                    text = "Tomorrow episode:  <font color=\"blue\">Low</font>";
                } else if (tomorrowPollen.equals("Medium")) {
                    text = "Tomorrow episode:  <font color=\"yellow\">Medium</font>";
                } else if (tomorrowPollen.equals("High")) {
                    text = "Tomorrow episode:  <font color=\"red\">High</font>";
                } else {
                    text = "Tomorrow episode:  <font color=\"red\">Low</font>";
                }
                predict.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);

                pollenDb = pollencount;

            }else{
                text = "Tomorrow episode:  <font color=\"blue\">Low</font>";

                predict.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);

                pollenDb = pollencount;
            }
        }

    }

//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putBoolean("myAsyncTaskIsRunning",myAsyncTaskIsRunning);
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        if(myAsyncTask!=null) myAsyncTask.cancel(true);
//        myAsyncTask = null;
//
//    }


    public void insertWeatherIntoDatabase(String date, String temperature, String humidity,
                                          String pressure, String wind, String pollen, String tomorrowPollen){
        try{
            if (myDb.todayWeatherExist(date) == 0){
                myDb.insertWeatherIntoDatabase(date,temperature,humidity,pressure,wind,pollen,tomorrowPollen);
            }else
            {
                myDb.updateTodayWeatherRecord(date,temperature,humidity,pressure,wind,pollen,tomorrowPollen);
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
            valueTV.setText("Track your attack!");
            //noinspection ResourceType
            valueTV.setId(5);
            valueTV.setTextColor(Color.RED);
//            valueTV.setClickable(true);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 12, 0, 0);
            valueTV.setLayoutParams(params);
            ((LinearLayout) linearLayout).addView(valueTV);
            simpleRatingBar.setVisibility(View.GONE);
        }
    }

    public static String generateKey(String password) throws
            NoSuchAlgorithmException {
        String key = null;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
        String today = dateFormat.format(calendar.getTime());
        if (today != null)
        {
            String[] str = today.split("/");
            key = Integer.toString(Integer.parseInt(str[0]) * 2 +
                    Integer.parseInt(str[1]) * 100 * 3 + Integer.parseInt(str[2]) * 10000 *
                    17) ;
            MessageDigest m = MessageDigest.getInstance("md5");
            byte[] data = key.getBytes();
            m.update(data, 0, data.length);
            BigInteger i = new BigInteger(1,
                    m.digest(password.getBytes()));
            return String.format("%1$032X", i);
        }
        return key;
    }

}


