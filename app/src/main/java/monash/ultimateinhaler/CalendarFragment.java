package monash.ultimateinhaler;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class CalendarFragment extends Fragment {
    private boolean undo = false;
    private CaldroidFragment caldroidFragment;
    private CaldroidFragment dialogCaldroidFragment;
    private View rootView;
    protected ArrayList<Date> selectedDates;
    private  NumberPicker np;
    private String m_Text = "";

    private void setCustomResourceForDates() {
        Calendar cal = Calendar.getInstance();

        // Min date is last 7 days
        cal.add(Calendar.DATE, -7);
        Date blueDate = cal.getTime();

        // Max date is next 7 days
        cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 0);
        Date greenDate = cal.getTime();

        if (caldroidFragment != null) {
            @SuppressWarnings("deprecation")
            ColorDrawable blue = new ColorDrawable(getResources().getColor(R.color.blue_grey_500));
            ColorDrawable green = new ColorDrawable(Color.LTGRAY);
            caldroidFragment.setBackgroundDrawableForDate(blue, blueDate);
            caldroidFragment.setBackgroundDrawableForDate(green, greenDate);
            caldroidFragment.setTextColorForDate(R.color.colorAccent, blueDate);
            caldroidFragment.setTextColorForDate(R.color.colorPrimary, greenDate);
        }
    }


    public CalendarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_calendar, container, false);

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");


        // Setup caldroid fragment
        // **** If you want normal CaldroidFragment, use below line ****
        caldroidFragment = new CaldroidFragment();

        // //////////////////////////////////////////////////////////////////////
        // **** This is to show customized fragment. If you want customized
        // version, uncomment below line ****
		// caldroidFragment = new CaldroidSampleCustomFragment();

        // Setup arguments

        // If Activity is created after rotation
        if (savedInstanceState != null) {
            caldroidFragment.restoreStatesFromKey(savedInstanceState,
                    "CALDROID_SAVED_STATE");
        }
        // If activity is created from fresh
        else {
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
//            args.putInt(CaldroidFragment.THEME_RESOURCE, com.caldroid.R.style.CaldroidDefaultDark);

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
                //Get the last week seven days date
                Calendar c = Calendar.getInstance();
                c.add(Calendar.DATE, -8);
                Date lastSevenDays = c.getTime();
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                Date now = new Date();
                if (date.before(now) && date.after(lastSevenDays)) {
                    selectedDates = new ArrayList<Date>();
                    Toast.makeText(getActivity().getApplicationContext(), formatter.format(date),
                            Toast.LENGTH_SHORT).show();
                    ColorDrawable green = new ColorDrawable(Color.LTGRAY);
                    caldroidFragment.setBackgroundDrawableForDate(green, date);
                    caldroidFragment.setTextColorForDate(R.color.colorAccent, date);
                    selectedDates.add(date);
                    caldroidFragment.refreshView();
                }else {
                    Toast.makeText(getActivity().getApplicationContext(), "You could only edit the last 7 days!",
                            Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onChangeMonth(int month, int year) {
                String text = "month: " + month + " year: " + year;
                Toast.makeText(getActivity().getApplicationContext(), text,
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClickDate(Date date, View view) {
                Toast.makeText(getActivity().getApplicationContext(),
                        "Long click cancel " + formatter.format(date),
                        Toast.LENGTH_SHORT).show();
                caldroidFragment.clearBackgroundDrawableForDate(date);
                caldroidFragment.clearTextColorForDate(date);
                caldroidFragment.refreshView();
                }




            @Override
            public void onCaldroidViewCreated() {
                if (caldroidFragment.getLeftArrowButton() != null) {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Caldroid view is created", Toast.LENGTH_SHORT)
                            .show();
                }
            }

        };

        // Setup Caldroid
        caldroidFragment.setCaldroidListener(listener);


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
        if(id == R.id.action_add_diary){
            //Do whatever you want to do
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Diary Entry");
            // I'm using fragment here so I'm using getView() to provide ViewGroup
            // but you can provide here any other instance of ViewGroup from your Fragment / Activity
            View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.text_input_dialog, (ViewGroup) getView(), false);
            // Set up the input

            RadioButton yesNewWithAsthma = (RadioButton) viewInflated.findViewById(R.id.yesButton_newWithAsthma);
            RadioButton noNewWithAsthma = (RadioButton) viewInflated.findViewById(R.id.noButton_newWithAsthma);
            RadioButton yesAttacked = (RadioButton) viewInflated.findViewById(R.id.yesButton_attackByAsthma);
            RadioButton noAttacked = (RadioButton) viewInflated.findViewById(R.id.noButton_attackByAsthma);
            final TextView timesAttacked = (TextView) viewInflated.findViewById(R.id.howManyTimesAttacked_textView);
            //final EditText input = (EditText) viewInflated.findViewById(R.id.input);
            np = (NumberPicker) viewInflated.findViewById(R.id.numberPicker);
            timesAttacked.setVisibility(View.GONE);
            np.setVisibility(View.GONE);
            //Set the minimum value of NumberPicker
            np.setMinValue(0);
            //Specify the maximum value/number of NumberPicker
            np.setMaxValue(10);

            //Gets whether the selector wheel wraps when reaching the min/max value.
            np.setWrapSelectorWheel(true);

            //Set a value change listener for NumberPicker
            np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(android.widget.NumberPicker picker, int oldVal, int newVal) {
                    //Display the newly selected number from picker

                }
            });

            yesAttacked.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    timesAttacked.setVisibility(View.VISIBLE);
                    np.setVisibility(View.VISIBLE);
                }
            });
            noAttacked.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    timesAttacked.setVisibility(View.GONE);
                    np.setVisibility(View.GONE);
                }
            });

            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            builder.setView(viewInflated);

            // Set up the buttons
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    //m_Text = input.getText().toString();
                }
            });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
