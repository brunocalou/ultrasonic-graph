package com.example.bruno.tabs;

import android.graphics.Bitmap;
import android.util.Log;

/**
 * Created by bruno on 19/06/16.
 * Check the contrast filter formulas on http://www.dfstudios.co.uk/articles/programming/image-programming-algorithms/image-processing-algorithms-part-5-contrast-adjustment/
 */
public class ContrastFilter extends Filter {
    private double factor = 0.0; // Contrast correction factor
    private int contrast = 0; // Contrast level (from -255 to 255)

    public void setContrast(int contrast) {
        if (contrast < -255) {
            contrast = -255;
        } else if (contrast > 255) {
            contrast = 255;
        }
        this.contrast = contrast;
        this.factor = 259 * (this.contrast + 255) / (double) (255 * (259 - this.contrast));
    }

    private int truncate (int color) {
        if (color < 0) {
            color = 0;
        } else if (color > 255) {
            color = 255;
        }
        return color;
    }

    private int apply(int color) {
        // Color is in ARGB format
        int new_color = 0x000000FF;
        int red = (color & 0x00FF0000) >> 16;
        int green = (color & 0x0000FF00) >> 8;
        int blue = (color & 0x000000FF);

        int new_red = truncate((int) (this.factor * (red - 128) + 128));
        int new_green = truncate((int) (this.factor * (green - 128) + 128));
        int new_blue = truncate((int) (this.factor * (blue - 128) + 128));

        new_color = (new_color << 8) | new_red;
        new_color = (new_color << 8) | new_green;
        new_color = (new_color << 8) | new_blue;

        return new_color;
    }

    @Override
    public void apply(Bitmap image) {
        super.apply(image);
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                image.setPixel(i, j, apply(image.getPixel(i, j)));
            }
        }
    }
}
