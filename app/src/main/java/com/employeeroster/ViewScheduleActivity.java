package com.employeeroster;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewScheduleActivity extends AppCompatActivity {

    private ListView scheduleListView;
    private DatabaseReference scheduleDatabaseReference;
    private List<String> scheduleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_schedule);

        // Initialize Firebase Database reference
        scheduleDatabaseReference = FirebaseDatabase.getInstance().getReference("ScheduledEvents");

        // Bind UI elements
        scheduleListView = findViewById(R.id.schedule_list_view);
        scheduleList = new ArrayList<>();

        // Load scheduled events
        loadScheduledEvents();
    }

    private void loadScheduledEvents() {
        scheduleDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                scheduleList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String event = snapshot.getValue(String.class);
                    if (event != null) {
                        scheduleList.add(event);
                    }
                }
                // Use an adapter to display the schedule in the ListView
                ScheduleAdapter adapter = new ScheduleAdapter(ViewScheduleActivity.this, scheduleList);
                scheduleListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ViewScheduleActivity.this, "Failed to load scheduled events: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
