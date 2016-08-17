package com.example.bruno.tabs;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * Created by bruno on 16/08/16.
 */
public class DataReceiver {

    private static DataReceiver instance;

    ConnectedThread connection;

    public static DataReceiver getInstance() {
        if (instance == null) {
            instance = new DataReceiver();
        }
        return instance;
    }

    private DataReceiver() {
    }

    public void startConnection(BluetoothSocket socket) {
        if (connection == null) {
            connection = new ConnectedThread(socket);
            connection.setOnDataReadListener(new OnDataReadListener() {

                @Override
                public void onDataRead(String data) {
                    Log.d("DATA", data);
                }
            });
        }
        connection.start();
    }

    public void finishConnection() {
        //TODO: IOException is occurring when calling this method. Must solve it
        connection.cancel();
        connection = null;
    }

}
