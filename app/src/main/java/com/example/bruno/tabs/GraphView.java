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
 * Created by bruno on 17/05/16.
 */
class GraphView extends View {
    private Bitmap bitmap;
    private Bitmap filtered_bitmap;
    private Paint paint;
    private GraphView instance;
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
        instance = this;
        filter_list = new FilterList();
        paint = new Paint();
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
            int y_border = 100;
            bitmap_rect = new RectF(canvas.getWidth() / 6, y_border, canvas.getWidth() * 5 / 6, y_border + canvas.getWidth() * 2 / 3);
        }
        paint.setColor(Color.WHITE);
        canvas.drawPaint(paint);

        paint.setColor(Color.BLACK);

        canvas.drawBitmap(filtered_bitmap, null, bitmap_rect, paint);

    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}