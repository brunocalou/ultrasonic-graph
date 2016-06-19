package com.example.bruno.tabs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import java.util.ListIterator;

/**
 * Created by bruno on 17/05/16.
 */
class GraphView extends View {
    private Bitmap bitmap;
    private Bitmap filtered_bitmap;
    private Paint paint;
    private int graph_width = 50;
    private int graph_height = 50;
    private int pixels[];
    private DisplayMetrics display_metrics;
    private RectF bitmap_rect;
    private MatrixStub matrix_stub = new MatrixStub();
    private int matrix_boundary = 30;
    private FilterList filter_list;

    public FilterList getFilterList() {
        return filter_list;
    }

    private void init() {
        pixels = new int[graph_width];
        filter_list = new FilterList();


        paint = new Paint();
        display_metrics = new DisplayMetrics();
        bitmap = Bitmap.createBitmap(graph_width, graph_height, Bitmap.Config.ARGB_8888);
        filtered_bitmap = Bitmap.createBitmap(graph_width, graph_height, Bitmap.Config.ARGB_8888);

        for (int i = 0; i < graph_height; i += 1) {

            //Copy and convert pixels to gray scale
            for (int j = 0; j < graph_width; j += 1) {
                int color_channel = (int) (((matrix_stub.pixels[i][j] + matrix_boundary) / (2 * matrix_boundary)) * 255);
                //Color format = ARGB
                int color = 0x000000FF;
                //Red channel
                color = (color << 8) | color_channel;
                //Green channel
                color = (color << 8) | color_channel;
                //Blue channel
                color = (color << 8) | color_channel;

                pixels[j] = color;
            }

            bitmap.setPixels(pixels, 0, graph_width, 0, i, pixels.length, 1);
            filtered_bitmap.setPixels(pixels, 0, graph_width, 0, i, pixels.length, 1);
        }
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
}