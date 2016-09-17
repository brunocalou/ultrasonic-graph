package com.example.bruno.tabs;

import android.graphics.Bitmap;

import java.util.Date;

/**
 * Created by bruno on 17/09/2016.
 */
public class SavedItem {
    public String name;
    public Date creationDate;
    public Bitmap image;

    public SavedItem(String name, Date creationDate, Bitmap image) {
        this.name = name;
        this.creationDate = creationDate;
        this.image = image;
    }
}
