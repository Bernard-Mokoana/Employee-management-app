package com.employeeroster;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SwapShiftActivity extends AppCompatActivity {

    private EditText reasonEditText;
    private Spinner desiredShiftSpinner;
    private Button submitSwapButton;
    private DatabaseReference swapRequestDatabaseReference, availableShiftsDatabaseReference;
    private FirebaseAuth mAuth;
    private String staffNumber;
    private List<String> availableShiftsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swap_shift);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            swapRequestDatabaseReference = FirebaseDatabase.getInstance().getReference("ShiftSwapRequests").child(currentUser.getUid());
            availableShiftsDatabaseReference = FirebaseDatabase.getInstance().getReference("AvailableShifts");
        } else {
            Toast.makeText(SwapShiftActivity.this, "You are not authenticated. Please log in again.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Bind UI elements
        reasonEditText = findViewById(R.id.reason_edit_text);
        desiredShiftSpinner = findViewById(R.id.desired_shift_spinner);
        submitSwapButton = findViewById(R.id.submit_swap_button);
        availableShiftsList = new ArrayList<>();

        // Load available shifts for the Spinner
        loadAvailableShifts();

        // Get staff number from intent
        staffNumber = getIntent().getStringExtra("staffNumber");

        // Set up button listener
        submitSwapButton.setOnClickListener(v -> submitShiftSwapRequest());
    }

    private void loadAvailableShifts() {
        if (availableShiftsDatabaseReference != null) {
            availableShiftsDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    availableShiftsList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String shift = snapshot.getValue(String.class);
                        if (shift != null) {
                            availableShiftsList.add(shift);
                        }
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(SwapShiftActivity.this, android.R.layout.simple_spinner_item, availableShiftsList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    desiredShiftSpinner.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(SwapShiftActivity.this, "Failed to load available shifts: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("SwapShiftActivity", "Error loading available shifts: " + databaseError.getMessage());
                }
            });
        }
    }

    private void submitShiftSwapRequest() {
        String reason = reasonEditText.getText().toString().trim();
        String desiredShift = desiredShiftSpinner.getSelectedItem() != null ? desiredShiftSpinner.getSelectedItem().toString() : "";

        if (reason.isEmpty()) {
            Toast.makeText(SwapShiftActivity.this, "Please enter a reason for the shift swap.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (desiredShift.isEmpty()) {
            Toast.makeText(SwapShiftActivity.this, "Please select a desired shift.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (swapRequestDatabaseReference != null && staffNumber != null) {
            String swapRequestId = swapRequestDatabaseReference.push().getKey();

            if (swapRequestId != null) {
                swapRequestDatabaseReference.child(swapRequestId).child("staffNumber").setValue(staffNumber);
                swapRequestDatabaseReference.child(swapRequestId).child("reason").setValue(reason);
                swapRequestDatabaseReference.child(swapRequestId).child("desiredShift").setValue(desiredShift)
                        .addOnSuccessListener(aVoid -> Toast.makeText(SwapShiftActivity.this, "Shift swap request submitted successfully.", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> {
                            Toast.makeText(SwapShiftActivity.this, "Failed to submit shift swap request: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("SwapShiftActivity", "Error submitting shift swap request: " + e.getMessage());
                        });
            }
        } else {
            Toast.makeText(SwapShiftActivity.this, "Unable to submit request. Staff number or database reference is missing.", Toast.LENGTH_SHORT).show();
        }
    }
}
