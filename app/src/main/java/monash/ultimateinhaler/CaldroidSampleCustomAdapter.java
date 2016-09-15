package monash.ultimateinhaler;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import hirondelle.date4j.DateTime;

/**
 * Created by jewel on 8/25/16.
 */
public class CaldroidSampleCustomAdapter extends CaldroidGridAdapter {

    public CaldroidSampleCustomAdapter(Context context, int month, int year,
                                       Map<String, Object> caldroidData,
                                       Map<String, Object> extraData) {
        super(context, month, year, caldroidData, extraData);
    }

    @SuppressWarnings("deprecation")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get your data here
        Drawable drawableRed = (Drawable) extraData.get("DrawableRed");
        ArrayList<Date> dateFromDatabase = (ArrayList<Date>) extraData.get("dateFromDatabase");
        LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View cellView = convertView;

        // For reuse
        if (convertView == null) {
            cellView = inflater.inflate(R.layout.custom_cell, null);
        }

        int topPadding = cellView.getPaddingTop();
        int leftPadding = cellView.getPaddingLeft();
        int bottomPadding = cellView.getPaddingBottom();
        int rightPadding = cellView.getPaddingRight();

        TextView tv1 = (TextView) cellView.findViewById(R.id.tv1);
        ImageView imageView = (ImageView) cellView.findViewById(R.id.imageEvent);
        //imageView.setImageDrawable(drawableWhite);

        tv1.setTextColor(Color.BLACK);

        // Get dateTime of this cell
        DateTime dateTime = this.datetimeList.get(position);
        Resources resources = context.getResources();

        //Get today date
        Date date= Calendar.getInstance().getTime();


        // Set color of the dates in previous / next month
        if (dateTime.getMonth() != month) {
            //noinspection deprecation
            tv1.setTextColor(resources
                    .getColor(com.caldroid.R.color.caldroid_555));
        }

        boolean shouldResetDiabledView = false;
        boolean shouldResetSelectedView = false;

        // Customize for disabled dates and date outside min/max dates
        if ((minDateTime != null && dateTime.lt(minDateTime))
                || (maxDateTime != null && dateTime.gt(maxDateTime))
                || (disableDates != null && disableDates.indexOf(dateTime) != -1)) {

            tv1.setTextColor(CaldroidFragment.disabledTextColor);
            if (CaldroidFragment.disabledBackgroundDrawable == -1) {
                cellView.setBackgroundResource(com.caldroid.R.drawable.disable_cell);
            } else {
                cellView.setBackgroundResource(CaldroidFragment.disabledBackgroundDrawable);
            }

            if (dateTime.equals(getToday())) {
                cellView.setBackgroundResource(com.caldroid.R.drawable.red_border_gray_bg);
            }

        } else {
            shouldResetDiabledView = true;
        }

        // Customize for selected dates
        if (selectedDates != null && selectedDates.indexOf(dateTime) != -1) {
            int year = selectedDates.get(0).getYear();
            int datetimeMonth =  selectedDates.get(0).getMonth();
            int day =  selectedDates.get(0).getDay();
            Calendar cal = Calendar.getInstance();
            cal.clear();

            // datetimeMonth start at 1. Need to minus 1 to get javaMonth
            cal.set(year, datetimeMonth - 1, day);
            if (cal.getTime().before(date)) {
                cellView.setBackgroundColor(resources
                        .getColor(R.color.btnRequest));
                //make visible to program
                tv1.setTextColor(Color.BLACK);
            }

        } else {
            shouldResetSelectedView = true;
        }

        if (shouldResetDiabledView && shouldResetSelectedView) {
            // Customize for today
            if (dateTime.equals(getToday())) {
                cellView.setBackgroundResource(com.caldroid.R.drawable.red_border);
            } else {
                cellView.setBackgroundResource(com.caldroid.R.drawable.cell_bg);
            }
        }

        if (dateFromDatabase != null){
            for (int i = 0 ; i < dateFromDatabase.size(); i++){
                int year = dateTime.getYear();
                int datetimeMonth = dateTime.getMonth();
                int day = dateTime.getDay();

                Calendar calendar = Calendar.getInstance();
                calendar.clear();
                // datetimeMonth start at 1. Need to minus 1 to get javaMonth
                calendar.set(year, datetimeMonth - 1, day);

                //If the date in calendar exits, use a custom layout
                if (dateFromDatabase.get(i).equals(calendar.getTime())) {
                    imageView.setImageDrawable(drawableRed);
                }

                else{
                    //make visible to program
                    cellView.setBackgroundResource(com.caldroid.R.drawable.cell_bg);

                }
            }
            if (dateTime.equals(getToday())) {
                cellView.setBackgroundResource(com.caldroid.R.drawable.red_border);
            } else {
                cellView.setBackgroundResource(com.caldroid.R.drawable.cell_bg);
            }
            if (selectedDates != null && selectedDates.indexOf(dateTime) != -1) {
                int year = selectedDates.get(0).getYear();
                int datetimeMonth =  selectedDates.get(0).getMonth();
                int day =  selectedDates.get(0).getDay();
                Calendar cal = Calendar.getInstance();
                cal.clear();

                // datetimeMonth start at 1. Need to minus 1 to get javaMonth
                cal.set(year, datetimeMonth - 1, day);
                if (cal.getTime().before(date)) {
                    cellView.setBackgroundColor(resources.getColor(R.color.btnRequest));
                    tv1.setTextColor(Color.BLACK);
                }

            } else {
                shouldResetSelectedView = true;
            }
        }

        tv1.setText("" + dateTime.getDay());
        // Somehow after setBackgroundResource, the padding collapse.
        // This is to recover the padding
        cellView.setPadding(leftPadding, topPadding, rightPadding,
                bottomPadding);

        // Set custom color if required
        setCustomResources(dateTime, cellView, tv1);

        return cellView;
    }




}