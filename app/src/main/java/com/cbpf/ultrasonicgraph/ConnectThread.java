package com.cbpf.ultrasonicgraph;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

/**
 * ConnectThread is a helper class to start a bluetooth communication as a client
 * See <a href="https://developer.android.com/guide/topics/connectivity/bluetooth.html">Bluetooth guide</a>
 */
public abstract class ConnectThread extends Thread {
    private final BluetoothSocket mmSocket;
    private final BluetoothDevice mmDevice;
    BluetoothAdapter mBluetoothAdapter;
    UUID uuid;
    ConnectedThread connection;

    public ConnectThread(BluetoothDevice device) throws IOException {
        // Use a temporary object that is later assigned to mmSocket,
        // because mmSocket is final
        BluetoothSocket tmp = null;
        mmDevice = device;

        if (device.getUuids() == null) {
            // Create a new uuid
            uuid = UUID.randomUUID();
        } else {
            uuid = device.getUuids()[0].getUuid();
        }

        // Get a BluetoothSocket to connect with the given BluetoothDevice
        try {
            // MY_UUID is the app's UUID string, also used by the server code
            tmp = device.createRfcommSocketToServiceRecord(uuid);
        } catch (IOException e) {
            Log.d("CONNECTTHREAD", e.toString());
        }
        mmSocket = tmp;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            throw new IOException("Could not get default bluetooth adapter");
        }
    }

    public void run() {
        // Cancel discovery because it will slow down the connection
        mBluetoothAdapter.cancelDiscovery();

        try {
            // Connect the device through the socket. This will block
            // until it succeeds or throws an exception
            mmSocket.connect();
        } catch (IOException connectException) {
            // Unable to connect; close the socket and get out
            Log.d("CONNECTTHREAD", connectException.toString());
            try {
                mmSocket.close();
            } catch (IOException closeException) {
                Log.d("CONNECTTHREAD", closeException.toString());
            }
            onFailed();
            return;
        }

        onConnected();
        // Do work to manage the connection (in a separate thread)
        DataReceiver.getInstance().startConnection(mmSocket);
    }

    /**
     * Will cancel an in-progress connection, and close the socket
     */
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
            Log.d("CONNECTTHREAD", e.toString());
        }
    }

    public abstract void onConnected();

    public abstract void onFailed();
}
