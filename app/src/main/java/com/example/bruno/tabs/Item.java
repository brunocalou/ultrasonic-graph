package com.example.bruno.tabs;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by bruno on 17/09/2016.
 */
public class Item {
    public static int HEIGHT_MAP_WIDTH = 16;
    public static int HEIGHT_MAP_HEIGHT = 16;

    public long _id = -1;
    public String name;
    public Date creationDate = new Date();
    public int [][] heightMap = new int [HEIGHT_MAP_WIDTH][HEIGHT_MAP_HEIGHT];
    public FiltersConfiguration filtersConfiguration;

    public static String dateFormat = "dd-MM-yyyy";

    private static class HeightMapFormat {
        public static String valueSeparator = " ";
        public static String lineSeparator = "\n";
    }

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

    public String getFormattedHeightMap() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < heightMap.length; i++) {
            for (int j = 0; j < heightMap[i].length; j++) {
                sb.append(heightMap[i][j]);
                sb.append(HeightMapFormat.valueSeparator);
            }
            sb.append(HeightMapFormat.lineSeparator);
        }

        return sb.toString();
    }

    public void setFormattedHeightMap(String formattedHeightMap) {
        String [] lines = formattedHeightMap.split(HeightMapFormat.lineSeparator);

        for (int i = 0; i < lines.length; i++) {
            String [] lineValues = lines[i].split(HeightMapFormat.valueSeparator);

            if (lineValues != null) {
                for (int j = 0; j < lineValues.length; j++) {
                    heightMap[i][j] = Integer.parseInt(lineValues[j]);
                }
            }
        }

    }
}
