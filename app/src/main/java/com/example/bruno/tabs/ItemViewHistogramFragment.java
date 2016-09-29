package com.example.bruno.tabs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * ItemViewHistogramFragment is similar to {@link HomeHistogramFragment}, but it draws the fragment
 * as soon as it's created, so the user doesn't have to click on the button
 */
public class ItemViewHistogramFragment extends HomeHistogramFragment {
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        reDrawHistogram();
    }
}
