package com.cbpf.ultrasonicgraph;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

/**
 * ItemViewImageFragment is similar to {@link HomeImageFragment}, but it loads the bitmap and the
 * filters from an {@link Item}
 */
public class ItemViewImageFragment extends HomeImageFragment {
    Item item;

    @Override
    public void setupDataReceiver() {
        // Create a new bitmap based on the DataReceiver's bitmap characteristics
        graph_view.setBitmap(Bitmap.createBitmap(data_receiver.getBitmap()));
        if (filteredBitmap != null) {
            graph_view.setFilteredBitmap(filteredBitmap);
        }
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

    public void saveItem() {
        saveItem(item);
    }

    @Override
    protected void saveItem(Item item) {
        item.filtersConfiguration.contrast = contrast.getContrast();

        if (ItemsDatabaseHelper.getInstance(getContext()).updateFiltersConfiguration(item.filtersConfiguration) > 0) {
            Toast toast = Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            Toast toast = Toast.makeText(getContext(), "Failed to save", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void deleteItem() {
        if (ItemsDatabaseHelper.getInstance(getContext()).deleteItem(item) > 0) {
            Toast toast = Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            Toast toast = Toast.makeText(getContext(), "Failed to delete", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}