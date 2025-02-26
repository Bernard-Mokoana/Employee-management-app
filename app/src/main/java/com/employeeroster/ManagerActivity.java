package com.employeeroster;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ManagerActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private Button btnViewSchedule, btnApproveRequests, btnTrackAttendance, btnPostUpdates, btnShiftSwaps, btnUpdateRoster, btnLogout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Find buttons in the layout
        btnViewSchedule = findViewById(R.id.btn_view_schedule);
        btnApproveRequests = findViewById(R.id.btn_approve_requests);
        btnTrackAttendance = findViewById(R.id.btn_track_attendance);
        btnPostUpdates = findViewById(R.id.btn_post_updates);
        btnShiftSwaps = findViewById(R.id.btn_shift_swaps);
        btnUpdateRoster = findViewById(R.id.btn_update_roster);  // New button for updating roster
        btnLogout = findViewById(R.id.btn_logout);

        // Set up button listeners
        setupButtonListeners();
    }

    // Method to set up button listeners for the actions
    private void setupButtonListeners() {
        btnViewSchedule.setOnClickListener(v -> {
            Intent intent = new Intent(ManagerActivity.this, ViewScheduleActivity.class);
            startActivity(intent);
        });

        btnApproveRequests.setOnClickListener(v -> {
            Intent intent = new Intent(ManagerActivity.this, TimeOffManagerActivity.class);
            startActivity(intent);
        });

        btnTrackAttendance.setOnClickListener(v -> {
            Intent intent = new Intent(ManagerActivity.this, TrackAttendanceActivity.class);
            startActivity(intent);
            loadAttendanceRecords();
        });

        btnPostUpdates.setOnClickListener(v -> {
            postUpdate();
        });

        btnShiftSwaps.setOnClickListener(v -> {
            Intent intent = new Intent(ManagerActivity.this, ManageShiftSwapsActivity.class);
            startActivity(intent);
            loadShiftSwapRequests();
        });

        // New Update Roster button action
        btnUpdateRoster.setOnClickListener(v -> {
            Intent intent = new Intent(ManagerActivity.this, RosterActivity.class);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(ManagerActivity.this, LoginActivity.class));
            finish();
        });
    }

    // Method to load attendance records
    private void loadAttendanceRecords() {
        db.collection("attendanceRecords").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(ManagerActivity.this, "Attendance records loaded successfully.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ManagerActivity.this, "Error loading attendance records: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Method to post updates on the app
    private void postUpdate() {
        Toast.makeText(ManagerActivity.this, "Feature to post updates coming soon.", Toast.LENGTH_SHORT).show();
    }

    // Method to load shift swap requests
    private void loadShiftSwapRequests() {
        db.collection("shiftSwapRequests").whereEqualTo("status", "Pending").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(ManagerActivity.this, "Shift swap requests loaded successfully.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ManagerActivity.this, "Error loading shift swap requests: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
