package monash.ultimateinhaler;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    Toolbar toolbar = null;
    NavigationView navigationView = null;
    String username, email, passwd,title;
    TextView nav_user, nav_email;
    FloatingActionButton fab_plus;
    FloatingActionButton fab_weather;
    FloatingActionButton fab_hospital;
    FloatingActionButton fab_tips;
    Animation FabOpen, FabClose, FabRClockwise, FabRanticlockwise;
    boolean isOpen = false, isNotlogin=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Initiate
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0);

        title = getString(R.string.app_name);

//        nav_user = (TextView)hView.findViewById(R.id.username_header);
//        nav_email = (TextView)hView.findViewById(R.id.email_header);

        //Floating Buttons
        fab_plus = (FloatingActionButton) findViewById(R.id.fab_plus);
        fab_hospital = (FloatingActionButton) findViewById(R.id.fab_hospital);
        fab_weather = (FloatingActionButton) findViewById(R.id.fab_weather);
        fab_tips = (FloatingActionButton) findViewById(R.id.fab_tips);
        FabOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        FabClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        FabRClockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_clockwise);
        FabRanticlockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.roate_anticlockwise);

        fab_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpen){
                    fab_weather.startAnimation(FabClose);
                    fab_hospital.startAnimation(FabClose);
                    fab_tips.startAnimation(FabClose);
                    fab_plus.startAnimation(FabRanticlockwise);
                    fab_weather.setClickable(false);
                    fab_hospital.setClickable(false);
                    fab_tips.setClickable(false);
                    isOpen=false;
                }else {
                    fab_weather.startAnimation(FabOpen);
                    fab_hospital.startAnimation(FabOpen);
                    fab_tips.startAnimation(FabOpen);
                    fab_plus.startAnimation(FabRClockwise);
                    fab_weather.setClickable(true);
                    fab_hospital.setClickable(true);
                    fab_tips.setClickable(true);
                    isOpen=true;
                }
            }
        });

        fab_weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = "Forecast";
                MainFragment fragment = new MainFragment();
                android.support.v4.app.FragmentTransaction fragmentTransaction =
                        getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                //fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                if (getSupportActionBar() != null){
                    getSupportActionBar().setTitle(title);
                }
            }
        });

        fab_hospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = "Hospitals";

                FindHospitalsFragment fragment = new FindHospitalsFragment();
                android.support.v4.app.FragmentTransaction fragmentTransaction =
                        getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                //fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                if (getSupportActionBar() != null){
                    getSupportActionBar().setTitle(title);
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
                //fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                if (getSupportActionBar() != null){
                    getSupportActionBar().setTitle(title);
                }
            }
        });

        //Set the fragment initially
        MainFragment fragment = new MainFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction =
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

//        if(isNotlogin)
//        {
//            navigationView.getMenu().clear();
//            navigationView.inflateMenu(R.menu.navigation_with_login);
//            isNotlogin = false;
//        } else
//        {
//            navigationView.getMenu().clear();
//            navigationView.inflateMenu(R.menu.navigation_with_logout);
//            isNotlogin = true;
       // }

         navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            finish();

        }
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        //String title = getString(R.string.app_name);
        if (id == R.id.nav_home) {
            title = "Forecast";
            MainFragment fragment = new MainFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            //fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }else if (id == R.id.nav_inhaler) {
            title = "My Inhaler";
            FindMyInhalerFragment fragment = new FindMyInhalerFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            //fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_hospital) {
            title = "Hospitals";

            FindHospitalsFragment fragment = new FindHospitalsFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            //fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_tips) {
            title = "General Tips";
            TipsFragment fragment = new TipsFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            //fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();


        }  else if (id == R.id.nav_diary) {
            title = "Personal Dairy";
            PredictionFragment fragment = new PredictionFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_reminder) {
//            ReminderFragment fragment = new ReminderFragment();
//            android.support.v4.app.FragmentTransaction fragmentTransaction =
//                    getSupportFragmentManager().beginTransaction();
//            fragmentTransaction.replace(R.id.fragment_container, fragment);
//            //fragmentTransaction.addToBackStack(null);
//            fragmentTransaction.commit();

            LogInFragment fragment = new LogInFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            //fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        }

        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle(title);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
