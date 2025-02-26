package com.employeeroster;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ShiftSwapRequestsAdapter extends ArrayAdapter<String> {

    public ShiftSwapRequestsAdapter(Context context, List<String> shiftSwapRequests) {
        super(context, 0, shiftSwapRequests);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String shiftSwapRequest = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(shiftSwapRequest);

        return convertView;
    }
}
