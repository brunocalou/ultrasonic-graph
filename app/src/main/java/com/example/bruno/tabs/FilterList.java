package com.example.bruno.tabs;

import android.graphics.Bitmap;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Created by bruno on 19/06/16.
 */
public class FilterList extends LinkedList<Filter> {

    public void apply(Bitmap image) {
        ListIterator<Filter> listIterator = this.listIterator();
        while (listIterator.hasNext()) {
            listIterator.next().apply(image);
        }
    }
}
