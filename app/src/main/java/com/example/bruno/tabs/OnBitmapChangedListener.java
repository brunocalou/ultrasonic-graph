package com.example.bruno.tabs;

import android.graphics.Bitmap;

/**
 * OnBitmapChangedListener defines methods to be overridden when a pixel has changed on a bitmap
 */
public abstract class OnBitmapChangedListener {
    public abstract  void onBitmapChanged(Bitmap bitmap);

    /**
     * Called when a pixel has changed
     * @param x The x position on the bitmap
     * @param y The y position on the bitmap
     * @param old_pixel The old pixel value before the change
     * @param new_pixel The new pixel value after the change
     * @param is_new Holds if the pixel is a new pixel. When the bitmap is created, initial values
     *               are assigned to it. So, on the first change, the pixel is considered as a new pixel
     *               and the is_new must be set to true. After that, every change on the pixel
     *               should set is_new to false
     */
    public abstract void onPixelChanged(int x, int y, int old_pixel, int new_pixel, boolean is_new);
    public abstract void onBitmapCleared(Bitmap bitmap);
}
