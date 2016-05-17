package com.example.bruno.tabs;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by bruno on 17/05/16.
 */
class GraphView extends View {
    private Paint paint;

    public GraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.paint = new Paint();
    }

    public GraphView(Context context) {
        super(context);
        this.paint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(Color.WHITE);
        canvas.drawPaint(paint);

        paint.setColor(Color.BLACK);

        for (int i = 0; i < canvas.getHeight(); i += 10) {
            canvas.drawLine(i, 0, i, canvas.getHeight(), paint);
            canvas.drawLine(0, i, canvas.getWidth(), i, paint);
        }
    }
}