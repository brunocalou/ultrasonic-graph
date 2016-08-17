package com.example.bruno.tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by bruno on 08/05/16.
 */
public class FirstFragment extends Fragment {
    // Store instance variables
    private String title;
    private int page;
    private SeekBar seek_bar;
    private ContrastFilter contrast;
    private GraphView graph_view;

    // newInstance constructor for creating fragment with arguments
    public static FirstFragment newInstance(int page, String title) {
        FirstFragment fragmentFirst = new FirstFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");
        contrast = new ContrastFilter();
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);
//        TextView tvLabel = (TextView) view.findViewById(R.id.tvLabel);
//        tvLabel.setText(page + " -- " + title);
        graph_view = (GraphView) view.findViewById(R.id.graphView);

        //Add filters
        FilterList filter_list = graph_view.getFilterList();
        filter_list.add(contrast);

        seek_bar = (SeekBar) view.findViewById(R.id.seekBar);
        seek_bar.setProgress(seek_bar.getMax() / 2);
        contrast.setContrast(0);
        graph_view.applyFilters();
        seek_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                contrast.setContrast(progress - seekBar.getMax() / 2);
                graph_view.applyFilters();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        return view;
    }
}