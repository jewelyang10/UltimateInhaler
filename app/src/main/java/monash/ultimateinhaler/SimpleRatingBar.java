package monash.ultimateinhaler;

/**
 * Created by jewel on 9/14/16.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Optional XML attributes:
 * -SRB_hideRatingNumber: boolean. When set to true, hides the rating number value. Default is false.
 * -SRB_size: dimension. Size of rating number text and icons.
 * -SRB_maxRating: int. Maximum possible rating. Default is DEFAULT_MAX_RATING.
 * -SRB_filledIcon: String. Single character to use for filled icon. Default is filled star. Refer to rating_icons.xml
 * -SRB_unfilledIcon: String. Single character to use for unfilled icon. Default is same as filledIcon.
 * -SRB_textColor: color. Color of rating number text. Default is DEFAULT_FILLED_COLOR_ID.
 * -SRB_filledIconColor: color. Color of filled rating icon. Default is DEFAULT_FILLED_COLOR_ID.
 * -SRB_unfilledIconColor: color. Color of unfilled rating icon. Default is DEFAULT_UNFILLED_COLOR_ID.
 */
public class SimpleRatingBar extends LinearLayout {

    static final int DEFAULT_MAX_RATING = 5;
    static final int DEFAULT_FILLED_COLOR_ID = android.R.color.holo_red_light;
    static final int DEFAULT_UNFILLED_COLOR_ID = android.R.color.darker_gray;

    private String[] filledIcons;
    private String[] unfilledIcons;

    private int maxRating;
    private boolean hideRatingNumber;
//    private TextView ratingNumber;
    private TextView filledRating;
    private TextView unfilledRating;

    public SimpleRatingBar(Context context) {
        super(context);
        init(context, null);
    }

    public SimpleRatingBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SimpleRatingBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    /**
     * Sets the rating.
     * If rating is less than 0 or greater than maxRating, the number will be displayed as is.
     * However, the icons themselves will be between 0 ~ maxRating icons.
     * @param rating rating.
     */
    public void setRating(int rating) {
        if (!hideRatingNumber) {
//            ratingNumber.setText(String.valueOf(rating));
        }
        setIcons(rating);
    }

    /**
     * Sets the rating.
     * If rating is less than 0 or greater than maxRating, the number will be displayed as is.
     * However, the icons themselves will be between 0 ~ maxRating icons.
     * The icons will not show half-icons, but will round up/down to the closest integer to show the icons.
     * @param rating rating.
     */
    public void setRating(double rating) {
        if (!hideRatingNumber) {
//            ratingNumber.setText(String.valueOf(rating));
        }
        int roundedRating = (int) Math.round(rating);
        setIcons(roundedRating);
    }

    private void setIcons(int rating) {
        if (rating > maxRating) {
            rating = maxRating;
        } else if (rating < 0) {
            rating = 0;
        }

        if (rating == 0) {
            filledRating.setText("");
            unfilledRating.setText(unfilledIcons[maxRating - 1]);
        } else if (rating == maxRating) {
            filledRating.setText(filledIcons[maxRating - 1]);
            unfilledRating.setText("");
        } else {
            filledRating.setText(filledIcons[rating - 1]);
            unfilledRating.setText(unfilledIcons[maxRating - rating - 1]);
        }
    }

    private void init(Context context, AttributeSet attributeSet) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.simple_rating_bar, this);
//        ratingNumber = (TextView) findViewById(R.id.ratingNumber);
        filledRating = (TextView) findViewById(R.id.filledRating);
        unfilledRating = (TextView) findViewById(R.id.unfilledRating);

        SimpleRatingBarAttributes attributes = new SimpleRatingBarAttributes(context, attributeSet);
        readAttributes(attributes);
    }

    private void readAttributes(SimpleRatingBarAttributes attributes) {
        hideRatingNumber = attributes.getHideRatingNumber();
        maxRating = attributes.getMaxRating();
        if (maxRating <= 0) {
            maxRating = DEFAULT_MAX_RATING;
        }

        filledIcons = new String[maxRating];
        String filledIcon = attributes.getFilledIcon();
        if (filledIcon == null || filledIcon.length() != 1) {
            filledIcon = getContext().getString(R.string.ratingIconHeartFilled);
        }
        setIcons(filledIcons, filledIcon);

        String unfilledIcon = attributes.getUnfilledIcon();
        unfilledIcons = new String[maxRating];
        if (unfilledIcon == null || unfilledIcon.length() != 1 ) {
            unfilledIcon = getContext().getString(R.string.ratingIconHeartOutline);
        }
        setIcons(unfilledIcons, unfilledIcon);


//        if (unfilledIcon == null || unfilledIcon.length() != 1 || filledIcon.equals(unfilledIcon)) {
//            unfilledIcons = filledIcons;
//        } else {
//            unfilledIcons = new String[maxRating];
//            setIcons(unfilledIcons, unfilledIcon);
//        }

        if (attributes.getSize() > 0) {
//            ratingNumber.setTextSize(TypedValue.COMPLEX_UNIT_PX, attributes.getSize());
            filledRating.setTextSize(TypedValue.COMPLEX_UNIT_PX, attributes.getSize());
            unfilledRating.setTextSize(TypedValue.COMPLEX_UNIT_PX, attributes.getSize());
        }

//        ratingNumber.setTextColor(attributes.getTextColor());
        filledRating.setTextColor(attributes.getFilledIconColor());
        unfilledRating.setTextColor(attributes.getUnfilledIconColor());

//        if (hideRatingNumber) {
//            ratingNumber.setVisibility(View.GONE);
//        } else {
//            ratingNumber.setVisibility(View.VISIBLE);
//        }
    }

    private void setIcons(String[] icons, String icon) {
        StringBuilder iconBuilder = new StringBuilder();
        for (int i = 0; i < icons.length; i++) {
            iconBuilder.append(icon);
            icons[i] = iconBuilder.toString();
        }
    }

    // exposed for testing
    static class SimpleRatingBarTester {

        private final SimpleRatingBar simpleRatingBar;

        SimpleRatingBarTester(SimpleRatingBar simpleRatingBar) {
            this.simpleRatingBar = simpleRatingBar;
        }

        void readAttributes(SimpleRatingBarAttributes attributes) {
            simpleRatingBar.readAttributes(attributes);
        }

//        CharSequence getRatingTextDisplayed() {
//            return simpleRatingBar.ratingNumber.getText();
//        }

        CharSequence getFilledRatingDisplayed() {
            return simpleRatingBar.filledRating.getText();
        }

        CharSequence getUnfilledRatingDisplayed() {
            return simpleRatingBar.unfilledRating.getText();
        }

//        float getRatingNumberTextSize() {
//            return simpleRatingBar.ratingNumber.getTextSize();
//        }

        float getFilledIconTextSize() {
            return simpleRatingBar.filledRating.getTextSize();
        }

        float getUnfilledIconTextSize() {
            return simpleRatingBar.unfilledRating.getTextSize();
        }
//
//        int getRatingNumberVisibility() {
//            return simpleRatingBar.ratingNumber.getVisibility();
//        }
//
//        int getRatingColor() {
//            return simpleRatingBar.ratingNumber.getCurrentTextColor();
//        }

        int getFilledIconColor() {
            return simpleRatingBar.filledRating.getCurrentTextColor();
        }

        int getUnfilledIconColor() {
            return simpleRatingBar.unfilledRating.getCurrentTextColor();
        }
    }
}