package com.example.bruno.tabs;

import android.graphics.Bitmap;

/**
 * Created by bruno on 19/06/16.
 */
public abstract class Filter {
    public abstract void apply(Bitmap image);
    public abstract void apply(Bitmap image, int x, int y);
}
