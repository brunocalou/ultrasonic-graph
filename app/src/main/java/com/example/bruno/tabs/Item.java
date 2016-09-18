package com.example.bruno.tabs;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by bruno on 17/09/2016.
 */
public class Item {
    public long _id = -1;
    public String name;
    public Date creationDate;
    public int [][] heightMap;
    public FiltersConfiguration filtersConfiguration;
    public static String dateFormat = "dd-MM-yyyy";

    public Item() {
    }

    public Item(String name, Date creationDate, int[][] heightMap, FiltersConfiguration filtersConfiguration) {
        this.name = name;
        this.creationDate = creationDate;
        this.heightMap = heightMap;
        this.filtersConfiguration = filtersConfiguration;
    }

    public String getFormattedCreationDate() {
        return new SimpleDateFormat(Item.dateFormat).format(creationDate);
    }
}
