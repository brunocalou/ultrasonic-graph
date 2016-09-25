package com.example.bruno.tabs;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by bruno on 08/05/16.
 */
public class HomePagerAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 2;
    private Context context;
    private HomeImageFragment homeImageFragment;
    private HomeHistogramFragment homeHistogramFragment;

    public HomePagerAdapter(FragmentManager fragmentManager, Context context) {
        super(fragmentManager);
        this.context = context;
        homeImageFragment = new HomeImageFragment();
        homeHistogramFragment = new HomeHistogramFragment();
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: // Image
                return homeImageFragment;
            case 1: // Histogram
                return homeHistogramFragment;
            default:
                return null;
        }
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0: // Image tab
                return context.getResources().getString(R.string.image_tab);
            case 1: // Histogram tab
                return context.getResources().getString(R.string.histogram_tab);
            default:
                return "Tab";
        }
    }

    public HomeImageFragment getHomeImageFragment() {
        return homeImageFragment;
    }

    public HomeHistogramFragment getHomeHistogramFragment() {
        return homeHistogramFragment;
    }
}
