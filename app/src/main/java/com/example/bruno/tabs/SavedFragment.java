package com.example.bruno.tabs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by bruno on 22/08/16.
 */
public class SavedFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_saved, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Construct the data source
        ArrayList<Item> items = ItemsDatabaseHelper.getInstance(getContext()).getAllItems();

        // Create the adapter to convert the array to views
        SavedItemAdapter adapter = new SavedItemAdapter(getContext(), items);
        // Attach the adapter to a ListView
        ListView listView = (ListView) view.findViewById(R.id.lvSavedItems);
        listView.setAdapter(adapter);

    }
}
