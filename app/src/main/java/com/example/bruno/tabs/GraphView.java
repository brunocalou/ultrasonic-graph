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

/**
 * Created by bruno on 17/05/16.
 */
class GraphView extends View {
    private Bitmap bitmap;
    private Paint paint;
    private int graph_width = 50;
    private int graph_height = 50;
    private int pixels[];
    private DisplayMetrics display_metrics;
    private RectF bitmap_rect;
    private MatrixStub matrix_stub = new MatrixStub();
    private int matrix_boundary = 30;

    private void init() {
        pixels = new int[graph_width];

        //pixel line
//        for (int i = 0; i < graph_width; i += 1) {
//            pixels[i] = 0xFF0000FF;
//        }
        paint = new Paint();
        display_metrics = new DisplayMetrics();
        bitmap = Bitmap.createBitmap(graph_width, graph_height,  Bitmap.Config.ARGB_8888);

//        bitmap.setPixels(pixels, 0, graph_width, 0, 0, pixels.length, 1);
//        bitmap.setPixels(pixels, 0, graph_width, 0, graph_height-1, pixels.length, 1);
        //set all the pixels on the bitmap
//        for (int i = 0; i < graph_width; i += 1) {
//            pixels[i] = 0xFFFFFFFF;
//        }
//        pixels[0] = 0xFF0000FF;
//        pixels[pixels.length-1] = 0xFF0000FF;
        for (int i = 0; i < graph_height; i+=1) {

            //Copy and convert pixels to grayscale
            for (int j = 0; j < graph_width; j+=1) {
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
        }
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
            bitmap_rect = new RectF(canvas.getWidth()/6, y_border, canvas.getWidth()*5/6, y_border + canvas.getWidth()*2/3);
        }
        paint.setColor(Color.WHITE);
        canvas.drawPaint(paint);

        paint.setColor(Color.BLACK);
//
//        for (int i = 0; i < canvas.getHeight(); i += 10) {
//            canvas.drawLine(i, 0, i, canvas.getHeight(), paint);
//            canvas.drawLine(0, i, canvas.getWidth(), i, paint);
//        }

        canvas.drawBitmap(bitmap, null, bitmap_rect, paint);

    }
}