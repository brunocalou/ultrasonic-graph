package com.example.bruno.tabs;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by bruno on 16/08/16.
 * See https://developer.android.com/guide/topics/connectivity/bluetooth.html
 */
public class ConnectedThread extends Thread {
    private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;
    private OnDataReadListener onDataReadListener;

    public ConnectedThread(BluetoothSocket socket) {
        mmSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        // Get the input and output streams, using temp objects because
        // member streams are final
        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) {
            Log.d("CONNECTEDTHREAD", e.toString());
        }

        mmInStream = tmpIn;
        mmOutStream = tmpOut;
    }

    public void run() {
        byte[] buffer = new byte[4096];  // buffer store for the stream
        int bytes; // bytes returned from read()
        int end = 0;
        int end_of_line = (int) '\r';
        String line = "";
        // Keep listening to the InputStream until an exception occurs
        while (true) {
            try {
                if (mmInStream.available() > 10) {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer, end, 32);
                    end += bytes;

                    for (int i = end - bytes; i < end; i++) {
                        if (buffer[i] == end_of_line) {
                            int next_idx = i + 1;
                            line = new String(buffer, 0, i, "ASCII");
                            onDataReadListener.onDataRead(line);

                            //Shift the buffer
                            for (int j = next_idx; j < end; j++) {
                                buffer[j - next_idx] = buffer[j];
                            }
                            end -= next_idx;
                            break;
                        }
                    }
                    // Send the obtained bytes to the UI activity
//                mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
//                        .sendToTarget();
                }
            } catch (IOException e) {
                Log.d("CONNECTEDTHREAD", e.toString());
                break;
            }
        }
    }

    /* Call this from the main activity to send data to the remote device */
    public void write(byte[] bytes) {
        try {
            mmOutStream.write(bytes);
        } catch (IOException e) {
            Log.d("CONNECTEDTHREAD", e.toString());
        }
    }

    /* Call this from the main activity to shutdown the connection */
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
            Log.d("CONNECTEDTHREAD", e.toString());
        }
    }

    public void setOnDataReadListener(OnDataReadListener listener) {
        onDataReadListener = listener;
    }
}