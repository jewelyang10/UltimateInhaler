package monash.ultimateinhaler;


import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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
    private TextView temperatureTextView, temperatureTextView_next;
    private TextView conditionTextView, conditionTextView_next;
    private TextView locationTextView, locationTextView_next;

    private YahooWeatherService service;
    private ProgressDialog dialog;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe);
        swipeRefreshLayout.setColorSchemeColors(android.R.color.holo_blue_dark, android.R.color.holo_green_light,android.R.color.holo_green_dark);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                Log.d("Swipe", "Refreshing");
                ( new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        dialog.setMessage("Loading...");
                        dialog.show();
                        service.refreshWeather("Melbourne, Australia");

                    }
                }, 3000);
            }
        });
        weatherIconImageView = (ImageView) rootView.findViewById(R.id.weatherIconImageView);
        temperatureTextView = (TextView) rootView.findViewById(R.id.temperatureTextView);
        conditionTextView = (TextView) rootView.findViewById(R.id.conditionTextView);
        locationTextView = (TextView) rootView.findViewById(R.id.locationTextView);

        weatherIconImageView_next = (ImageView) rootView.findViewById(R.id.weatherIconImageView_next);
        temperatureTextView_next = (TextView) rootView.findViewById(R.id.temperatureTextView_next);
        conditionTextView_next = (TextView) rootView.findViewById(R.id.conditionTextView_next);
        locationTextView_next = (TextView) rootView.findViewById(R.id.locationTextView_next);

        service = new YahooWeatherService(this);
        dialog = new ProgressDialog(this.getActivity());
        dialog.setMessage("Loading...");
        dialog.show();
        service.refreshWeather("Melbourne, Australia");



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

        temperatureTextView.setText(item.getCondition().getTemperature() + "\u00B0" + channel.getUnits().getTemperature()
        + "/ " + Integer.toString(ctn) + "\u00B0" + "C");
        conditionTextView.setText(item.getCondition().getDescription());
        locationTextView.setText(service.getLocation());

        weatherIconImageView_next.setImageDrawable(weatherIconDrawable_next);

        int ft = Integer.valueOf(item.getForecast().getHigh());
        double ct = (ft - 32) *  5 / 9;
        int ctt = (int) Math.floor(ct);

        temperatureTextView_next.setText(item.getForecast().getHigh() + "\u00B0" + channel.getUnits().getTemperature()
                + "/ " + Integer.toString(ctt) + "\u00B0" + "C");
        conditionTextView_next.setText(item.getForecast().getText());
        locationTextView_next.setText(service.getLocation());
    }

    @Override
    public void serviceFailure(Exception exception) {
        dialog.hide();
        Toast.makeText(getActivity(), exception.getMessage(), Toast.LENGTH_LONG).show();;
    }



    //Get the pollen count
    private class GetPollenCount extends AsyncTask<String, Void, String> {
        String API_key = "AIzaSyAZs_2p8PfJ1BCrvdAzOiflrVBYTfOBS3c";

        @Override
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

        }

    }

}
