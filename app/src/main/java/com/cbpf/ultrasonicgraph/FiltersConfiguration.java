package com.cbpf.ultrasonicgraph;

/**
 * FiltersConfiguration holds all the filters values. It's used to associate an {@link Item} with many
 * {@link Filter}s. It's useful when storing and retrieving the filters from the database
 * @see ItemsDatabaseHelper
 */
public class FiltersConfiguration {
    public long _id = -1;
    public int contrast = 0;

    @Override
    public String toString() {
        return
                "contrast=" + contrast;
    }
}
