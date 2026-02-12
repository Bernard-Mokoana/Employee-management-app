package com.employeeroster.ui.activity;


import com.employeeroster.R;
import com.employeeroster.ui.adapter.AttendanceAdapter;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class TrackAttendanceActivity extends AppCompatActivity {

    private ListView attendanceListView;
    private FirebaseFirestore db;
    private List<String> attendanceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_attendance);

        // Initialize Firestore instance
        db = FirebaseFirestore.getInstance();

        // Bind UI elements
        attendanceListView = findViewById(R.id.attendance_list_view);
        attendanceList = new ArrayList<>();

        // Load attendance records from Firestore
        loadAttendanceRecords();
    }

    private void loadAttendanceRecords() {
        db.collection("AttendanceRecords").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        attendanceList.clear();

                        if (querySnapshot != null) {
                            for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                                String staffNumber = document.getString("staffNumber");
                                String role = document.getString("role");
                                String action = document.getString("action");
                                String timestamp = document.getString("timestamp");

                                // Format the attendance record for display
                                String attendanceRecord = "Staff Number: " + staffNumber + "\nRole: " + role + "\nAction: " + action + "\nTime: " + timestamp;
                                attendanceList.add(attendanceRecord);
                            }
                        }

                        // Use an adapter to display the attendance records in the ListView
                        AttendanceAdapter adapter = new AttendanceAdapter(TrackAttendanceActivity.this, attendanceList);
                        attendanceListView.setAdapter(adapter);
                    } else {
                        Toast.makeText(TrackAttendanceActivity.this, "Error loading attendance records: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(TrackAttendanceActivity.this, "Failed to load attendance records: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}

