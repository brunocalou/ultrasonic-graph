package com.example.bruno.tabs;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * ItemViewPagerAdapter is similar to {@link HomePagerAdapter}, but it uses
 * {@link ItemViewImageFragment} and {@link ItemViewHistogramFragment}
 */
public class ItemViewPagerAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 2;
    private Context context;
    private ItemViewImageFragment itemViewImageFragment;
    private ItemViewHistogramFragment itemViewHistogramFragment;

    public ItemViewPagerAdapter(FragmentManager fragmentManager, Context context) {
        super(fragmentManager);
        this.context = context;
        itemViewImageFragment = new ItemViewImageFragment();
        itemViewHistogramFragment = new ItemViewHistogramFragment();
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
                return itemViewImageFragment;
            case 1: // Histogram
                return itemViewHistogramFragment;
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

    public ItemViewImageFragment getItemViewImageFragment() {
        return itemViewImageFragment;
    }

    public ItemViewHistogramFragment getItemViewHistogramFragment() {
        return itemViewHistogramFragment;
    }
}
