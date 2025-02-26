package com.employeeroster;

import android.os.Bundle;
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

public class ManageShiftSwapsActivity extends AppCompatActivity {

    private ListView shiftSwapsListView;
    private Button approveButton, denyButton;
    private DatabaseReference shiftSwapRequestsDatabaseReference;
    private List<String> shiftSwapRequestsList;
    private String selectedRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_shift_swaps);

        // Initialize Firebase Database reference
        shiftSwapRequestsDatabaseReference = FirebaseDatabase.getInstance().getReference("ShiftSwapRequests");

        // Bind UI elements
        shiftSwapsListView = findViewById(R.id.shift_swaps_list_view);
        approveButton = findViewById(R.id.approve_button);
        denyButton = findViewById(R.id.deny_button);
        shiftSwapRequestsList = new ArrayList<>();

        // Load shift swap requests from the database
        loadShiftSwapRequests();

        // Set up approve button click listener
        approveButton.setOnClickListener(v -> {
            if (selectedRequest != null) {
                updateRequestStatus(selectedRequest, "Approved");
            } else {
                Toast.makeText(ManageShiftSwapsActivity.this, "Please select a request to approve.", Toast.LENGTH_SHORT).show();
            }
        });

        // Set up deny button click listener
        denyButton.setOnClickListener(v -> {
            if (selectedRequest != null) {
                updateRequestStatus(selectedRequest, "Denied");
            } else {
                Toast.makeText(ManageShiftSwapsActivity.this, "Please select a request to deny.", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle list item clicks to select a request
        shiftSwapsListView.setOnItemClickListener((parent, view, position, id) -> selectedRequest = shiftSwapRequestsList.get(position));
    }

    // Method to load shift swap requests from Firebase
    private void loadShiftSwapRequests() {
        shiftSwapRequestsDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                shiftSwapRequestsList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String request = snapshot.getValue(String.class);
                    if (request != null) {
                        shiftSwapRequestsList.add(request);
                    }
                }
                // Use an adapter to display the shift swap requests in the ListView
                ShiftSwapRequestsAdapter adapter = new ShiftSwapRequestsAdapter(ManageShiftSwapsActivity.this, shiftSwapRequestsList);
                shiftSwapsListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ManageShiftSwapsActivity.this, "Failed to load shift swap requests: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to update the shift swap request status in Firebase
    private void updateRequestStatus(String requestId, String status) {
        shiftSwapRequestsDatabaseReference.child(requestId).child("status").setValue(status)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(ManageShiftSwapsActivity.this, "Request " + status.toLowerCase() + " successfully.", Toast.LENGTH_SHORT).show();
                    selectedRequest = null; // Reset the selected request
                })
                .addOnFailureListener(e -> Toast.makeText(ManageShiftSwapsActivity.this, "Failed to update request: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
