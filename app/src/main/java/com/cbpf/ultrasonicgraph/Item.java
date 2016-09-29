package com.cbpf.ultrasonicgraph;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Item holds an item to be saved on the Database. It's composed of a name, creation date, heightmap
 * and a {@link FiltersConfiguration}. These are the necessary data to recreate the {@link GraphView}
 * image and the {@link Histogram}
 * @see GraphView
 * @see Histogram
 * @see ItemsDatabaseHelper
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

    /**
     * Get a string on the {@link #dateFormat} format
     * @return The formatted date
     */
    public String getFormattedCreationDate() {
        return new SimpleDateFormat(Item.dateFormat).format(creationDate);
    }

    /**
     * <p>Get the {@link #heightMap} as a string. The structure for each line is: </p>
     * <p>value-1 value-separator value-2 value-separator ... value-n line-separator</p>
     * <p>e.g. for values = [1,2,3,4], value-separator = " " and line-separator = "\n", the line will be: </p>
     * <p>1 2 3 4\n</p>
     * @return The formatted height map
     */
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

    /**
     * <p>Set the {@link #heightMap} based on the formatted height map string</p>
     * <p>Note that the string must have been exported using the {@link #getFormattedHeightMap()}
     * method</p>
     * @param formattedHeightMap The formatted height map as a string
     */
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

    @Override
    public String toString() {
        return
                "name=" + name + '\n' +
                "creationDate=" + getFormattedCreationDate() + '\n' +
                "heightMap=" + '\n' + getFormattedHeightMap() + '\n' +
                filtersConfiguration.toString();
    }
}
