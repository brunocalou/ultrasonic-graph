package com.cbpf.ultrasonicgraph;

import android.graphics.Bitmap;

/**
 * Abstract filter class. It defines the methods that a filter should override
 */
public abstract class Filter {
    /**
     * Apply a filter to the whole image
     * @param image The image to apply the filter
     */
    public abstract void apply(Bitmap image);

    /**
     * Apply a filter to a single pixel
     * @param image The image to apply the filter
     * @param x The x position of the pixel
     * @param y The y position of the pixel
     */
    public abstract void apply(Bitmap image, int x, int y);
}
