package com.cbpf.ultrasonicgraph;

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

/**
 * BluetoothDevicesActivity is the activity responsible for showing the list of bluetooth devices.
 * The activity will try to connect to the device as soon as the user selects it. Since this
 * activity is started by other activity, it will return CONNECTION_SUCCESS or CONNECTION_ERROR
 * to the intent so the activity that started it can know if there is a connection
 * @see #CONNECTION_SUCCESS
 * @see #CONNECTION_ERROR
 */
public class BluetoothDevicesActivity extends Activity {
    public static int CONNECTION_SUCCESS = 2;
    public static int CONNECTION_ERROR = 3;

    ListView bt_devices_list_view;
    ArrayAdapter<String> bt_devices_adapter;
    ArrayList<String> bt_devices_names;
    ArrayList<BluetoothDevice> bt_devices;

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
