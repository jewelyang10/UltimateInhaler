package monash.ultimateinhaler;

/**
 * Created by jewel on 9/4/16.
 */

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class CardPagerAdapter extends PagerAdapter implements CardAdapter {

    private List<CardView> mViews;
    private List<String> mData;
    private float mBaseElevation;
    private Context context;

    public CardPagerAdapter(Context current){
        this.context = current;
        mData = new ArrayList<>();
        mViews = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            mData.add("12");
            mViews.add(null);
        }
    }
    public CardPagerAdapter() {


    }

    public float getBaseElevation() {
        return mBaseElevation;
    }

    @Override
    public CardView getCardViewAt(int position) {
        return mViews.get(position);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.adapter, container, false);
        container.addView(view);

        if (position == 0) {
            CardView cardView1 = (CardView) view.findViewById(R.id.cardView);
            ImageView imageView1 = (ImageView) view.findViewById(R.id.imageRecommen);
            TextView cardViewContent1 = (TextView) view.findViewById(R.id.cardview_content);
            Resources res = context.getResources();
            @SuppressWarnings("deprecation") Drawable drawableMask = res.getDrawable(R.drawable.escapemask);
            if (mBaseElevation == 0) {
                mBaseElevation = cardView1.getCardElevation();
            }

            cardView1.setMaxCardElevation(mBaseElevation * MAX_ELEVATION_FACTOR);
            cardViewContent1.setText("•\tConsider wearing a facemask in certain situations when allergy is severe and exposure to high amounts of pollen is unavoidable.\n");
            imageView1.setImageDrawable(drawableMask);

            mViews.set(0, cardView1);
        }else if (position == 1) {

            CardView cardView2 = (CardView) view.findViewById(R.id.cardView);
            ImageView imageView2 = (ImageView) view.findViewById(R.id.imageRecommen);
            TextView cardViewContent2 = (TextView) view.findViewById(R.id.cardview_content);
            Resources res2 = context.getResources();
            @SuppressWarnings("deprecation") Drawable drawableRain = res2.getDrawable(R.drawable.torrentialrain48);
            if (mBaseElevation == 0) {
                mBaseElevation = cardView2.getCardElevation();
            }

            cardView2.setMaxCardElevation(mBaseElevation * MAX_ELEVATION_FACTOR);
            cardViewContent2.setText("•\tStay indoors during and after thunderstorms.\n");
            imageView2.setImageDrawable(drawableRain);

            mViews.set(1, cardView2);
        }else if (position == 2){
            CardView cardView3 = (CardView) view.findViewById(R.id.cardView);
            ImageView imageView3 = (ImageView) view.findViewById(R.id.imageRecommen);
            TextView cardViewContent3 = (TextView) view.findViewById(R.id.cardview_content);
            Resources res3 = context.getResources();
            @SuppressWarnings("deprecation") Drawable drawableCar = res3.getDrawable(R.drawable.carpool50);
            if (mBaseElevation == 0) {
                mBaseElevation = cardView3.getCardElevation();
            }

            cardView3.setMaxCardElevation(mBaseElevation * MAX_ELEVATION_FACTOR);
            cardViewContent3.setText("•\tWhen travelling in the car, keep the windows shut and use recirculating air conditioning.\n");
            imageView3.setImageDrawable(drawableCar);

            mViews.set(1, cardView3);
        }else if (position == 3){
            CardView cardView4 = (CardView) view.findViewById(R.id.cardView);
            ImageView imageView4 = (ImageView) view.findViewById(R.id.imageRecommen);
            TextView cardViewContent4 = (TextView) view.findViewById(R.id.cardview_content);
            Resources res4 = context.getResources();
            @SuppressWarnings("deprecation") Drawable drawableRain = res4.getDrawable(R.drawable.carpetcleaning50);
            if (mBaseElevation == 0) {
                mBaseElevation = cardView4.getCardElevation();
            }

            cardView4.setMaxCardElevation(mBaseElevation * MAX_ELEVATION_FACTOR);
            cardViewContent4.setText("•\tDry your bed linen and clothes indoors during the pollen season.\n");
            imageView4.setImageDrawable(drawableRain);

            mViews.set(1, cardView4);
        }else if (position == 4){
            CardView cardView5 = (CardView) view.findViewById(R.id.cardView);
            ImageView imageView5 = (ImageView) view.findViewById(R.id.imageRecommen);
            TextView cardViewContent5 = (TextView) view.findViewById(R.id.cardview_content);
            Resources res5 = context.getResources();
            @SuppressWarnings("deprecation") Drawable drawableRain = res5.getDrawable(R.drawable.flower50);
            if (mBaseElevation == 0) {
                mBaseElevation = cardView5.getCardElevation();
            }

            cardView5.setMaxCardElevation(mBaseElevation * MAX_ELEVATION_FACTOR);
            cardViewContent5.setText("•\t The evening — between 4pm and 6pm — or morning can be the time of day with the greatest amount of pollen in the air. ");
            imageView5.setImageDrawable(drawableRain);

            mViews.set(1, cardView5);
        }



        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        mViews.set(position, null);
    }
}

