package com.example.bruno.tabs;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import io.karim.MaterialTabs;

/**
 * ItemViewActivity is responsible for retrieving an item from the database and showing it to the user.
 * It's similar to the {@link HomeFragment}, but it does not use the {@link DataReceiver} to create
 * the {@link GraphView}, it loads the {@link Item} and shows the {@link GraphView} and the
 * {@link Histogram} accordingly
 */
public class ItemViewActivity extends AppCompatActivity {
    public static String ITEM_ID = "item_id";
    private ItemsDatabaseHelper itemsDatabaseHelper;
    private Item item;
    private ItemViewImageFragment itemViewImageFragment;
    private ItemViewHistogramFragment itemViewHistogramFragment;

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

        // Create a bitmap to hold the generated item pixels
        Bitmap bitmap = Bitmap.createBitmap(item.heightMap.length, item.heightMap[0].length, Bitmap.Config.ARGB_8888);
        itemViewImageFragment = itemViewPagerAdapter.getItemViewImageFragment();
        itemViewHistogramFragment = itemViewPagerAdapter.getItemViewHistogramFragment();
        itemViewImageFragment.setItem(item);

        // Fills the bitmap with the correct pixel values based on the item height map
        for (int i = 0; i < item.heightMap.length; i++) {
            for (int j = 0; j < item.heightMap[i].length; j++) {
                bitmap.setPixel(i, j, DataReceiver.getColor(item.heightMap[i][j]));
            }
        }

        // Set the bitmap om the itemViewImageFragment and the itemViewHistogramFragment. Note that
        // the bitmap will be SHARED on these instances
        itemViewImageFragment.setFilteredBitmap(bitmap);
        itemViewHistogramFragment.setBitmap(bitmap);

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
            // See http://stackoverflow.com/questions/9049143/android-share-intent-for-a-bitmap-is-it-possible-not-to-save-it-prior-sharing
            // Save bitmap to cache directory
            try {

                File cachePath = new File(getCacheDir(), "images");
                cachePath.mkdirs(); // don't forget to make the directory
                FileOutputStream stream = new FileOutputStream(cachePath + "/image.png"); // overwrites this image every time
                Bitmap originalBitmap = itemViewImageFragment.graph_view.getBitmap();
                int scaleFactor = 60;
                Bitmap.createScaledBitmap(
                        originalBitmap,
                        originalBitmap.getWidth() * scaleFactor,
                        originalBitmap.getHeight() * scaleFactor,
                        false)
                        .compress(Bitmap.CompressFormat.PNG, 100, stream);
                stream.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            File imagePath = new File(getCacheDir(), "images");
            File newFile = new File(imagePath, "image.png");
            Uri contentUri = FileProvider.getUriForFile(getApplicationContext(), "com.example.bruno.tabs.fileprovider", newFile);

            // Create the share intent
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission for receiving app to read this file
            shareIntent.putExtra(Intent.EXTRA_TEXT, this.item.toString());
            shareIntent.setDataAndType(contentUri, getContentResolver().getType(contentUri));
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.email_subject));
            startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.share_dialog_title)));

        } else if (id == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }
}
