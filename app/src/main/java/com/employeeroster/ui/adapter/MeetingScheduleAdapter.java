package com.employeeroster.ui.adapter;


import com.employeeroster.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class MeetingScheduleAdapter extends ArrayAdapter<String> {

    private Context mContext;
    private List<String> meetingScheduleList;

    public MeetingScheduleAdapter(Context context, List<String> list) {
        super(context, 0, list);
        this.mContext = context;
        this.meetingScheduleList = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_meeting_schedule, parent, false);
        }

        // Get the data item for this position
        String schedule = meetingScheduleList.get(position);

        // Lookup view for data population
        TextView scheduleTextView = convertView.findViewById(R.id.schedule_text_view);

        // Populate the data into the template view
        scheduleTextView.setText(schedule);

        // Return the completed view to render on screen
        return convertView;
    }
}

