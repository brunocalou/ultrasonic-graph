package com.example.bruno.tabs;

import android.bluetooth.BluetoothSocket;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by bruno on 16/08/16.
 */
public class DataReceiver {

    private static DataReceiver instance;

    private Bitmap bitmap;
    private int bitmap_width = 16;
    private int bitmap_height = 16;
    private int MAX_X = 1023;
    private int MAX_Y = 1023;
    private double MAX_Z = 40000.0d;
    int [][] height_map;
    private ArrayList<OnBitmapChangedListener> bitmap_changed_listeners;

    ConnectedThread connection;

    public static DataReceiver getInstance() {
        if (instance == null) {
            instance = new DataReceiver();
        }
        return instance;
    }

    private DataReceiver() {
        bitmap_changed_listeners = new ArrayList<>();
        bitmap = Bitmap.createBitmap(bitmap_width, bitmap_height, Bitmap.Config.ARGB_8888);
        clearBitmap();
        height_map = new int[bitmap_width][bitmap_height];
    }

    public void startConnection(BluetoothSocket socket) {
        if (connection == null) {
            connection = new ConnectedThread(socket);
            connection.setOnDataReadListener(new OnDataReadListener() {

                @Override
                public void onDataRead(String data) {
//                    Log.d("DATA", data);
                    parseLine(data);
                }
            });
        }
        connection.start();
    }

    public Boolean isConnected() {
        if (connection != null) {
            return connection.isAlive();
        }
        return false;
    }

    public void finishConnection() {
        //TODO: IOException is occurring when calling this method. Must solve it
        if (connection != null) {
            connection.cancel();
            connection = null;
        }
    }

    private void parseLine(String line) {
        try {
            String[] values = line.replace("s", "").replace("e", "").replace("\n", "").split(" ");
            if (values.length == 3) {
            Log.d("LINE", "x = " + values[0] + " y = " + values[1] + " val = " + values[2]);
                int x = Integer.parseInt(values[0]) * bitmap_width / MAX_X;
                int y = Integer.parseInt(values[1]) * bitmap_height / MAX_Y;
                int z = Integer.parseInt(values[2]);
                height_map[x][y] = z;

                // Color format = ARGB
                int color = 0x000000FF;
                int color_channel = (int) (255 * (1 - (Math.abs(z) / MAX_Z))); //The "1 - " inverts the color
                Log.d("COLOR", "" + color_channel);

                //Red channel
                color = (color << 8) | color_channel;
                //Green channel
                color = (color << 8) | color_channel;
                //Blue channel
                color = (color << 8) | color_channel;

                int old_color = bitmap.getPixel(x, y);
                if (old_color != color) {
                    bitmap.setPixel(x, y, color);
                    // Notify change
                    for (OnBitmapChangedListener listener : bitmap_changed_listeners) {
                        listener.onBitmapChanged(bitmap);
                        listener.onPixelChanged(x, y, old_color, color);
                    }
                }
            }
        } catch (Exception e) {
            Log.d("DATARECEIVER", e.toString());
        }
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void clearBitmap() {
        for (int i = 0; i < bitmap.getWidth(); i++) {
            for (int j = 0; j < bitmap.getHeight(); j++) {
                bitmap.setPixel(i, j, 0xFFFFFFFF);
            }
        }
        for (OnBitmapChangedListener listener : bitmap_changed_listeners) {
            listener.onBitmapCleared(bitmap);
        }
    }

    public void addOnBitmapChangedListener(OnBitmapChangedListener listener) {
        bitmap_changed_listeners.add(listener);
    }
}
