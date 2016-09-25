package com.example.bruno.tabs;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by bruno on 24/09/2016.
 */
public class HomeHistogramFragment extends Fragment {
    protected DataReceiver dataReceiver = DataReceiver.getInstance();
    protected Histogram<Integer> histogram;
    Bitmap bitmap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_histogram, container, false);
        histogram = (Histogram<Integer>) view.findViewById(R.id.histogram);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.reloadButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reDrawHistogram();
            }
        });

        return view;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void clear() {
        histogram.clear();
        histogram.reDrawData();
    }

    public void reDrawHistogram() {
        histogram.clear();
        if (bitmap != null) {
            //Initialize the histogram with whatever is on the bitmap
            for (int i = 0; i < bitmap.getWidth(); i++) {
                for (int j = 0; j < bitmap.getHeight(); j++) {
                    histogram.increment(bitmap.getPixel(i, j));
                }
            }
            histogram.reDrawData();
        }
    }
}
