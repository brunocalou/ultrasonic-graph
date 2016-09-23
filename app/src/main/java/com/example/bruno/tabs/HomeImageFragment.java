package com.example.bruno.tabs;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import java.util.Date;

/**
 * Created by bruno on 08/05/16.
 */
public class HomeImageFragment extends Fragment {
    private SeekBar seek_bar;
    private ContrastFilter contrast;
    private GraphView graph_view;
    private DataReceiver data_receiver;

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contrast = new ContrastFilter();
        data_receiver = DataReceiver.getInstance();
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_image, container, false);
        graph_view = (GraphView) view.findViewById(R.id.graphView);

        //Add filters
        final FilterList filter_list = graph_view.getFilterList();
        filter_list.add(contrast);

        seek_bar = (SeekBar) view.findViewById(R.id.seekBar);
        seek_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                contrast.setContrast(progress - seekBar.getMax() / 2);
                graph_view.applyFilters();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        graph_view.setBitmap(data_receiver.getBitmap());

        data_receiver.addOnBitmapChangedListener(new OnBitmapChangedListener() {

            Bitmap filtered_bitmap = graph_view.getFilteredBitmap();

            @Override
            public void onBitmapChanged(Bitmap bitmap) {
            }

            @Override
            public void onPixelChanged(int x, int y, int old_pixel, int new_pixel) {
                filtered_bitmap.setPixel(x, y, new_pixel);
                filter_list.apply(filtered_bitmap, x, y);
                graph_view.postInvalidate();
            }

            @Override
            public void onBitmapCleared(Bitmap bitmap) {
                graph_view.applyFilters();
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        setFilterValue(contrast.getContrast());
        graph_view.applyFilters();
    }

    private void setFilterValue(int value) {
        contrast.setContrast(value);
        seek_bar.setProgress(seek_bar.getMax() / 2 + value);
    }

    public void save(String name) {
        Log.d(getClass().getSimpleName(), "Saving " + name);
        Item item = new Item();
        item.name = name;
        item.creationDate = new Date();
        item.heightMap = DataReceiver.getInstance().getHeightMap();
        item.filtersConfiguration = new FiltersConfiguration();
        item.filtersConfiguration.contrast = contrast.getContrast();

        ItemsDatabaseHelper.getInstance(getContext()).addItem(item);

    }
}