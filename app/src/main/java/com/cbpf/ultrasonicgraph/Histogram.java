package com.cbpf.ultrasonicgraph;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Custom {@link View} to plot a histogram
 */

public class Histogram<T> extends View {

    /**
     * <p>The histogramData is stored in a sorted map. Each key is a histogram data and the value of each
     * key is the number of times the key occurs</p>
     * <p>Note that a key can contain only positive values. Once a key hits 0, it's removed from the
     * map</p>
     * <p>e.g.</p>
     * <p>{ 0: 12 // the 0 occurred 12 times</p>
     * <p>  2: 9  // the 2 occurred 9 times</p>
     * <p>  5: 1} // the 5 occurred 1 time</p>
     */
    TreeMap<T, Integer> histogramData = new TreeMap<>();

    // Drawing related variables
    private Paint paint = new Paint();
    int barMargin = 1;

    public Histogram(Context context) {
        this(context, null);
    }

    public Histogram(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint.setColor(0xFF3F51B5); //Primary color
        paint.setStyle(Paint.Style.FILL);
    }

    /**
     * Increments the occurrence of the data #histogramData
     * @param data The data to be incremented
     */
    public void increment(T data) {
        Integer dataCounter = histogramData.get(data);
        if (dataCounter != null) {
            dataCounter += 1;
        } else {
            dataCounter = 1;
        }
        histogramData.put(data, dataCounter);
        invalidate();
    }

    /**
     * Decrements the occurrence of the data. Note that when it reaches 0, the data is removed from
     * the {@link #histogramData}
     * @param data The data to be decremented
     */
    public void decrement(T data) {
        Integer dataCounter = histogramData.get(data);
        if (dataCounter != null) {
            dataCounter -= 1;
        } else {
            dataCounter = 1;
        }

        // If the counter reaches zero, remove it from the histogramData
        if (dataCounter == 0) {
            histogramData.remove(data);
        } else {
            histogramData.put(data, dataCounter);
        }
        invalidate();
    }

    public void clear() {
        histogramData.clear();
        invalidate();
    }

    public void reDrawData() {
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (histogramData.size() > 0) {
            int barWidth = canvas.getWidth() / histogramData.size();
            float yScale = 1;
            int highestCounter = 0;
            int yBaseline = canvas.getHeight(); // Start drawing the rectangles from the yBaseLine
            int yTopLine = 0;

            Iterator it = histogramData.entrySet().iterator();

            // Get the highest data counter
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                Integer dataCounter = (Integer) entry.getValue();

                if (dataCounter > highestCounter) {
                    highestCounter = dataCounter;
                }
            }

            // Scale the bars height to fit the histogram
            yScale = (yBaseline - yTopLine) / (float) highestCounter;

            // Draw the histogram
            it = histogramData.entrySet().iterator();

            // Holds the left side of the rectangle on the iteration
            // The first value is supposed to centralize the histogram, because the width of the
            // canvas will not be divisible by the size of the histogram. The barWidth is integer,
            // so the histogram width will be less or equals to the canvas width
            int currentBarLeft = (canvas.getWidth() - barWidth * histogramData.size()) / 2;

            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                Integer dataCounter = (Integer) entry.getValue();

                canvas.drawRect(
                        currentBarLeft, //left
                        yBaseline - dataCounter * yScale, //top
                        currentBarLeft + barWidth - barMargin, //right
                        yBaseline, //Bottom
                        paint
                );

                currentBarLeft += barWidth;
            }

        }
    }
}
