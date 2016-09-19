package monash.ultimateinhaler;

/**
 * Created by jewel on 9/16/16.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class TabsPagerAdapter extends FragmentStatePagerAdapter {
    private static int NUM_ITEMS = 2;
    private String tabTitles[] = new String[] { "General Tips", "Asthma News" };


    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                // Tips fragment activity
               return TipsFragment.newInstance();
            case 1:
                // News fragment activity
                AsthmaNewsFragment fragmenttab2 = new AsthmaNewsFragment();

                return fragmenttab2;
        }
        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return NUM_ITEMS;
    }
    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}