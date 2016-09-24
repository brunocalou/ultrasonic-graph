package com.example.bruno.tabs;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import io.karim.MaterialTabs;

/**
 * Created by bruno on 23/09/2016.
 */
public class ItemViewActivity extends AppCompatActivity {
    public static String ITEM_ID = "item_id";
    private ItemsDatabaseHelper itemsDatabaseHelper;
    private Item item;
    private ItemViewImageFragment itemViewImageFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Get the item from the database
        Intent intent = getIntent();
        long item_id = intent.getLongExtra(ITEM_ID, -1);
        itemsDatabaseHelper = ItemsDatabaseHelper.getInstance(getApplicationContext());
        item = itemsDatabaseHelper.getItem(item_id);

        // Set the title of the action bar
        setTitle(item.name);

        // Set the back button on the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize the ViewPager and set an adapter
        ViewPager pager = (ViewPager) findViewById(R.id.view_pager);
        ItemViewPagerAdapter itemViewPagerAdapter = new ItemViewPagerAdapter(getSupportFragmentManager(), getApplicationContext());
        pager.setAdapter(itemViewPagerAdapter);

        itemViewImageFragment = itemViewPagerAdapter.getItemViewImageFragment();
        itemViewImageFragment.setItem(item);

        // Bind the tabs to the ViewPager
        MaterialTabs tabs = (MaterialTabs) findViewById(R.id.tabs);
        tabs.setViewPager(pager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_item_view, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.saveButton) {
            itemViewImageFragment.saveItem();
        } else if (id == R.id.deleteButton) {
            // Show a dialog confirming the delete action
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.delete_dialog_message)
                    .setTitle(R.string.delete_dialog_title)
                    .setPositiveButton(R.string.delete_dialog_positive, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            itemViewImageFragment.deleteItem();
                            finish();
                        }
                    })
                    .setNegativeButton(R.string.delete_dialog_negative, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();

        } else if (id == R.id.shareButton) {

        } else if (id == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }
}
