package com.example.bruno.tabs;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import java.util.Set;

import io.karim.MaterialTabs;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    private final static int REQUEST_ENABLE_BT = 1;
    private final static int REQUEST_CONNECT_BT = 2;
    BluetoothAdapter mBluetoothAdapter;
    Context context;
    Menu options_menu;
    private MenuItemImpl connect_button;
    private MenuItemImpl clear_button;
    private MenuItemImpl disconnect_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        // Initialize the ViewPager and set an adapter
        ViewPager pager = (ViewPager) findViewById(R.id.view_pager);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), this));

        // Bind the tabs to the ViewPager
        MaterialTabs tabs = (MaterialTabs) findViewById(R.id.tabs);
        tabs.setViewPager(pager);

        // Find the toolbar view inside the activity layout
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);

        // Initializes Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // Show a dialog if bluetooth is not supported
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
            new AlertDialog.Builder(this)
                    .setTitle("Not compatible")
                    .setMessage("Your phone does not support Bluetooth")
                    .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else if (!mBluetoothAdapter.isEnabled()) {
            //Enable bluetooth
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CONNECT_BT) {
            if (resultCode == BluetoothDevicesActivity.CONNECTION_SUCCESS) {
                // The user has connected
                Log.d("CONNECT", "The user has connected");
                connect_button.setIcon(R.drawable.ic_toggle_switch_white_24dp);

                disconnect_button.setEnabled(true);

                Toast toast = Toast.makeText(getApplicationContext(), "Connected to " + data.getStringExtra("device_name"), Toast.LENGTH_LONG);
                toast.show();
            } else if (resultCode == BluetoothDevicesActivity.CONNECTION_ERROR) {
                Log.d("CONNECT", "Failed to connect");
                Toast toast = Toast.makeText(getApplicationContext(), "Failed to connect to " + data.getStringExtra("device_name"), Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        options_menu = menu;
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);

        connect_button = (MenuItemImpl) menu.findItem(R.id.connectButton);
        connect_button.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent connectBtIntent = new Intent(context, BluetoothDevicesActivity.class);
                startActivityForResult(connectBtIntent, REQUEST_CONNECT_BT);
                return true;
            }
        });

        clear_button = (MenuItemImpl) menu.findItem(R.id.clearButton);
        clear_button.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                DataReceiver.getInstance().clearBitmap();
                return true;
            }
        });

        disconnect_button = (MenuItemImpl) menu.findItem(R.id.disconnectButton);
        disconnect_button.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                item.setEnabled(false);
                DataReceiver.getInstance().finishConnection();
                connect_button.setIcon(R.drawable.ic_toggle_switch_off_white_24dp);
                Toast toast = Toast.makeText(getApplicationContext(), "You are now disconnected", Toast.LENGTH_LONG);
                toast.show();
                return true;
            }
        });

        disconnect_button.setEnabled(false);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DataReceiver.getInstance().finishConnection();
    }
}
