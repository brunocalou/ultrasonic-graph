package com.example.bruno.tabs;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import io.karim.MaterialTabs;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the ViewPager and set an adapter
        ViewPager pager = (ViewPager) findViewById(R.id.view_pager);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        // Bind the tabs to the ViewPager
        MaterialTabs tabs = (MaterialTabs) findViewById(R.id.tabs);
        tabs.setViewPager(pager);
    }
}
