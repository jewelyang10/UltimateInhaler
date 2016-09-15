package monash.ultimateinhaler;

import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.joshdholtz.sentry.Sentry;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * A simple {@link Fragment} subclass.
 */
public class CalendarFragment extends Fragment implements Serializable {
    private boolean undo = false;
    private CaldroidFragment caldroidFragment;
    private CaldroidFragment dialogCaldroidFragment;
    private View rootView;
    protected String selectedDate;
    protected ArrayList<Records> records;
    private  NumberPicker np;
    DatabaseHelper myDb;
    SQLiteDatabase sqLiteDatabase;
    String selectedNo;
    Date previoursDateSelected;
    Button addDiary, showHistory;

    private void setCustomResourceForDates() {
        Calendar cal = Calendar.getInstance();

        // Min date is last 7 days
//        cal.add(Calendar.DATE, 0);
//        Date blueDate = cal.getTime();

        // Max date is next 7 days
//        cal = Calendar.getInstance();
//        cal.add(Calendar.DATE, 0);
//        Date greenDate = cal.getTime();
//
//        if (caldroidFragment != null) {
//            @SuppressWarnings("deprecation")
//            //ColorDrawable blue = new ColorDrawable(getResources().getColor(R.color.caldroid_light_red));
//            ColorDrawable green = new ColorDrawable(getResources().getColor(R.color.caldroid_light_red));
//           // caldroidFragment.setBackgroundDrawableForDate(blue, blueDate);
//            caldroidFragment.setBackgroundDrawableForDate(green, greenDate);
//            //caldroidFragment.setTextColorForDate(R.color.colorAccent, blueDate);
//            caldroidFragment.setTextColorForDate(R.color.colorAccent, greenDate);
//        }
    }


    public CalendarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_calendar, container, false);
        myDb = new DatabaseHelper(this.getContext());
        //        sqLiteDatabase = myDb.getWritableDatabase();
        //        myDb.onUpgrade(sqLiteDatabase,2,3);

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        StartActivity startActivity = (StartActivity) getActivity();

        // Set title bar
        startActivity.setToolBar("Diary", null);
        final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");

        //Configure the button
        addDiary = (Button) rootView.findViewById(R.id.addDiary_button);
        showHistory = (Button) rootView.findViewById(R.id.showHistory_button);

        //Configure the typeface
        Typeface ty1 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/PTSans/PTSansRegular.ttf");
        addDiary.setTypeface(ty1);

        showHistory.setTypeface(ty1);


        //Set click listner for add diary button
        addDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigateToDiaryEntryFragment();
            }
        });
        showHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToHistoryFragment();
            }
        });
        // Setup caldroid fragment
        // **** If you want normal CaldroidFragment, use below line ****
        //caldroidFragment = new CaldroidFragment();

        // //////////////////////////////////////////////////////////////////////
        // **** This is to show customized fragment. If you want customized
        // version, uncomment below line ****
        try {
            caldroidFragment = new CaldroidSampleCustomFragment();
            // Setup arguments
            if (savedInstanceState != null) {
                caldroidFragment.restoreStatesFromKey(savedInstanceState,
                        "CALDROID_SAVED_STATE");
            }
            // If activity is created from fresh
            else {
                // If activity is created from fresh
                Bundle args = new Bundle();
                Calendar cal = Calendar.getInstance();
                args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
                args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
                args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
                args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);

                // Uncomment this to customize startDayOfWeek
                // args.putInt(CaldroidFragment.START_DAY_OF_WEEK,
                // CaldroidFragment.TUESDAY); // Tuesday

                // Uncomment this line to use Caldroid in compact mode
                // args.putBoolean(CaldroidFragment.SQUARE_TEXT_VIEW_CELL, false);

                // Uncomment this line to use dark theme
                 args.putInt(CaldroidFragment.THEME_RESOURCE, R.style.CaldroidCustomized);

                caldroidFragment.setArguments(args);
            }

            setCustomResourceForDates();

            // Attach to the activity
            FragmentTransaction t = getActivity().getSupportFragmentManager().beginTransaction();
            t.replace(R.id.calendar1, caldroidFragment);
            t.commit();
            // Setup listener
            final CaldroidListener listener = new CaldroidListener() {

                @Override
                public void onSelectDate(Date date, View view) {
                    //Parse the date with custom format
                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                    String select = formatter.format(date);
                    String selectedDay = formatter.format(date).substring(0, 2);
//                    if (selectedDay.charAt(0) == '0') {
//                        selectedDay = selectedDay.substring(1, 2);
//                    }
                    String selectMon = formatter.format(date).substring(3, 5);
//                    if (selectMon.charAt(0) == '0') {
//                        selectMon = selectMon.substring(1, 2);
//                    }
                    String selectYear = formatter.format(date).substring(6, 10);
                    selectedDate = selectedDay + "-" + selectMon + "-" + selectYear;
                    String selectedDatesString = selectYear + "-" + selectMon + "-" + selectedDay + " 16:21:32";
                    try {
                        SimpleDateFormat formatterToDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                        //Only one date will be selected
                        caldroidFragment.setSelectedDates(formatterToDate.parse(selectedDatesString),
                                formatterToDate.parse(selectedDatesString));
                    } catch (ParseException e) {
                        e.printStackTrace();
                        Sentry.captureException(e);

                    }
                    //displayEventDetailForSelectedDate(selectedDate);
                    //Get the last week seven days date
                    Calendar c = Calendar.getInstance();
                    c.add(Calendar.DATE, 0);
                    Date todayDate = c.getTime();
                    if (date.after(todayDate)) {
//                        android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(getActivity()).create();
//                        alertDialog.setTitle("Notice");
//                        alertDialog.setMessage("Date should be the past!");
//                        alertDialog.setButton(android.app.AlertDialog.BUTTON_NEUTRAL, "OK",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        dialog.dismiss();
//                                    }
//                                });
//                        alertDialog.show();
                        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Notice")
                                .setContentText("You must select a date!")
                                .setConfirmText("Yes, got it!")
                                .show();
                        selectedDate = null;
                    }
                    caldroidFragment.refreshView();

                }

                @Override
                public void onChangeMonth(int month, int year) {
                    String text = "month: " + month + " year: " + year;
                }

                @Override
                public void onLongClickDate(Date date, View view) {
                }

                @Override
                public void onCaldroidViewCreated() {
                    if (caldroidFragment.getLeftArrowButton() != null) {
                    }
                }

            };

            // Setup Caldroid
            caldroidFragment.setCaldroidListener(listener);

            displayEventOnCalendar();
        }catch (Exception e){
            System.out.print(e.getMessage());
            Sentry.captureException(e);

        }

        return rootView;
    }



    /**
     * Save current states of the Caldroid here
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);

        if (caldroidFragment != null) {
            caldroidFragment.saveStatesToKey(outState, "CALDROID_SAVED_STATE");
        }

        if (dialogCaldroidFragment != null) {
            dialogCaldroidFragment.saveStatesToKey(outState,
                    "DIALOG_CALDROID_SAVED_STATE");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_add_diary, menu);  // Use filter.xml from step 1
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add_diary) {
            NavigateToDiaryEntryFragment();

//            if (selectedDate != null) {
//
//                //When the user selected a date and click the add button on the title bar, user will be allowed to input diary.
//                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//                builder.setTitle("Diary Entry");
//
//                // I'm using fragment here so I'm using getView() to provide ViewGroup
//                // but you can provide here any other instance of ViewGroup from your Fragment / Activity
//                final View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.text_input_dialog, (ViewGroup) getView(), false);
//
//                // Set up the input
//                final RadioGroup newWithAsthmaGroup = (RadioGroup) viewInflated.findViewById(R.id.radioGroup_newWithAsthma);
//                final RadioGroup attackedTodayGroup = (RadioGroup) viewInflated.findViewById(R.id.radioGroup_attackedToday);
//                RadioButton yesNewWithAsthma = (RadioButton) viewInflated.findViewById(R.id.yesButton_newWithAsthma);
//                RadioButton noNewWithAsthma = (RadioButton) viewInflated.findViewById(R.id.noButton_newWithAsthma);
//                RadioButton yesAttacked = (RadioButton) viewInflated.findViewById(R.id.yesButton_attackByAsthma);
//                RadioButton noAttacked = (RadioButton) viewInflated.findViewById(R.id.noButton_attackByAsthma);
//                final EditText othersText = (EditText) viewInflated.findViewById(R.id.editText_others);
//                final TextView timesAttacked = (TextView) viewInflated.findViewById(R.id.howManyTimesAttacked_textView);
//
//
//                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
//                builder.setView(viewInflated);
//
//                // Set up the buttons
//                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        int newAsthma = newWithAsthmaGroup.indexOfChild(viewInflated.findViewById(newWithAsthmaGroup.getCheckedRadioButtonId()));
//                        int attackedToday = attackedTodayGroup.indexOfChild(viewInflated.findViewById(attackedTodayGroup.getCheckedRadioButtonId()));
//                        String others = othersText.getText().toString();
//
//                        //Justify the date is selected and insert records into database
//
//                        try {
//                            if (selectedDate != null) {
//
//                                //Justify whether the current selected date's diary exist in db, if exists, just update the records.
//                                if (myDb.currentDayDiaryExist(selectedDate) != 0) {
//                                    if (myDb.updateCurrentDayRecord(selectedDate, newAsthma, attackedToday,
//                                            Integer.parseInt(selectedNo), others)) {
//
//                                        //make visible to program
//                                        Toast.makeText(getContext(), "Successfully update", Toast.LENGTH_SHORT).show();
//                                    }
//
//                                } else {
//                                    if (selectedNo != null) {
//                                        myDb.insertDataToUser(selectedDate, newAsthma, attackedToday,
//                                                Integer.parseInt(selectedNo), others);
//                                    } else {
//                                        myDb.insertDataToUser(selectedDate, newAsthma, attackedToday,
//                                                0, others);
//                                    }
//                                }
//
//                                displayEventOnCalendar();
//                            }
//                        }catch (Exception e){
//                            System.out.print(e.getMessage());
//                            Sentry.captureException(e);
//
//                        }
//
//                        dialog.dismiss();
//
//                    }
//                });
//                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                });
//
//                builder.show();
//
//                return true;
//            } else {
//                //If the user doesn't select a date and just click add button, the alert dialog will appear
//                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//                builder.setTitle("  ");
//                // I'm using fragment here so I'm using getView() to provide ViewGroup
//                // but you can provide here any other instance of ViewGroup from your Fragment / Activity
//                final View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.text_alert_dialog, (ViewGroup) getView(), false);
//                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
//                builder.setView(viewInflated);
//
//                // Set up the buttons
//                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                        dialog.dismiss();
//
//                    }
//                });
//                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                });
//
//                builder.show();
//
//                return true;
//            }

        }
        return super.onOptionsItemSelected(item);

    }


    public void displayEventOnCalendar(){

        //Get the diary date from database
        ArrayList<Date> displayDatabaseRecords = new ArrayList<>();
        records = myDb.getRecords();
        if (records.size() != 0) {
            for (int i = 0; i < records.size(); i++) {
                DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                Date dateFromDatabase = null;
                try {
                    dateFromDatabase = df.parse(records.get(i).getDate());
                    displayDatabaseRecords.add(dateFromDatabase);

                } catch (ParseException e) {
                    e.printStackTrace();
                    Sentry.captureException(e);

                    //Toast.m、dddiiakeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            try {
                //make visible to program
                Resources res = getResources();
                @SuppressWarnings("deprecation") Drawable drawablered = res.getDrawable(R.drawable.checkmark50);

                // To set the extraData:
                Map<String, Object> extraData = caldroidFragment.getExtraData();
                Map<String, Object> extraDataDatabase = caldroidFragment.getExtraData();

                //Parse the data
                extraData.put("DrawableRed", drawablered);
                extraDataDatabase.put("dateFromDatabase", displayDatabaseRecords);

                //Refresh the calendar
                caldroidFragment.refreshView();
            }catch (Exception e) {
                System.out.print(e.getMessage());
                Sentry.captureException(e);

            }
        }

    }

    public void NavigateToDiaryEntryFragment(){
        if (selectedDate != null) {

            Records records = myDb.getCurrentDayDiary(selectedDate);

            Bundle args = new Bundle();
            args.putSerializable("selectedDate", selectedDate);
            args.putSerializable("records", records);

            //Set the title of diary entry fragment
            String title = selectedDate;

            DiaryDetailsFragment fragment = new DiaryDetailsFragment();
            fragment.setArguments(args);

            FragmentTransaction fragmentTransaction =
                    getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_containerStart, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }else {
//            //If the user doesn't select a date and just click add button, the alert dialog will appear
//            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//            builder.setTitle("  ");
//            // I'm using fragment here so I'm using getView() to provide ViewGroup
//            // but you can provide here any other instance of ViewGroup from your Fragment / Activity
//            final View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.text_alert_dialog, (ViewGroup) getView(), false);
//            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
//            builder.setView(viewInflated);
//
//            // Set up the buttons
//            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//
//                    dialog.dismiss();
//
//                }
//            });
//            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.cancel();
//                }
//            });
//
//            builder.show();
            new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Notice")
                    .setContentText("You must select a date!")
                    .setConfirmText("Yes, got it!")
                    .show();
        }

    }


    public void navigateToHistoryFragment(){
        HistoryFragment fragment = new HistoryFragment();
        FragmentTransaction fragmentTransaction =
                getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_containerStart, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

//    public void displayEventDetailForSelectedDate(String selectedDateString){
//
//        //Get the diary date from database
//        Records records = myDb.getCurrentDayDiary(selectedDateString);
//        //Configure the textview on card
//        TextView getAttacked = (TextView)rootView.findViewById(R.id.getAttacked);
//        TextView attackedTimes = (TextView)rootView.findViewById(R.id.attacked_times);
//
//        if (records.getDate() != null) {
//            //Get the detail of records
//            if (records.getAttcked_today().equals("1")) {
//                getAttacked.setText("Didn't get attacked.");
//                attackedTimes.setText("Attacked Times: 0");
//            }else{
//                getAttacked.setText("Get attacked.");
//                attackedTimes.setText("Attacked Times: " + records.getAttack_times());
//            }
//        }else{
//            getAttacked.setText("No diary!");
//        }
//    }
}
