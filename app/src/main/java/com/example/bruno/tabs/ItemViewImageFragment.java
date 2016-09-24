package com.example.bruno.tabs;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
public class ItemViewImageFragment extends HomeImageFragment {
    Item item;

    @Override
    public void setupDataReceiver() {
        // Create a new bitmap based on the DataReceiver's bitmap characteristics
        graph_view.setBitmap(Bitmap.createBitmap(data_receiver.getBitmap()));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (item != null) {
            Log.d(getClass().getSimpleName(), "onViewCreated - The item is not null");
            // Setup the bitmap according to the item
            Bitmap bitmap = graph_view.getBitmap();

            // Setup the bitmap
            for (int i = 0; i < item.heightMap.length; i++) {
                for (int j = 0; j < item.heightMap[i].length; j++) {
                    bitmap.setPixel(i, j, DataReceiver.getColor(item.heightMap[i][j]));
                }
            }
            // Setup the contrast
            setFilterValue(item.filtersConfiguration.contrast);
        }
    }

    public void setItem(Item item) {
        Log.d(getClass().getSimpleName(), "Set item" + item.name);
        this.item = item;
    }
}