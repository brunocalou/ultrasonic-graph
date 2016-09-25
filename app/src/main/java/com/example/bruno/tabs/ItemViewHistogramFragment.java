package com.example.bruno.tabs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * Created by bruno on 25/09/2016.
 */
public class ItemViewHistogramFragment extends HomeHistogramFragment {
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        reDrawHistogram();
    }
}
