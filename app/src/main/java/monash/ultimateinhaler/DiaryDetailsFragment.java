package monash.ultimateinhaler;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.shawnlin.numberpicker.NumberPicker;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * A simple {@link Fragment} subclass.
 */
public class DiaryDetailsFragment extends Fragment {
    View rootView;
    RadioButton tightChestNone, tightChestSome,tightChestAlot,
            wheezingNone,wheezingSome, wheezingAlot,tirednessNone, tirednessSome,tirednessAlot,
            feelingStressedNone,feelingStressedSome, feelingStressedAlot;
    RadioGroup tightChestGroup, wheezingGroup, tirednessGroup, feelingStressedGroup;
    Button saveTracker;
    ImageButton openInhalerWindow;
    String selectedNo;
    TextView totalInhalerText;
    DatabaseHelper myDb;
    SQLiteDatabase sqLiteDatabase;
    String passDate;
    Records records;

    public DiaryDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Configure the action bar
        setHasOptionsMenu(true);

        //Open the database helper
        myDb = new DatabaseHelper(this.getContext());
//                sqLiteDatabase = myDb.getWritableDatabase();
//                myDb.onUpgrade(sqLiteDatabase,2,3);


        //Get the date from calendar
        Bundle args = getArguments();
        passDate = (String) args.getSerializable("selectedDate");
        records = (Records) args.getSerializable("records");

        //Pass String to date formatt
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        try{
            Date date = formatter.parse(passDate);
            //Get the weekday from date
            DateFormat format2 = new SimpleDateFormat("EEEE");
            String finalDay =format2.format(date);
            StartActivity startActivity = (StartActivity) getActivity();

            // Set title bar
            startActivity.setToolBar(finalDay, passDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        rootView = inflater.inflate(R.layout.fragment_diary_details, container, false);

        tightChestGroup = (RadioGroup) rootView.findViewById(R.id.tightChest_radioGroup);
        tightChestNone = (RadioButton) rootView.findViewById(R.id.radio_button_tightChest_none);
        tightChestSome = (RadioButton) rootView.findViewById(R.id.radio_button_tightChest_some);
        tightChestAlot = (RadioButton) rootView.findViewById(R.id.radio_button_tightChest_alot);

        wheezingGroup = (RadioGroup) rootView.findViewById(R.id.wheezing_radioGroup);
        wheezingNone = (RadioButton) rootView.findViewById(R.id.radio_button_wheezing_none);
        wheezingSome = (RadioButton) rootView.findViewById(R.id.radio_button_wheezing_some);
        wheezingAlot = (RadioButton) rootView.findViewById(R.id.radio_button_wheezing_alot);

        tirednessGroup = (RadioGroup) rootView.findViewById(R.id.tiredness_radioGroup);
        tirednessNone = (RadioButton) rootView.findViewById(R.id.radio_button_tiredness_none);
        tirednessSome = (RadioButton) rootView.findViewById(R.id.radio_button_tiredness_some);
        tirednessAlot = (RadioButton) rootView.findViewById(R.id.radio_button_tiredness_alot);

        feelingStressedGroup = (RadioGroup) rootView.findViewById(R.id.others_radioGroup);
        feelingStressedNone = (RadioButton) rootView.findViewById(R.id.radio_button_others_none);
        feelingStressedSome = (RadioButton) rootView.findViewById(R.id.radio_button_others_some);
        feelingStressedAlot = (RadioButton) rootView.findViewById(R.id.radio_button_others_alot);

        saveTracker = (Button) rootView.findViewById(R.id.saveTracking_button);
        openInhalerWindow = (ImageButton) rootView.findViewById(R.id.open_inhaler_number_button);


        totalInhalerText = (TextView) rootView.findViewById(R.id.totalinhaler_text);

        SetDiaryDetailsIfDiaryAlreadyExist();


        saveTracker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDiaryIntoDatabase();

            }
        });

        openInhalerWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openInhalerDialog();
            }
        });
        // Inflate the layout for this fragment
        return rootView;

        }

        @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_exit_diary, menu);  // Use filter.xml from step 1
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_exit_diary) {

            new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Are you sure?")
                    .setContentText("Won't be able to save your diary!")
                    .setCancelText("No, cancel it!")
                    .setConfirmText("Yes, no save!")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            //Go back to calendar fragment
                            CalendarFragment fragment = new CalendarFragment();
                            FragmentTransaction fragmentTransaction =
                                    getFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.fragment_containerStart, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();

                            sDialog.dismissWithAnimation();
                        }
                    })
                    .showCancelButton(true)
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.cancel();
                        }
                    })
                    .show();

        }
        return true;

    }


    public void saveDiaryIntoDatabase() {
        String tight = Integer.toString(tightChestGroup.indexOfChild(rootView.findViewById(tightChestGroup.getCheckedRadioButtonId())));
        String wheezing = Integer.toString(wheezingGroup.indexOfChild(rootView.findViewById(wheezingGroup.getCheckedRadioButtonId())));
        String tiredness = Integer.toString(tirednessGroup.indexOfChild(rootView.findViewById(tirednessGroup.getCheckedRadioButtonId())));
        String feelingStressed = Integer.toString(feelingStressedGroup.indexOfChild(rootView.findViewById(feelingStressedGroup.getCheckedRadioButtonId())));

        Log.v("tightDisplayed", tight);
        Log.v("wheezingDisplayed", wheezing);
        Log.v("tiredDisplayed", tiredness);
        Log.v("feelingDisplayed", feelingStressed);

        if (!tight.equals("-1") && !wheezing.equals("-1") && !tiredness.equals("-1") && !feelingStressed.equals("-1") && selectedNo != null) {

            if (myDb.currentDayDiaryExist(passDate) == 0) {
                if (myDb.insertDataToUser(passDate, tight, wheezing, tiredness, selectedNo, feelingStressed)) {

                    //Show successful message to tell user
                    new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Successful!")
                            .setContentText("Your diary has been tracked!")
                            .show();

                }
            }else{
                if (myDb.updateCurrentDayRecord(passDate, tight, wheezing, tiredness, selectedNo, feelingStressed)){
                    //Show successful message to tell user
                    new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Successful!")
                            .setContentText("Your diary has been tracked!")
                            .show();
                }
            }

            //Go back to calendar fragment
            CalendarFragment fragment = new CalendarFragment();
            FragmentTransaction fragmentTransaction =
                    getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_containerStart, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }else {

            //Show msg to tell user to input everything record
            new SweetAlertDialog(getContext())
                    .setTitleText("Please answer all questions!")
                    .show();
        }

    }


    public void openInhalerDialog(){
        //When the user selected a date and click the add button on the title bar, user will be allowed to input diary.
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("             TOTAL INHALER");

                // I'm using fragment here so I'm using getView() to provide ViewGroup
                // but you can provide here any other instance of ViewGroup from your Fragment / Activity
                final View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.text_input_dialog, (ViewGroup) getView(), false);

                // Set up the input

//                NumberPicker np = (NumberPicker) viewInflated.findViewById(R.id.numberPicker);
//
//                //Set the minimum value of NumberPicker
//                np.setMinValue(0);
//                //Specify the maximum value/number of NumberPicker
//                np.setMaxValue(10);
//
//                //Gets whether the selector wheel wraps when reaching the min/max value.
//                np.setWrapSelectorWheel(true);
//
//                //Set a value change listener for NumberPicker
//                np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
//                    @Override
//                    public void onValueChange(android.widget.NumberPicker picker, int oldVal, int newVal) {
//                        //Display the newly selected number from picker
//
//                        selectedNo = String.valueOf(newVal);
//
//                    }
//                });

        com.shawnlin.numberpicker.NumberPicker numberPicker = (com.shawnlin.numberpicker.NumberPicker) viewInflated.findViewById(R.id.number_picker);

// set divider color
        numberPicker.setDividerColor(getResources().getColor(R.color.colorPrimary));
        numberPicker.setDividerColorResource(R.color.colorPrimary);

// set formatter
        numberPicker.setFormatter(getString(R.string.number_picker_formatter));
        numberPicker.setFormatter(R.string.number_picker_formatter);

// set text color
        numberPicker.setTextColor(getResources().getColor(R.color.colorPrimary));
        numberPicker.setTextColorResource(R.color.colorPrimary);

// set text size
        numberPicker.setTextSize(getResources().getDimension(R.dimen.text_size));
        numberPicker.setTextSize(R.dimen.text_size);

// set typeface
        numberPicker.setTypeface(Typeface.create(getString(R.string.roboto_light), Typeface.NORMAL));
        numberPicker.setTypeface(getString(R.string.roboto_light), Typeface.NORMAL);
        numberPicker.setTypeface(getString(R.string.roboto_light));
        numberPicker.setTypeface(R.string.roboto_light, Typeface.NORMAL);
        numberPicker.setTypeface(R.string.roboto_light);

        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                //Display the newly selected number from picker

                selectedNo = String.valueOf(newVal);
            }

        });
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                builder.setView(viewInflated);

                // Set up the buttons
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        totalInhalerText.setText(selectedNo);
                        dialog.dismiss();

                    }
                });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();

    }

    public void SetDiaryDetailsIfDiaryAlreadyExist(){
        if (records.getTight_chest() != null)
        {
            Log.v("DiaryTight", records.getTight_chest());
            ((RadioButton)tightChestGroup.getChildAt(Integer.valueOf(records.getTight_chest()))).setChecked(true);
            ((RadioButton)wheezingGroup.getChildAt(Integer.valueOf(records.getWheezing()))).setChecked(true);
            ((RadioButton)tirednessGroup.getChildAt(Integer.valueOf(records.getTiredness()))).setChecked(true);
            totalInhalerText.setText(records.getInhaler());
            selectedNo = records.getInhaler();
            ((RadioButton)feelingStressedGroup.getChildAt(Integer.valueOf(records.getFeeling_stressed()))).setChecked(true);

        }
    }

}
