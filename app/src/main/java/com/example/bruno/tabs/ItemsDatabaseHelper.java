package com.example.bruno.tabs;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by bruno on 18/09/2016.
 */
public class ItemsDatabaseHelper extends SQLiteOpenHelper {
    //ItemsDatabaseHelper instance, needed to implement the singleton parttern
    private static ItemsDatabaseHelper instance;

    // Database Info
    private static final String DATABASE_NAME = "postsDatabase";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_ITEMS = "items";
    private static final String TABLE_FILTERS = "filters";

    // Item Table Columns
    private static final String KEY_ITEM_ID = "id";
    private static final String KEY_ITEM_FILTERS_ID_FK = "filtersId"; //Foreign key for the filters table
    private static final String KEY_ITEM_NAME = "name";
    private static final String KEY_ITEM_CREATION_DATE = "creationDate";

    // Filter table columns
    private static final String KEY_FILTERS_ID = "id";
    private static final String KEY_FILTERS_CONTRAST_VALUE = "contrast";

    /**
     * Constructor should be private to prevent direct instantiation.
     * Make a call to the static method "getInstance()" instead.
     */
    private ItemsDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized ItemsDatabaseHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (instance == null) {
            instance = new ItemsDatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    // Called when the database is created for the FIRST time.
    // If a database already exists on disk with the same DATABASE_NAME, this method will NOT be called.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ITEMS_TABLE = "CREATE TABLE " + TABLE_ITEMS +
                "(" +
                    KEY_ITEM_ID + " INTEGER PRIMARY KEY," + // Define a primary key
                    KEY_ITEM_FILTERS_ID_FK + " INTEGER REFERENCES " + TABLE_FILTERS + "," + // Define a foreign key
                    KEY_ITEM_NAME + " TEXT," +
                    KEY_ITEM_CREATION_DATE + " STRING" +
                ")";

        String CREATE_FILTERS_TABLE = "CREATE TABLE " + TABLE_FILTERS +
                "(" +
                    KEY_FILTERS_ID + " INTEGER PRIMARY KEY," +
                    KEY_FILTERS_CONTRAST_VALUE + " INTEGER" +
                ")";

        db.execSQL(CREATE_ITEMS_TABLE);
        db.execSQL(CREATE_FILTERS_TABLE);
    }

    // Called when the database needs to be upgraded.
    // This method will only be called if a database already exists on disk with the same DATABASE_NAME,
    // but the DATABASE_VERSION is different than the version of the database that exists on disk.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_FILTERS);
            onCreate(db);
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_FILTERS);
            onCreate(db);
        }
    }

    // Insert an item into the database
    public long addItem(Item item) {
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();
        try {
            //Add the filters configuration to the data base
            addFiltersConfiguration(item.filtersConfiguration);

            ContentValues values = new ContentValues();
            values.put(KEY_ITEM_FILTERS_ID_FK, item.filtersConfiguration._id);
            values.put(KEY_ITEM_NAME, item.name);
            values.put(KEY_ITEM_CREATION_DATE, item.getFormattedCreationDate());

            // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
            item._id = db.insertOrThrow(TABLE_ITEMS, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(getClass().getSimpleName(), "Error while trying to add item to database");
        } finally {
            db.endTransaction();
        }

        return item._id;
    }

    private long addFiltersConfiguration(FiltersConfiguration filtersConfiguration) {
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_FILTERS_CONTRAST_VALUE, filtersConfiguration.contrast);

            // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
            filtersConfiguration._id = db.insertOrThrow(TABLE_FILTERS, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(getClass().getSimpleName(), "Error while trying to add filters configuration to database");
        } finally {
            db.endTransaction();
        }

        return filtersConfiguration._id;
    }

    public ArrayList<Item> getAllItems() {
        ArrayList<Item> items = new ArrayList<>();

        // SELECT * FROM ITEMS
        // LEFT OUTER JOIN FILTERS
        // ON ITEMS.KEY_ITEM_FILTERS_ID_FK = FILTERS.KEY_FILTERS_ID
        String ITEMS_SELECT_QUERY=
                String.format("SELECT * FROM %s LEFT OUTER JOIN %s ON %s.%s = %s.%s",
                        TABLE_ITEMS,
                        TABLE_FILTERS,
                        TABLE_ITEMS, KEY_ITEM_FILTERS_ID_FK,
                        TABLE_FILTERS, KEY_FILTERS_ID);

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(ITEMS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Item newItem = new Item();
//                    DateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
//                    Date date = Date.parse(cursor.getString(cursor.getColumnIndex(KEY_ITEM_CREATION_DATE)));
                    Date date = new SimpleDateFormat(Item.dateFormat).parse(cursor.getString(cursor.getColumnIndex(KEY_ITEM_CREATION_DATE)));

                    FiltersConfiguration newFiltersConfiguration = new FiltersConfiguration();
                    newFiltersConfiguration.contrast = Integer.valueOf(cursor.getString(cursor.getColumnIndex(KEY_FILTERS_CONTRAST_VALUE)));

                    newItem.name = cursor.getString(cursor.getColumnIndex(KEY_ITEM_NAME));
                    newItem.creationDate = date;
                    newItem.filtersConfiguration = newFiltersConfiguration;

                    items.add(newItem);

                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(getClass().getSimpleName(), "Error while trying to get items from database");
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return items;
    }

    // Update the item
    public int updateItem(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ITEM_NAME, item.name);
        values.put(KEY_ITEM_CREATION_DATE, item.creationDate.toString());

        // Updating item
        return db.update(TABLE_ITEMS, values, KEY_ITEM_ID + " = ?",
                new String[] { String.valueOf(item._id) });
    }

    // Update the filters
    public int updateFiltersConfiguration(FiltersConfiguration filtersConfiguration) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FILTERS_CONTRAST_VALUE, filtersConfiguration.contrast);

        // Updating item
        return db.update(TABLE_FILTERS, values, KEY_FILTERS_ID + " = ?",
                new String[] { String.valueOf(filtersConfiguration._id) });
    }

    // Delete the item and the related filter configuration
    public int deleteItem(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FILTERS, KEY_FILTERS_ID + " = ?", new String[] { String.valueOf(item.filtersConfiguration._id) });

        return db.delete(TABLE_ITEMS, KEY_ITEM_ID + " = ?", new String[] { String.valueOf(item._id) });
    }

    // Delete all items and filters
    public void deleteAllItemsAndFilters() {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            // Order of deletions is important when foreign key relationships exist.
            db.delete(TABLE_ITEMS, null, null);
            db.delete(TABLE_FILTERS, null, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(getClass().getSimpleName(), "Error while trying to delete all items and filters");
        } finally {
            db.endTransaction();
        }
    }
}
