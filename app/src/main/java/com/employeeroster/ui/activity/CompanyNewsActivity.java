package com.employeeroster.ui.activity;


import com.employeeroster.R;
import com.employeeroster.ui.adapter.CompanyNewsAdapter;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CompanyNewsActivity extends AppCompatActivity {

    private ListView newsListView;
    private DatabaseReference newsDatabaseReference;
    private List<String> newsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_news);

        // Initialize Firebase Database reference
        newsDatabaseReference = FirebaseDatabase.getInstance().getReference("CompanyNews");

        // Bind UI elements
        newsListView = findViewById(R.id.news_list_view);

        // Load company news
        loadCompanyNews();
    }

    private void loadCompanyNews() {
        newsDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                newsList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String news = snapshot.getValue(String.class);
                    if (news != null) {
                        newsList.add(news);
                    }
                }
                CompanyNewsAdapter adapter = new CompanyNewsAdapter(CompanyNewsActivity.this, newsList);
                newsListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(CompanyNewsActivity.this, "Failed to load news: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

