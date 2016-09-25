package com.example.bruno.tabs;

import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import io.karim.MaterialTabs;

/**
 * Created by bruno on 22/08/16.
 */
public class HomeFragment extends Fragment implements SaveNewItemDialogFragment.SaveNewItemDialogListener {

    BluetoothAdapter mBluetoothAdapter;
    private final static int REQUEST_ENABLE_BT = 1;
    private final static int REQUEST_CONNECT_BT = 2;

    Menu options_menu;
    private MenuItemImpl connect_button;
    private MenuItemImpl clear_button;
    private MenuItemImpl disconnect_button;
    private MenuItemImpl save_button;
    private HomeImageFragment homeImageFragment;
    private HomeHistogramFragment homeHistogramFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initializes Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // Show a dialog if bluetooth is not supported
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
            new AlertDialog.Builder(getContext())
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

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize the ViewPager and set an adapter
        ViewPager pager = (ViewPager) view.findViewById(R.id.view_pager);
        HomePagerAdapter homePagerAdapter = new HomePagerAdapter(getChildFragmentManager(), getContext());
        pager.setAdapter(homePagerAdapter);
        homeImageFragment = homePagerAdapter.getHomeImageFragment();
        homeHistogramFragment = homePagerAdapter.getHomeHistogramFragment();

        Bitmap filteredBitmap = Bitmap.createBitmap(DataReceiver.getInstance().getBitmap());
        homeImageFragment.setFilteredBitmap(filteredBitmap);
        homeHistogramFragment.setBitmap(filteredBitmap);

        // Bind the tabs to the ViewPager
        MaterialTabs tabs = (MaterialTabs) view.findViewById(R.id.tabs);
        tabs.setViewPager(pager);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CONNECT_BT) {
            if (resultCode == BluetoothDevicesActivity.CONNECTION_SUCCESS) {
                // The user has connected
                Log.d("CONNECT", "The user has connected");
                setConnectButton(true);

                disconnect_button.setEnabled(true);

                Toast toast = Toast.makeText(getContext(), "Connected to " + data.getStringExtra("device_name"), Toast.LENGTH_LONG);
                toast.show();
            } else if (resultCode == BluetoothDevicesActivity.CONNECTION_ERROR) {
                Log.d("CONNECT", "Failed to connect");
                Toast toast = Toast.makeText(getContext(), "Failed to connect to " + data.getStringExtra("device_name"), Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
//        setRetainInstance(true);
    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        options_menu = menu;
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_main, menu);
        ((Toolbar) getActivity().findViewById(R.id.toolbar)).setNavigationIcon(R.drawable.ic_menu_white_24dp);

        connect_button = (MenuItemImpl) menu.findItem(R.id.connectButton);
        connect_button.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent connectBtIntent = new Intent(getContext(), BluetoothDevicesActivity.class);
                startActivityForResult(connectBtIntent, REQUEST_CONNECT_BT);
                return true;
            }
        });

        clear_button = (MenuItemImpl) menu.findItem(R.id.clearButton);
        clear_button.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                DataReceiver.getInstance().clearBitmap();
                homeHistogramFragment.clear();
                return true;
            }
        });

        disconnect_button = (MenuItemImpl) menu.findItem(R.id.disconnectButton);
        disconnect_button.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                item.setEnabled(false);
                DataReceiver.getInstance().finishConnection();
                setConnectButton(false);
                Toast toast = Toast.makeText(getContext(), "You are now disconnected", Toast.LENGTH_LONG);
                toast.show();
                return true;
            }
        });

        save_button = (MenuItemImpl) menu.findItem(R.id.saveButton);
        save_button.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //Open a dialog asking the name of the item to ave
                showSaveDialog();
                return true;
            }
        });

        if (!DataReceiver.getInstance().isConnected()) {
                disconnect_button.setEnabled(false);
                setConnectButton(false);
        } else {
            setConnectButton(true);
        }
    }

    private void setConnectButton(Boolean connected) {
        if (connected) {
            connect_button.setIcon(R.drawable.ic_toggle_switch_white_24dp);
        } else {
            connect_button.setIcon(R.drawable.ic_toggle_switch_off_white_24dp);
        }
    }

    private void showSaveDialog() {
        SaveNewItemDialogFragment dialog = new SaveNewItemDialogFragment();
        dialog.setTargetFragment(HomeFragment.this, 300);
        dialog.show(getActivity().getSupportFragmentManager(), "saveNewItemDialog");
    }

    @Override
    public void onFinishSaveNewItemDialog(String name) {
        homeImageFragment.saveItem(name);
    }
}
