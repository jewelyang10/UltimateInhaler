package monash.ultimateinhaler;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.joshdholtz.sentry.Sentry;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.ArrayList;

import za.co.riggaroo.materialhelptutorial.TutorialItem;
import za.co.riggaroo.materialhelptutorial.tutorial.MaterialTutorialActivity;

public class StartActivity extends AppCompatActivity {
    private TextView mTextView;
    private int previousPosition;
    private Typeface ty1;
    private static final int REQUEST_CODE = 1234;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        // Sentry will look for uncaught exceptions from previous runs and send them
        Sentry.init(this.getApplicationContext(), "https://4c9327e0877c45bcbacee3fff7a38236:193fac1f37f74ba29983ecb1fb10be3f@sentry.io/96599");
        Sentry.captureMessage("Test test test");

        loadTutorial();

        //Configure the typeface
        ty1 = Typeface.createFromAsset(getAssets(),"fonts/PTSansWide.ttf");

        //Customize the title bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mTextView = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTextView.setText("Ultimate Inhaler");
        mTextView.setGravity(Gravity.CENTER);
        mTextView.setTypeface(ty1);
        mTextView.setTextSize(27);
        mTextView.setTypeface(mTextView.getTypeface(), Typeface.BOLD);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

      //  getSupportActionBar().setIcon(R.drawable.applogoblack);

        //noinspection deprecation
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.transparent));

        if (!CheckNetwork()) {

            Toast.makeText(StartActivity.this, "No internet!Please check your internet!", Toast.LENGTH_SHORT).show();
            openDialog();
        }
        //Set the fragment initially
        MainFragment fragment = new MainFragment();
        FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_containerStart, fragment);
        fragmentTransaction.commit();


        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);

        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                String title = "";
                switch (tabId) {
                    case R.id.nav_home:
                        title = "Ultimate Inhaler";
                        MainFragment fragment = new MainFragment();
                        FragmentTransaction fragmentTransaction =
                                getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_containerStart, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        break;
                    case R.id.nav_hospital:
                        title = "Nearby";

                        FindHospitalsFragment fragment2 = new FindHospitalsFragment();
                        FragmentTransaction fragmentTransaction2 =
                                getSupportFragmentManager().beginTransaction();
                        fragmentTransaction2.replace(R.id.fragment_containerStart, fragment2);
                        fragmentTransaction2.addToBackStack(null);
                        fragmentTransaction2.commit();                        break;
                    case R.id.nav_tips:
                        title = "Tips";

                        TipsFragment fragment3 = new TipsFragment();
                        FragmentTransaction fragmentTransaction3 =
                                getSupportFragmentManager().beginTransaction();
                        fragmentTransaction3.replace(R.id.fragment_containerStart, fragment3);
                        fragmentTransaction3.addToBackStack(null);
                        fragmentTransaction3.commit();                        break;
                    case R.id.nav_diary:
                        title = "Diary";

                        CalendarFragment fragment4 = new CalendarFragment();
                        FragmentTransaction fragmentTransaction4 =
                                getSupportFragmentManager().beginTransaction();
                        fragmentTransaction4.replace(R.id.fragment_containerStart, fragment4);
                        fragmentTransaction4.addToBackStack(null);
                        fragmentTransaction4.commit();                        break;
                    case R.id.nav_prediction:
                        title = "Prediction";

                        PredictionFragment fragment5 = new PredictionFragment();
                        FragmentTransaction fragmentTransaction5 =
                                getSupportFragmentManager().beginTransaction();
                        fragmentTransaction5.replace(R.id.fragment_containerStart, fragment5);
                        fragmentTransaction5.addToBackStack(null);
                        fragmentTransaction5.commit();                        break;
                }
                // update selected fragment and title
                if (getSupportActionBar() != null) {
                    mTextView.setText(title);
                    mTextView.setTypeface(ty1);

                }
            }
        });

        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {
            }
        });

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        //Bottom navigation
//        AHBottomNavigation bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);
//        // Create items
//        @SuppressWarnings("deprecation") AHBottomNavigationItem item1 = new AHBottomNavigationItem("Weather", R.drawable.ic_wb_sunny_black_24dp, R.color.fab2_color);
//        @SuppressWarnings("deprecation") AHBottomNavigationItem item2 = new AHBottomNavigationItem("Map", R.drawable.red_worldwide_location, R.color.fab1_color);
//        @SuppressWarnings("deprecation") AHBottomNavigationItem item3 = new AHBottomNavigationItem("Tips", R.drawable.idea, R.color.fab3_color);
//        @SuppressWarnings("deprecation") AHBottomNavigationItem item4 = new AHBottomNavigationItem("Diary", R.drawable.ic_mode_edit_black_24dp, R.color.fab4_color);
//
//
//// Add items
//        bottomNavigation.addItem(item1);
//        bottomNavigation.addItem(item2);
//        bottomNavigation.addItem(item3);
//        bottomNavigation.addItem(item4);
//
//// Set background colorFEFEFE
//        //bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#000000"));
//
//// Disable the translation inside the CoordinatorLayout
//        bottomNavigation.setBehaviorTranslationEnabled(false);
//
//// Change colors
////        bottomNavigation.setAccentColor(Color.parseColor("#F63D2B"));
////        bottomNavigation.setInactiveColor(Color.parseColor("#747474"));
//        bottomNavigation.setAccentColor(Color.parseColor("#FF4081"));
//        bottomNavigation.setInactiveColor(Color.parseColor("#747474"));
//
//// Force to tint the drawable (useful for font with icon for example)
//        bottomNavigation.setForceTint(true);
//
//// Force the titles to be displayed (against Material Design guidelines!)
//        bottomNavigation.setForceTitlesDisplay(true);
//
//// Use colored navigation with circle reveal effect
//        bottomNavigation.setColored(true);
//
//// Set current item programmatically
//        bottomNavigation.setCurrentItem(0);
//
//// Customize notification (title, background, typeface)
//        bottomNavigation.setNotificationBackgroundColor(Color.parseColor("#F63D2B"));
//
//// Add or remove notification for each item
//        bottomNavigation.setNotification("4", 1);
//        bottomNavigation.setNotification("", 1);
//
//// Set listeners
//        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
//            @Override
//            public boolean onTabSelected(int position, boolean wasSelected) {
//                // Do something cool here...
//                if (position == 0 && wasSelected == false) {
//                    MainFragment fragment = new MainFragment();
//                    FragmentTransaction fragmentTransaction =
//                            getSupportFragmentManager().beginTransaction();
//                    fragmentTransaction.replace(R.id.fragment_container, fragment);
//                    fragmentTransaction.addToBackStack(null);
//                    fragmentTransaction.commit();
//                } else if (position == 1 && wasSelected == false) {
//                    FindHospitalsFragment fragment = new FindHospitalsFragment();
//                    FragmentTransaction fragmentTransaction =
//                            getSupportFragmentManager().beginTransaction();
//                    fragmentTransaction.replace(R.id.fragment_container, fragment);
//                    fragmentTransaction.addToBackStack(null);
//                    fragmentTransaction.commit();
//                } else if (position == 2 && wasSelected == false) {
//                    TipsFragment fragment = new TipsFragment();
//                    FragmentTransaction fragmentTransaction =
//                            getSupportFragmentManager().beginTransaction();
//                    fragmentTransaction.replace(R.id.fragment_container, fragment);
//                    fragmentTransaction.addToBackStack(null);
//                    fragmentTransaction.commit();
//                } else if (position == 3 && wasSelected == false) {
//                    CalendarFragment fragment = new CalendarFragment();
//                    FragmentTransaction fragmentTransaction =
//                            getSupportFragmentManager().beginTransaction();
//                    fragmentTransaction.replace(R.id.fragment_container, fragment);
//                    fragmentTransaction.addToBackStack(null);
//                    fragmentTransaction.commit();
//                }else{
//                    Toast.makeText(getApplicationContext(), "Wrong", Toast.LENGTH_LONG).show();;
//                }
//                return true;
//            }
//        });
//        bottomNavigation.setOnNavigationPositionListener(new AHBottomNavigation.OnNavigationPositionListener() {
//            @Override
//            public void onPositionChange(int y) {
//                // Manage the new y position
//            }
//        });


    }

    @Override
    public void onBackPressed() {}

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

    //Open dialog when internet error occurs
    public void openDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(StartActivity.this).create();
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

    public void loadTutorial() {
        Intent mainAct = new Intent(this, MaterialTutorialActivity.class);
        mainAct.putParcelableArrayListExtra(MaterialTutorialActivity.MATERIAL_TUTORIAL_ARG_TUTORIAL_ITEMS, getTutorialItems(this));
        startActivityForResult(mainAct, REQUEST_CODE);

    }

    private ArrayList<TutorialItem> getTutorialItems(Context context) {
        TutorialItem tutorialItem1 = new TutorialItem(R.string.slide_1_biodiversity, R.string.slide_1_biodiversity_subtitle,
                R.color.slide_1, R.drawable.tut_page_1_front,  R.drawable.tut_page_1_background);

        TutorialItem tutorialItem2 = new TutorialItem(R.string.slide_2_diary, R.string.slide_2_diary_subtitle,
                R.color.slide_2,  R.drawable.tut_page_2_front,  R.drawable.tut_page_2_background);

        TutorialItem tutorialItem3 = new TutorialItem(R.string.slide_3_tips, R.string.slide_3_tips_subtitle,
                R.color.slide_3, R.drawable.tut_page_3_foreground,   R.drawable.tut_page_3_background);

        TutorialItem tutorialItem4 = new TutorialItem(R.string.slide_4_weather, R.string.slide_4_weather_subtitle,
                R.color.slide_4, R.drawable.tut_page_3_background, R.drawable.tut_page_4_background);

        ArrayList<TutorialItem> tutorialItems = new ArrayList<>();
        tutorialItems.add(tutorialItem1);
        tutorialItems.add(tutorialItem2);
        tutorialItems.add(tutorialItem3);
        tutorialItems.add(tutorialItem4);

        return tutorialItems;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //    super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE){
//            Toast.makeText(this, "Tutorial finished", Toast.LENGTH_LONG).show();

        }
    }

}
