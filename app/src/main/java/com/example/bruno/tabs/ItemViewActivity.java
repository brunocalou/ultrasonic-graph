package com.example.bruno.tabs;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import io.karim.MaterialTabs;

/**
 * Created by bruno on 23/09/2016.
 */
public class ItemViewActivity extends AppCompatActivity {
    public static String ITEM_ID = "item_id";
    private ItemsDatabaseHelper itemsDatabaseHelper;
    private Item item;

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

        ItemViewImageFragment itemViewImageFragment = itemViewPagerAdapter.getItemViewImageFragment();
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
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
