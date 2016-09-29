package com.cbpf.ultrasonicgraph;

import android.bluetooth.BluetoothSocket;
import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;

/**
 * <p>DataReceiver is a singleton class responsible for storing the received data and converting it to
 * a {@link Bitmap} image. The data is received as a line in the following format:</p>
 * <p>s x_value y_value z_value e\n</p>
 * <p>The {@link #parseLine(String)} translates the x and y values to the i and j position of the
 * {@link #heightMap} matrix. The z value is stored at the i,j position and it's converted to a pixel value
 * so it can be stored at {@link #bitmap}</p>
 * <p>Note that the conversion is made based on the the
 * {@link #MAX_Z} value, and the pixel is in grey scale (red, green and blue components are the same)</p>
 * @see #getColor(int)
 * @see #parseLine(String)
 */
public class DataReceiver {

    private static DataReceiver instance;

    private Bitmap bitmap;
    private int bitmap_width = Item.HEIGHT_MAP_WIDTH;
    private int bitmap_height = Item.HEIGHT_MAP_HEIGHT;

    /**
     * Max x and y values coming from the bluetooth connection. It's used to convert the x value
     * into a row and the y value into a column
     * number on the {@link #heightMap}
     */
    private int MAX_X = 1023;
    private int MAX_Y = 1023;

    /**
     * Max z value coming from the bluetooth connection. It's used to convert the z value into a
     * pixel color
     * @see #getColor(int)
     */
    private int [][] heightMap;

    /**Stores the incoming data*/
    private static double MAX_Z = 40000.0d;

    /**Callback to call when the bitmap has changed*/
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
        heightMap = new int[bitmap_width][bitmap_height];
        clearBitmap();
        fillWithRandomData();
    }

    public void startConnection(BluetoothSocket socket) {
        if (connection != null) {
            connection.cancel();
            try {
                connection.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            connection = null;
        }

        connection = new ConnectedThread(socket);
        connection.setOnDataReadListener(new OnDataReadListener() {

            @Override
            public void onDataRead(String data) {
                parseLine(data);
            }
        });
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

    /**
     * Parses the line and convert the incoming x, y values into a row and column and the z value
     * into a pixel. The z value is stored on the {@link #heightMap} and the converted z value (to pixel)
     * is stored on the {@link #bitmap}
     * @param line The line to be converted
     */
    private void parseLine(String line) {
        try {
            String[] values = line.replace("s", "").replace("e", "").replace("\n", "").split(" ");
            if (values.length == 3) {
                // Holds if the new height map value replaced an old value
                // (already obtained, different from the initial value)
                boolean isNew = false;
                int x = Integer.parseInt(values[0]) * bitmap_width / MAX_X;
                int y = Integer.parseInt(values[1]) * bitmap_height / MAX_Y;
                int z = Integer.parseInt(values[2]);

                if (heightMap[x][y] == -1) {
                    isNew = true;
                }
                heightMap[x][y] = z;

                int color = getColor(z);
                int old_color = bitmap.getPixel(x, y);
                if (old_color != color) {
                    bitmap.setPixel(x, y, color);
                    // Notify change
                    for (OnBitmapChangedListener listener : bitmap_changed_listeners) {
                        listener.onBitmapChanged(bitmap);
                        listener.onPixelChanged(x, y, old_color, color, isNew);
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
                // Fill height map with -1 so there can be a verification of new value when a height is added
                // There is a new value when the old value is equals to -1
                heightMap[i][j] = -1;
            }
        }
        for (OnBitmapChangedListener listener : bitmap_changed_listeners) {
            listener.onBitmapCleared(bitmap);
        }
    }

    public int[][] getHeightMap() {
        return heightMap;
    }

    public void addOnBitmapChangedListener(OnBitmapChangedListener listener) {
        bitmap_changed_listeners.add(listener);
    }

    /**
     * Get the color of a height map value z
     * @param z - the height
     * @return the color associated with the height z
     */
    public static int getColor(int z) {
        // Color format = ARGB
        int color = 0x000000FF;
        int color_channel = (int) (255 * (1 - (Math.abs(z) / MAX_Z))); //The "1 - " inverts the color

        //Red channel
        color = (color << 8) | color_channel;
        //Green channel
        color = (color << 8) | color_channel;
        //Blue channel
        color = (color << 8) | color_channel;

        return color;
    }

    /**
     * Method to fill the heightMap and the bitmap with random data. Used to debug only
     */
    public void fillWithRandomData() {
        for (int i = 0; i < heightMap.length; i++) {
            for (int j = 0; j < heightMap.length; j++) {
                heightMap[i][j] = (int) (Math.random() * MAX_Z);
                bitmap.setPixel(i, j, getColor(heightMap[i][j]));
            }
        }
    }
}
