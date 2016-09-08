package monash.ultimateinhaler;


import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Date;

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

    private YahooWeatherService service;
    private ProgressDialog dialog;

    ProgressBar myprogressBar;
    TextView progressingTextView;
    Handler progressHandler = new Handler();
    int i = 0;
    private GetPollenCount myAsyncTask = null;
    private boolean myAsyncTaskIsRunning = true;

    private ViewPager mViewPager;

    private CardPagerAdapter mCardAdapter;
    private ShadowTransformer mCardShadowTransformer;
    private CardFragmentPagerAdapter mFragmentCardAdapter;
    private ShadowTransformer mFragmentCardShadowTransformer;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
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

        mViewPager = (ViewPager) rootView.findViewById(R.id.viewPager);
        mCardAdapter = new CardPagerAdapter(this.getContext());
        mFragmentCardAdapter = new CardFragmentPagerAdapter(getFragmentManager(),
                dpToPixels(2, getActivity()));

        mCardShadowTransformer = new ShadowTransformer(mViewPager, mCardAdapter);
        mFragmentCardShadowTransformer = new ShadowTransformer(mViewPager, mFragmentCardAdapter);

        mViewPager.setAdapter(mCardAdapter);
        mViewPager.setPageTransformer(false, mCardShadowTransformer);
        mViewPager.setOffscreenPageLimit(3);

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

        @SuppressWarnings("deprecation")
        Drawable weatherIconDrawable = getResources().getDrawable(resourceId);
        @SuppressWarnings("deprecation")
        Drawable weatherIconDrawable_next = getResources().getDrawable(tomorrowId);

        weatherIconImageView.setImageDrawable(weatherIconDrawable);
        int f = item.getCondition().getTemperature();
        double cn = (f - 32) *  5 / 9;
        int ctn = (int) Math.floor(cn);

        //Get the current system date
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        Date date = new Date();
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

        //Classigy the condition code to give recommendtaions
        int[] foggy = {19,20,21,22};
        int[] thunderstorm = {0,1,2,3,4,5,6,35,37,38,39,40,45,47};
        int[] snow = {8,9,10,11,12,46};
        int[] heavySnow = {7,13,14,15,16,17,18,41,42,43};
        int[] windy = {23,24};
        int[] night = {27,29,31,33};
        int[] day = {25,26,28,30,32,34,36,44};
//        if (Arrays.toString(foggy).matches(".*[\\[ ]" + item.getCondition().getCode() + "[\\],].*")) {
//            recommendation.setText("•\tStay indoors. \n" +
//                    "•\tWhen travelling in the car, keep the windows shut and use recirculating air conditioning (if possible).\n"
//            + "•\tConsider wearing a facemask in certain situations when allergy is severe and exposure to high amounts of pollen is unavoidable.\n");
//        }else if(Arrays.toString(thunderstorm).matches(".*[\\[ ]" + item.getCondition().getCode() + "[\\],].*")){
//            recommendation.setText("•\tStay indoors during and after thunderstorms.\n");
//        }else if(Arrays.toString(snow).matches(".*[\\[ ]" + item.getCondition().getCode() + "[\\],].*")){
//            recommendation.setText("•\tWhen travelling in the car, keep the windows shut and use recirculating air conditioning (if possible).\n");
//        }else if(Arrays.toString(heavySnow).matches(".*[\\[ ]" + item.getCondition().getCode() + "[\\],].*")){
//            recommendation.setText("•\tStay indoors. \n");
//        }else if(Arrays.toString(windy).matches(".*[\\[ ]" + item.getCondition().getCode() + "[\\],].*")){
//            recommendation.setText("•\tWear sunglasses when you are outside to help prevent pollen allergen from getting into your eyes.\n"
//            + "•\tIf you’ve been outside, wash your hands and face when you return home to reduce the amount of pollen allergen on your skin.\n"
//            + "•\tDry your bed linen and clothes indoors during the pollen season, if possible.\n"
//            + "•\tConsider wearing a facemask in certain situations when allergy is severe and exposure to high amounts of pollen is unavoidable.\n");
//        }else if(Arrays.toString(night).matches(".*[\\[ ]" + item.getCondition().getCode() + "[\\],].*")){
//            recommendation.setText("•\t The evening — between 4pm and 6pm — can be the time of day with the greatest amount of pollen in the air. In other areas, the morning may be worse and should be a time when you try to stay indoors.\n");
//        }else if(Arrays.toString(day).matches(".*[\\[ ]" + item.getCondition().getCode() + "[\\],].*")){
//            recommendation.setText("•\tConsider wearing a facemask in certain situations when allergy is severe and exposure to high amounts of pollen is unavoidable.\n"
//            + "•\tWhen travelling in the car, keep the windows shut and use recirculating air conditioning (if possible).\n"
//            + "•\tDry your bed linen and clothes indoors during the pollen season, if possible.\n"
//            + "•\tConsider having a low-allergen garden incorporating low-allergen plants and shrubs and a lawn or ground cover that needs infrequent or no mowing. Low-allergen plants tend to be those that are pollinated by insects or birds, rather than by wind, and include many native trees and shrubs.\n");
//        }else {
//            recommendation.setText("•\tConsider wearing a facemask in certain situations when allergy is severe and exposure to high amounts of pollen is unavoidable.\n"
//                    + "•\tWhen travelling in the car, keep the windows shut and use recirculating air conditioning (if possible).\n"
//                    + "•\tDry your bed linen and clothes indoors during the pollen season, if possible.\n"
//                    + "•\tConsider having a low-allergen garden incorporating low-allergen plants and shrubs and a lawn or ground cover that needs infrequent or no mowing. Low-allergen plants tend to be those that are pollinated by insects or birds, rather than by wind, and include many native trees and shrubs.\n");
//
//        }


        //Weathe info for tomorrow
//        dateTextView_tomorrow.setText(" " + channel.getItem().getForecast().getDay() + ", " +
//        channel.getItem().getForecast().getDate());
//        weatherIconImageView_next.setImageDrawable(weatherIconDrawable_next);
//
//        //Parse the F to C
//        int ft = Integer.valueOf(item.getForecast().getHigh());
//        double ct = (ft - 32) *  5 / 9;
//        int ctt = (int) Math.floor(ct);
//
//        int fl = Integer.valueOf(item.getForecast().getLow());
//        double cl = (fl - 32) *  5 / 9;
//        int clt = (int) Math.floor(cl);
//
//        temperatureTextView_next.setText(" High: " + item.getForecast().getHigh() + "\u00B0"
//                + channel.getUnits().getTemperature()
//                + "/ " + Integer.toString(ctt) + "\u00B0" + "C");
//        lowtempTextView.setText(" Low: " + item.getForecast().getLow() + "\u00B0"
//                + channel.getUnits().getTemperature()
//                + "/ " + Integer.toString(clt) + "\u00B0" + "C" );
//        conditionTextView_next.setText(" " + item.getForecast().getText());
//        locationTextView_next.setText(" " + service.getLocation());


    }

    @Override
    public void serviceFailure(Exception exception) {
        dialog.hide();
        Toast.makeText(getActivity(), exception.getMessage(), Toast.LENGTH_LONG).show();
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
            try {
                JSONObject raw = new JSONObject(s);
                count = raw.getJSONArray("forecast");
                String pollencount = count.getJSONObject(0).getString("pollen_count");
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




}
