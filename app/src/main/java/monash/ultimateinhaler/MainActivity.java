package monash.ultimateinhaler;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import monash.ultimateinhaler.autoRecord.autoRecordService;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Toolbar toolbar = null;
    NavigationView navigationView = null;
    String username, email, passwd, title;
    TextView nav_user, nav_email;
    FloatingActionButton fab_plus;
    FloatingActionButton fab_weather;
    FloatingActionButton fab_hospital;
    FloatingActionButton fab_tips;
    FloatingActionButton fab_diary;

    Animation FabOpen, FabClose, FabRClockwise, FabRanticlockwise;
    boolean isOpen = false, isNotlogin = true;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());

       
        //Intent intent = new Intent(this , autoRecord.autoRecordService.class);
        this.startService( new Intent(this , autoRecordService.class));
        
        //Initiate
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView = navigationView.getHeaderView(0);

        title = getString(R.string.app_name);
        //noinspection ConstantConditions

//        nav_user = (TextView)hView.findViewById(R.id.username_header);
//        nav_email = (TextView)hView.findViewById(R.id.email_header);

        //Floating Buttons
        fab_plus = (FloatingActionButton) findViewById(R.id.fab_plus);
        fab_hospital = (FloatingActionButton) findViewById(R.id.fab_hospital);
        fab_weather = (FloatingActionButton) findViewById(R.id.fab_weather);
        fab_tips = (FloatingActionButton) findViewById(R.id.fab_tips);
        fab_diary = (FloatingActionButton) findViewById(R.id.fab_diary);
        FabOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        FabClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        FabRClockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_clockwise);
        FabRanticlockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.roate_anticlockwise);

        if (!CheckNetwork()) {

            Toast.makeText(MainActivity.this, "No internet!Please check your internet!", Toast.LENGTH_SHORT).show();
            openDialog();
        }
        fab_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpen) {
                    fab_weather.startAnimation(FabClose);
                    fab_hospital.startAnimation(FabClose);
                    fab_tips.startAnimation(FabClose);
                    fab_diary.startAnimation(FabClose);
                    fab_plus.startAnimation(FabRanticlockwise);
                    fab_weather.setClickable(false);
                    fab_hospital.setClickable(false);
                    fab_tips.setClickable(false);
                    fab_diary.setClickable(false);
                    isOpen = false;
                } else {
                    fab_weather.startAnimation(FabOpen);
                    fab_hospital.startAnimation(FabOpen);
                    fab_tips.startAnimation(FabOpen);
                    fab_diary.startAnimation(FabOpen);
                    fab_plus.startAnimation(FabRClockwise);
                    fab_weather.setClickable(true);
                    fab_hospital.setClickable(true);
                    fab_tips.setClickable(true);
                    fab_diary.setClickable(true);
                    isOpen = true;
                }
            }
        });

        fab_weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = "Ultimate Inhaler";
                MainFragment fragment = new MainFragment();
                android.support.v4.app.FragmentTransaction fragmentTransaction =
                        getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(title);
                    Resources res = getResources();
                    @SuppressWarnings("deprecation") Drawable drawablered = res.getDrawable(R.drawable.transparent);
                    getSupportActionBar().setBackgroundDrawable(drawablered);
                    getSupportActionBar().setTitle(Html.fromHtml("<font color=\"#000\">" + title + "</font>"));

                }
            }
        });

        fab_hospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = "Nearby";

                FindHospitalsFragment fragment = new FindHospitalsFragment();
                android.support.v4.app.FragmentTransaction fragmentTransaction =
                        getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(title);
                    Resources res = getResources();
                    @SuppressWarnings("deprecation") Drawable drawablered = res.getDrawable(R.drawable.transparent);
                    getSupportActionBar().setBackgroundDrawable(drawablered);
                    getSupportActionBar().setTitle(Html.fromHtml("<font color=\"#000\">" + title + "</font>"));

                }
            }
        });

        fab_tips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = "General Tips";
                TipsFragment fragment = new TipsFragment();
                android.support.v4.app.FragmentTransaction fragmentTransaction =
                        getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(title);
                    Resources res = getResources();
                    @SuppressWarnings("deprecation") Drawable drawablered = res.getDrawable(R.drawable.transparent);
                    getSupportActionBar().setBackgroundDrawable(drawablered);
                    getSupportActionBar().setTitle(Html.fromHtml("<font color=\"#000\">" + title + "</font>"));

                }
            }
        });

        fab_diary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = "Personal Diary";
                CalendarFragment fragment = new CalendarFragment();
                android.support.v4.app.FragmentTransaction fragmentTransaction =
                        getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(title);
                    Resources res = getResources();
                    @SuppressWarnings("deprecation") Drawable drawablered = res.getDrawable(R.drawable.transparent);
                    getSupportActionBar().setBackgroundDrawable(drawablered);
                    getSupportActionBar().setTitle(Html.fromHtml("<font color=\"#000\">" + title + "</font>"));

                }
            }
        });

        //Set the fragment initially
        MainFragment fragment = new MainFragment();
        FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);

        navigationView.setNavigationItemSelectedListener(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else if(getSupportFragmentManager().getBackStackEntryCount() != 0) {
//            getSupportFragmentManager().popBackStack();
//        }else{
//            super.onBackPressed();
//            finish();
//
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        String title = getString(R.string.app_name);
        if (id == R.id.nav_home) {
            title = "Ultimate Inhaler";
            MainFragment fragment = new MainFragment();
            FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_hospital) {
            title = "Nearby";

            FindHospitalsFragment fragment = new FindHospitalsFragment();
            FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_tips) {
            title = "General Tips";
            TipsFragment fragment = new TipsFragment();
            FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();


        } else if (id == R.id.nav_diary) {
            title = "Personal Dairy";
            CalendarFragment fragment = new CalendarFragment();
            FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_prediction) {
            title = "Prediction";
            PredictionFragment fragment = new PredictionFragment();
            FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_call) {
            onCall();

        }

        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle(title);
            Resources res = getResources();
            @SuppressWarnings("deprecation") Drawable drawablered = res.getDrawable(R.drawable.transparent);
            getSupportActionBar().setBackgroundDrawable(drawablered);
            getSupportActionBar().setTitle(Html.fromHtml("<font color=\"#000\">" + title + "</font>"));

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void onCall() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    123);
        } else {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:000"));

            startActivity(callIntent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case 123:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                } else {
                    Log.d("TAG", "Call Permission Not Granted");
                }
                break;

            default:
                break;
        }
    }

    //Open dialog when internet error occurs
    public void openDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Internet Error");
        alertDialog.setMessage("Make sure your open your internet connection!");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    //Check the mobile's network state
    private boolean CheckNetwork() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://monash.ultimateinhaler/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://monash.ultimateinhaler/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
