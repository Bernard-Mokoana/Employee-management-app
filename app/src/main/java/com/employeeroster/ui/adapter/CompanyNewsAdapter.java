package com.employeeroster.ui.adapter;


import com.employeeroster.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class CompanyNewsAdapter extends ArrayAdapter<String> {

    private Context mContext;
    private List<String> newsList;

    public CompanyNewsAdapter(Context context, List<String> list) {
        super(context, 0, list);
        this.mContext = context;
        this.newsList = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_news, parent, false);
        }

        // Get the data item for this position
        String news = newsList.get(position);

        // Lookup view for data population
        TextView newsTextView = convertView.findViewById(R.id.news_text_view);

        // Populate the data into the template view
        newsTextView.setText(news);

        // Return the completed view to render on screen
        return convertView;
    }
}

