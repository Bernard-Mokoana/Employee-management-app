package com.employeeroster.ui.activity;


import com.employeeroster.R;
import com.employeeroster.ui.adapter.TimeOffRequestsAdapter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class TimeOffManagerActivity extends AppCompatActivity {

    private ListView timeOffRequestsListView;
    private Button approveButton, denyButton;
    private DatabaseReference timeOffRequestsDatabaseReference;
    private List<String> timeOffRequests;
    private String selectedRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_off_manager);

        // Initialize Firebase Database reference
        timeOffRequestsDatabaseReference = FirebaseDatabase.getInstance().getReference("TimeOffRequests");

        // Bind UI elements
        timeOffRequestsListView = findViewById(R.id.time_off_requests_list_view);
        approveButton = findViewById(R.id.approve_button);
        denyButton = findViewById(R.id.deny_button);
        timeOffRequests = new ArrayList<>();

        // Load time-off requests from the database
        loadTimeOffRequests();

        // Set up approve button click listener
        approveButton.setOnClickListener(v -> {
            if (selectedRequest != null) {
                updateRequestStatus(selectedRequest, "Approved");
            } else {
                Toast.makeText(TimeOffManagerActivity.this, "Please select a request to approve.", Toast.LENGTH_SHORT).show();
            }
        });

        // Set up deny button click listener
        denyButton.setOnClickListener(v -> {
            if (selectedRequest != null) {
                updateRequestStatus(selectedRequest, "Denied");
            } else {
                Toast.makeText(TimeOffManagerActivity.this, "Please select a request to deny.", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle list item clicks to select a request
        timeOffRequestsListView.setOnItemClickListener((parent, view, position, id) -> selectedRequest = timeOffRequests.get(position));
    }

    // Method to load time-off requests from Firebase
    private void loadTimeOffRequests() {
        timeOffRequestsDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                timeOffRequests.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String request = snapshot.getValue(String.class);
                    if (request != null) {
                        timeOffRequests.add(request);
                    }
                }
                // Create an ArrayAdapter to populate the ListView with time-off requests
                TimeOffRequestsAdapter adapter = new TimeOffRequestsAdapter(TimeOffManagerActivity.this, timeOffRequests);
                timeOffRequestsListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(TimeOffManagerActivity.this, "Failed to load requests: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to update the request status in Firebase
    private void updateRequestStatus(String requestId, String status) {
        timeOffRequestsDatabaseReference.child(requestId).child("status").setValue(status)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(TimeOffManagerActivity.this, "Request " + status.toLowerCase() + " successfully.", Toast.LENGTH_SHORT).show();
                    selectedRequest = null; // Reset the selected request
                })
                .addOnFailureListener(e -> Toast.makeText(TimeOffManagerActivity.this, "Failed to update request: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}

