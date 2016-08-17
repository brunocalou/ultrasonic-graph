package com.example.bruno.tabs;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

/**
 * Created by bruno on 16/08/16.
 */
public class BluetoothDevicesActivity extends Activity {
    ListView bt_devices_list_view;
    ArrayAdapter<String> bt_devices_adapter;
    ArrayList<String> bt_devices_names;
    ArrayList<BluetoothDevice> bt_devices;
    public static String BLUETOOTH_DEVICE = "BLUETOOTH_DEVICE";
    public static int CONNECTION_SUCCESS = 2;
    public static int CONNECTION_ERROR = 3;
    BluetoothDevice device;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_devices);

        bt_devices = new ArrayList<>();
        bt_devices_names = new ArrayList<>();
        bt_devices_adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, bt_devices_names);
        bt_devices_list_view = (ListView) findViewById(R.id.bluetoothDevicesListView);
        bt_devices_list_view.setAdapter(bt_devices_adapter);

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter != null) {
            if (mBluetoothAdapter.isEnabled()) {
                Log.d("CONNECT", "Bluetooth is enabled");
                Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

                // If there are paired devices
                if (pairedDevices.size() > 0) {
                    // Loop through paired devices
                    for (BluetoothDevice device : pairedDevices) {
                        // Add the name and address to an array adapter to show in a ListView
                        bt_devices_adapter.add(device.getName() + "\n" + device.getAddress());
                        bt_devices.add(device);
                        Log.d("CONNECT", device.getName() + "\n" + device.getAddress());
                    }
                }
            }
        }

        bt_devices_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Connect to device and finish the activity
                device = bt_devices.get(position);
                bt_devices_list_view.getChildAt(position).setEnabled(false);

                ConnectThread connect;
                try {
                    connect = new ConnectThread(device) {
                        @Override
                        public void onConnected() {
                            Intent intent = new Intent();
                            intent.putExtra("device_name", device.getName());
                            setResult(CONNECTION_SUCCESS, intent);
                            finish();
                        }

                        @Override
                        public void onFailed() {
                            Intent intent = new Intent();
                            intent.putExtra("device_name", device.getName());
                            setResult(CONNECTION_ERROR, intent);
                            finish();
                        }
                    };
                    connect.start();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
