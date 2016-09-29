package com.example.bruno.tabs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Custom {@link View} to show a bitmap image and apply filters to it. Used to show the incoming data
 * @see DataReceiver
 */
class GraphView extends View {
    private Bitmap bitmap;
    private Bitmap filtered_bitmap;
    private Paint paint;
    private RectF bitmap_rect;
    private FilterList filter_list;

    public FilterList getFilterList() {
        return filter_list;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        filtered_bitmap = Bitmap.createBitmap(bitmap);
    }

    public Bitmap getFilteredBitmap() {
        return filtered_bitmap;
    }

    private void init() {
        filter_list = new FilterList();
        paint = new Paint();
        paint.setColor(Color.BLACK);
        bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        filtered_bitmap = Bitmap.createBitmap(bitmap);

        applyFilters();
    }

    public void applyFilters() {
        // Copy the original bitmap
        for (int i = 0; i < bitmap.getWidth(); i++) {
            for (int j = 0; j < bitmap.getHeight(); j++) {
                filtered_bitmap.setPixel(i, j, bitmap.getPixel(i, j));
            }
        }
        //Apply the filters
        filter_list.apply(filtered_bitmap);

        invalidate();
    }

    public GraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GraphView(Context context) {
        super(context);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (bitmap_rect == null) {
            int left = 0;
            int right = canvas.getWidth();
            int bottom = canvas.getHeight();
            int top = 0;
            // Make it a centralized square
            if (right > bottom) {
                right = bottom;
            } else {
                bottom = right;
            }

            //Centralize
            int dx = (canvas.getWidth() - right) / 2;
            int dy = (canvas.getHeight() - bottom) / 2;
            left += dx;
            right += dx;
            bottom += dy;
            top += dy;
            bitmap_rect = new RectF(left, top, right, bottom);
        }
        canvas.drawBitmap(filtered_bitmap, null, bitmap_rect, paint);
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setFilteredBitmap(Bitmap filteredBitmap) {
        this.filtered_bitmap = filteredBitmap;
    }
}