package com.example.bruno.tabs;

import android.graphics.Bitmap;

/**
 * Created by bruno on 17/08/16.
 */
public abstract class OnBitmapChangedListener {
    public abstract  void onBitmapChanged(Bitmap bitmap);
    public abstract void onPixelChanged(int x, int y, int old_pixel, int new_pixel);
}
