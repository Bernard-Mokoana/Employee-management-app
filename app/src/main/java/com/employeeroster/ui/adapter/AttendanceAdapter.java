package com.employeeroster.ui.adapter;


import com.employeeroster.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class AttendanceAdapter extends ArrayAdapter<String> {

    public AttendanceAdapter(Context context, List<String> attendanceRecords) {
        super(context, 0, attendanceRecords);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String attendanceRecord = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(attendanceRecord);

        return convertView;
    }
}

