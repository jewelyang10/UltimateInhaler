package monash.ultimateinhaler;

/**
 * Created by jewel on 9/14/16.
 */
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;

class SimpleRatingBarAttributes {

    private float size;
    private boolean hideRatingNumber;
    private int maxRating;
    private int textColor;
    private int filledIconColor;
    private int unfilledIconColor;
    private String filledIcon;
    private String unfilledIcon;

    SimpleRatingBarAttributes(Context context, AttributeSet attrs) {
        @SuppressWarnings("deprecation") final int defaultFilledColor = context.getResources().getColor(SimpleRatingBar.DEFAULT_FILLED_COLOR_ID);
        @SuppressWarnings("deprecation") final int defaultUnfilledColor = context.getResources().getColor(SimpleRatingBar.DEFAULT_UNFILLED_COLOR_ID);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SimpleRatingBar, 0, 0);
        try {
            size = typedArray.getDimensionPixelSize(R.styleable.SimpleRatingBar_SRB_size, 0);
            hideRatingNumber = typedArray.getBoolean(R.styleable.SimpleRatingBar_SRB_hideRatingNumber, false);
            maxRating = typedArray.getInt(R.styleable.SimpleRatingBar_SRB_maxRating, SimpleRatingBar.DEFAULT_MAX_RATING);
            filledIcon = typedArray.getString(R.styleable.SimpleRatingBar_SRB_filledIcon);
            unfilledIcon = typedArray.getString(R.styleable.SimpleRatingBar_SRB_unfilledIcon);
            textColor = typedArray.getColor(R.styleable.SimpleRatingBar_SRB_textColor, Color.RED);
            filledIconColor = typedArray.getColor(R.styleable.SimpleRatingBar_SRB_filledIconColor, Color.RED);
            unfilledIconColor = typedArray.getColor(R.styleable.SimpleRatingBar_SRB_unfilledIconColor,
                    defaultUnfilledColor);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            typedArray.recycle();
        }
    }

    float getSize() {
        return size;
    }

    boolean getHideRatingNumber() {
        return hideRatingNumber;
    }

    int getMaxRating() {
        return maxRating;
    }

    int getTextColor() {
        return textColor;
    }

    int getFilledIconColor() {
        return filledIconColor;
    }

    int getUnfilledIconColor() {
        return unfilledIconColor;
    }

    String getFilledIcon() {
        return filledIcon;
    }

    String getUnfilledIcon() {
        return unfilledIcon;
    }
}