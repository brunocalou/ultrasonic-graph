package com.example.bruno.tabs;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by bruno on 22/08/16.
 */
public class SavedFragment extends Fragment {
    private ListView listView;

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

        // Attach the adapter to a ListView
        listView = (ListView) view.findViewById(R.id.lvSavedItems);


    }

    @Override
    public void onResume() {
        super.onResume();

        // Construct the data source
        final ArrayList<Item> items = ItemsDatabaseHelper.getInstance(getContext()).getAllItems();

        // Create the adapter to convert the array to views
        SavedItemAdapter adapter = new SavedItemAdapter(getContext(), items);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(getClass().getSimpleName(), "Clicked on item" + position);
                Item item = items.get(position);
                Intent intent = new Intent(getContext(), ItemViewActivity.class);
                intent.putExtra(ItemViewActivity.ITEM_ID, item._id);
                startActivity(intent);
            }
        });
    }
}
