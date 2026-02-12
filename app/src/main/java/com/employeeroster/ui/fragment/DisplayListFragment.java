package com.employeeroster.ui.fragment;


import com.employeeroster.R;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class DisplayListFragment extends Fragment {

    private ListView listView;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the fragment layout that contains the ListView
        View view = inflater.inflate(R.layout.fragment_display_list, container, false);

        // Find the ListView
        listView = view.findViewById(R.id.list_view);

        // Get data from arguments
        if (getArguments() != null) {
            ArrayList<String> data = getArguments().getStringArrayList("data");
            // Set the data to the ListView using an ArrayAdapter
            if (data != null) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, data);
                listView.setAdapter(adapter);
            }
        }

        return view;  // Make sure to return android.view.View
    }
}

