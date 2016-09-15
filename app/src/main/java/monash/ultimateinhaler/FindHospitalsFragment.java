package monash.ultimateinhaler;


import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
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
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 *
 * Get current location resource from
 * http://stackoverflow.com/questions/17519198/how-to-get-the-current-location-latitude-and-longitude-in-android
 */
public class FindHospitalsFragment extends Fragment implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    View rootView;
    private GoogleMap mMap;
    private double latitude, longitude;
    private UiSettings mUiSettings;
    GPSTracker gps;
    LatLng monash;
    //Define a request code to send to Google Play services
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private double currentLatitude;
    private double currentLongitude;
    List<String> quotes;

    public FindHospitalsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
        rootView = inflater.inflate(R.layout.fragment_find_hospitals, container, false);

            //Set the tool bar
            StartActivity startActivity = (StartActivity) getActivity();

            // Set title bar
            startActivity.setToolBar("Nearby",null);

        mGoogleApiClient = new GoogleApiClient.Builder(this.getContext())
                // The next two lines tell the new client that “this” current class will handle connection stuff
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                        //fourth line adds the LocationServices API endpoint from GooglePlayServices
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds


        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        } catch(InflateException e) {
            e.printStackTrace();
            Sentry.captureException(e);

            System.out.println("Predicted Time Fragment");
        }

        // Inflate the layout for this fragment
        return rootView;
        // Inflate the layout for this fragment
    }

    @Override
    public void onResume() {
        super.onResume();
        //Now lets connect to the API
        mGoogleApiClient.connect();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.v(this.getClass().getSimpleName(), "onPause()");

        //Disconnect from API onPause()
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }


    }

    /**
     * If connected get lat and long
     *
     */
    @Override
    public void onConnected(Bundle bundle) {
        try {
            Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            if (location == null) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

            } else {
                //If everything went fine lets get latitude and longitude
                currentLatitude = location.getLatitude();
                currentLongitude = location.getLongitude();

                monash = new LatLng(currentLatitude, currentLongitude);
                //Customize the marker
                mMap.addMarker(new MarkerOptions().position(monash).title("Current location")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).alpha(0.7f));

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(monash, 14));
                //Toast.makeText(this.getContext(), currentLatitude + " WORKS " + currentLongitude + "", Toast.LENGTH_LONG).show();


                //GetHospitalsGet the nearby park within 5 kms
                GetHospitals getHospitals = new GetHospitals();
                getHospitals.execute(Double.toString(currentLatitude), Double.toString(currentLongitude));

                //Get the biodiveristy
                GetBio(Double.toString(currentLatitude), Double.toString(currentLongitude));
            }
        }catch (Exception e){
            Sentry.captureException(e);
        }
    }


    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
            /*
             * Google Play services can resolve some errors it detects.
             * If the error has a resolution, try sending an Intent to
             * start a Google Play services activity that can resolve
             * error.
             */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this.getActivity(), CONNECTION_FAILURE_RESOLUTION_REQUEST);
                    /*
                     * Thrown if Google Play services canceled the original
                     * PendingIntent
                     */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
                Sentry.captureException(e);

            }
        } else {
                /*
                 * If no resolution is available, display a dialog to the
                 * user with the error.
                 */
            Log.e("Error", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    /**
     * If locationChanges change lat and long
     *
     *
     * @param location
     */
    @Override
    public void onLocationChanged(Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();
     //-37.876470,145.044078

       // Toast.makeText(this.getContext(), currentLatitude + " WORKS " + currentLongitude + "", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mUiSettings = mMap.getUiSettings();
        mMap.setPadding(100,100,0,200);

//        mMap.setPadding(0,0,300,0);

        mUiSettings.setZoomControlsEnabled(true);
        //LatLng monash = new LatLng(latitude, longitude);//-37.876470,145.044078


//        //Customize the marker
//        mMap.addMarker(new MarkerOptions().position(monash).title("Current location")
//                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).alpha(0.7f));
//
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(monash, 14));

        //GetHospitalsGet the nearby park within 5 kms
//         getHospitals = new GetHospitals();
//        getHospitals.execute(Double.toString(-37.876470), Double.toString(145.044078));

    }

    //Get the biodiversity
    public void GetBio(String lat, String lon){
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this.getContext());
        databaseAccess.open();
        quotes = databaseAccess.getQuotes(lat,lon);
        databaseAccess.close();

        for (int i =0; i < quotes.size()/2; i++) {
            String[] treeDetails = quotes.get(i).split(",");
            LatLng tree = new LatLng(Double.valueOf(treeDetails[2]), Double.valueOf(treeDetails[3]));
            String treeName = treeDetails[0];
            String scientificName = treeDetails[1];

            String vicinity = "Scientific Name: " + scientificName;
            //Add markers for trees
            mMap.addMarker(new MarkerOptions().position(tree).title(treeName).snippet(vicinity)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.tree)));

        }
    }

    //Get the hospitals within 5kms
    private class GetHospitals extends AsyncTask<String, Void, String> {
        String API_key = "AIzaSyAZs_2p8PfJ1BCrvdAzOiflrVBYTfOBS3c";

        @Override
        protected String doInBackground(String... args) {
            URL url;
            HttpURLConnection conn = null;
            String resmsg = "";
            // Making HTTP request
            try {
                url = new URL("https://maps.googleapis.com/maps/api/place/nearbysearch/json?radius=5000&types=hospital&location="
                        + args[0] + "," + args[1] + "&key=" + API_key);
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
            JSONArray parks = null;
            try {
                JSONObject raw = new JSONObject(s);
                parks = raw.getJSONArray("results");
                // Populate tap point
                for (int i = 0; i < parks.length(); i++) {
                    // set GeoPoints and title/snippet to be used in the annotation view
                    double lat = parks.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                    double lng = parks.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getDouble("lng");
                    String name = parks.getJSONObject(i).getString("name");
                    String vicinity = parks.getJSONObject(i).getString("vicinity");
                    LatLng park = new LatLng(lat, lng);

                    //Add markers for parks
                    mMap.addMarker(new MarkerOptions().position(park).title(name).snippet(vicinity).icon(BitmapDescriptorFactory.fromResource(R.drawable.first_aid_kit)));

                }
            } catch (JSONException e) {
                Log.e("1", e.getMessage());
                Sentry.captureException(e);

            }
        }

    }
}
//
//package monash.ultimateinhaler;
//
//
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.UiSettings;
//import com.google.android.gms.maps.model.BitmapDescriptorFactory;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.MarkerOptions;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.BufferedInputStream;
//import java.io.BufferedReader;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.List;
//
//
///**
// * A simple {@link Fragment} subclass.
// */
//public class FindHospitalsFragment extends Fragment implements OnMapReadyCallback{
//    View rootView;
//    private GoogleMap mMap;
//    String userId;
//    private double latitude, longitude;
//    private UiSettings mUiSettings;
//    GPSTracker gps;
//    List<String> quotes;
//
//    public FindHospitalsFragment() {
//        // Required empty public constructor
//    }
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//
//        rootView = inflater.inflate(R.layout.fragment_find_hospitals, container, false);
//
//
//        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
//
//
//        // Inflate the layout for this fragment
//        return rootView;
//        // Inflate the layout for this fragment
//    }
//
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//        mUiSettings = mMap.getUiSettings();
//        mMap.setPadding(0,0,300,0);
//        mUiSettings.setZoomControlsEnabled(true);
//        //LatLng monash = new LatLng(latitude, longitude);//-37.876470,145.044078
//
//        LatLng monash = new LatLng(-37.876470, 145.044078);//-37.876470,145.044078
//
//        //Customize the marker
//        mMap.addMarker(new MarkerOptions().position(monash).title("Current location")
//                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).alpha(0.7f));
//
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(monash, 14));
//
//        //Get the nearby park within 5 kms
//        GetHospitals getHospitals = new GetHospitals();
//        getHospitals.execute(Double.toString(-37.876470), Double.toString(145.044078));
//        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this.getContext());
//        databaseAccess.open();
//        quotes = databaseAccess.getQuotes("-37","145.");
//        databaseAccess.close();
//
//        for (int i =0; i < quotes.size()/2; i++) {
//            String[] treeDetails = quotes.get(i).split(",");
//            LatLng tree = new LatLng(Double.valueOf(treeDetails[7]), Double.valueOf(treeDetails[8]));
//            String treeName = treeDetails[0];
//            String genus = treeDetails[1];
//            String age = treeDetails[5];
//            String vicinity = "Genus: " + genus +". Date Planted: " + age;
//            //Add markers for trees
//            mMap.addMarker(new MarkerOptions().position(tree).title(treeName).snippet(vicinity)
//                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
//
//        }
//
//    }
//
//
//    //Get the hospitals within 5kms
//    private class GetHospitals extends AsyncTask<String, Void, String> {
//        String API_key = "AIzaSyAZs_2p8PfJ1BCrvdAzOiflrVBYTfOBS3c";
//
//        @Override
//        protected String doInBackground(String... args) {
//            URL url;
//            HttpURLConnection conn = null;
//            String resmsg = "";
//            // Making HTTP request
//            try {
//                url = new URL("https://maps.googleapis.com/maps/api/place/nearbysearch/json?radius=5000&types=hospital&location="
//                        + args[0] + "," + args[1] + "&key=" + API_key);
//                conn = (HttpURLConnection) url.openConnection();
//                conn.setReadTimeout(10000);
//                conn.setConnectTimeout(15000);
//                conn.setRequestMethod("GET");
//                //make some HTTP headers
//                conn.setRequestProperty("Content-Type", "application/json");
//                conn.setRequestProperty("Accept", "application/json");
//                //Read response
//                InputStream instream = new BufferedInputStream(conn.getInputStream());
//                BufferedReader buffer = new BufferedReader(new InputStreamReader(instream));
//                String s;
//                while ((s = buffer.readLine()) != null) {
//                    resmsg += s;
//                }
//            } catch (Exception e) {
//                Log.e("JSON", e.getMessage());
//            } finally {
//                if (conn != null) {
//                    conn.disconnect();
//                }
//            }
//            return resmsg;
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            JSONArray hospitals = null;
//            try {
//                JSONObject raw = new JSONObject(s);
//                hospitals = raw.getJSONArray("results");
//                // Populate tap point
//                for (int i = 0; i < hospitals.length(); i++) {
//                    // set GeoPoints and title/snippet to be used in the annotation view
//                    double lat = hospitals.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getDouble("lat");
//                    double lng = hospitals.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getDouble("lng");
//                    String name = hospitals.getJSONObject(i).getString("name");
//                    String vicinity = hospitals.getJSONObject(i).getString("vicinity");
//                    LatLng park = new LatLng(lat, lng);
//
//                    //Add markers for hospitals
//                    mMap.addMarker(new MarkerOptions().position(park).title(name).snippet(vicinity));
//                }
//
//
//
//            } catch (JSONException e) {
//                Log.e("1", e.getMessage());
//            }
//        }
//
//    }
//
//
//}

