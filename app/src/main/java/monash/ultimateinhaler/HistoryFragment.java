package monash.ultimateinhaler;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {
    View rootView;
    TableLayout tableLayout;
    DatabaseHelper myDb;
    ArrayList<Records> history;

    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_history, container, false);

        //Configure the action bar
        setHasOptionsMenu(true);

        //Open database
        myDb = new DatabaseHelper(this.getContext());

        StartActivity startActivity = (StartActivity) getActivity();

        // Set title bar
        startActivity.setToolBar("History", null);
        displayHistory();
        return rootView;
    }


    @SuppressWarnings("deprecation")
    public void displayHistory(){
        history = myDb.getRecords();
        for (int i = 0; i < history.size() ; i++) {
            tableLayout = (TableLayout) rootView.findViewById(R.id.history_table);

            TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);

            layoutParams.setMargins(10,10,10,10);

            TableRow tableRow = new TableRow(getContext());

            //Pupulate the date for history
            TextView diaryDate = new TextView(getContext());


            diaryDate.setText(history.get(i).getDate());
            diaryDate.setGravity(Gravity.CENTER);
            diaryDate.setTextSize(19);
            diaryDate.setLayoutParams(layoutParams);



            //Pupolate the stressed extent
//            RatingBar ratingBar = new RatingBar(getContext());
            SimpleRatingBar ratingBar = new SimpleRatingBar(getContext());

            ratingBar.setClickable(false);
            ratingBar.setFocusable(false);
            if (Integer.valueOf(history.get(i).getInhaler()) == 0) {
                ratingBar.setRating(0);
            }else if (Integer.valueOf(history.get(i).getInhaler()) >= 1 &&
                    Integer.valueOf(history.get(i).getInhaler()) <= 5){
                ratingBar.setRating(1);
            }else if (Integer.valueOf(history.get(i).getInhaler()) >= 6 &&
                    Integer.valueOf(history.get(i).getInhaler()) <= 12){
                ratingBar.setRating(3);
            }else if (Integer.valueOf(history.get(i).getInhaler()) >= 13 &&
                    Integer.valueOf(history.get(i).getInhaler()) <= 20)
            {
                ratingBar.setRating(4);
            }else {
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


                //Populate the pollen count
                pollen.setText(weatherCondition.getPollen());
//                Log.v("pollen", weatherCondition.getPollen());

            }else{
                //Populate the temperature
                temperature.setText("N/A");


                //Populate the pollen count
                pollen.setText("N/A");
            }

            tableRow.addView(diaryDate, 0);
            tableRow.addView(ratingBar, 1);
            tableRow.addView(temperature, 2);
            tableRow.addView(pollen, 3);
            tableRow.setBackground(getResources().getDrawable(R.drawable.tablerow_border));

            tableLayout.addView(tableRow, i +1);
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


}
