package com.cbpf.ultrasonicgraph;

import android.graphics.Bitmap;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 * FilterList is a helper class. It holds {@link Filter} objects and defines the apply methods for
 * convenience
 */
public class FilterList extends LinkedList<Filter> {

    /**
     * Apply all the filters to the image
     * @param image The image to apply the filters
     */
    public void apply(Bitmap image) {
        ListIterator<Filter> listIterator = this.listIterator();
        while (listIterator.hasNext()) {
            listIterator.next().apply(image);
        }
    }

    /**
     * Apply all the filters to a single pixel on the image
     * @param image The image to apply the filters
     * @param x The x position of the image
     * @param y The y position of the image
     */
    public void apply(Bitmap image, int x, int y) {
        ListIterator<Filter> listIterator = this.listIterator();
        while (listIterator.hasNext()) {
            listIterator.next().apply(image, x, y);
        }
    }
}
