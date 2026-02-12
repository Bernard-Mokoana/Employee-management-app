package com.employeeroster.ui.activity;


import com.employeeroster.R;
import com.employeeroster.ui.adapter.MeetingScheduleAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EmployeeDashboardActivity extends AppCompatActivity {

    private TextView workDaysTextView, companyUpdatesTextView;
    private ListView meetingSchedulesListView;
    private Button requestLeaveButton;
    private DatabaseReference dashboardDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_dashboard);

        // Initialize Firebase Database reference
        dashboardDatabaseReference = FirebaseDatabase.getInstance().getReference("Dashboard");

        // Bind UI elements
        workDaysTextView = findViewById(R.id.work_days_text_view);
        companyUpdatesTextView = findViewById(R.id.company_updates_text_view);
        meetingSchedulesListView = findViewById(R.id.meeting_schedules_list_view);
        requestLeaveButton = findViewById(R.id.request_leave_button);

        // Load dashboard data
        loadWorkDays();
        loadCompanyUpdates();
        loadMeetingSchedules();

        // Set up request leave button
        requestLeaveButton.setOnClickListener(v -> startActivity(new Intent(EmployeeDashboardActivity.this, LeaveRequestActivity.class)));
    }

    private void loadWorkDays() {
        dashboardDatabaseReference.child("workDays").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String workDays = dataSnapshot.getValue(String.class);
                workDaysTextView.setText(workDays != null ? workDays : "No workday information available");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(EmployeeDashboardActivity.this, "Failed to load workdays: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadCompanyUpdates() {
        dashboardDatabaseReference.child("companyUpdates").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String updates = dataSnapshot.getValue(String.class);
                companyUpdatesTextView.setText(updates != null ? updates : "No updates available");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(EmployeeDashboardActivity.this, "Failed to load updates: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadMeetingSchedules() {
        dashboardDatabaseReference.child("meetingSchedules").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> schedules = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String schedule = snapshot.getValue(String.class);
                    if (schedule != null) {
                        schedules.add(schedule);
                    }
                }
                MeetingScheduleAdapter adapter = new MeetingScheduleAdapter(EmployeeDashboardActivity.this, schedules);
                meetingSchedulesListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(EmployeeDashboardActivity.this, "Failed to load meeting schedules: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

