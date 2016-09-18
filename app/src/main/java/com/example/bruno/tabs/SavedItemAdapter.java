package com.example.bruno.tabs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by bruno on 17/09/2016.
 */
public class SavedItemAdapter extends ArrayAdapter<Item> {

    public SavedItemAdapter(Context context, ArrayList<Item> items) {
        super(context, R.layout.saved_item, items);
    }

//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        // Get the data item for this position
//        Item savedItem = getItem(position);
//        // Check if an existing view is being reused, otherwise inflate the view
//        if (convertView == null) {
//            convertView = LayoutInflater.from(getContext()).inflate(R.layout.saved_item, parent, false);
//        }
//        // Lookup view for data population
//        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
//        TextView tvDate = (TextView) convertView.findViewById(R.id.tvDate);
//        // Populate the data into the template view using the data object
//        tvName.setText(savedItem.name);
//        tvDate.setText(savedItem.creationDate.toString());
//        // Return the completed view to render on screen
//        return convertView;
//    }

    // View lookup cache
    private static class ViewHolder {
        TextView name;
        TextView date;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Item item = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.saved_item, parent, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.tvName);
            viewHolder.date = (TextView) convertView.findViewById(R.id.tvDate);
            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object
        viewHolder.name.setText(item.name);
        viewHolder.date.setText(item.getFormattedCreationDate());
        // Return the completed view to render on screen
        return convertView;
    }
}
