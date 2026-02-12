package com.employeeroster.ui.activity;


import com.employeeroster.R;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class GeneralWorkerActivity extends AppCompatActivity {

    private TextView workerStaffNumberTextView;
    private Button requestTimeOffButton, logoutButton, clockInButton, clockOutButton, swapShiftButton, viewRosterButton;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String staffNumber;
    private final String role = "General Worker";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_worker);

        // Initialize Firebase Auth and Firestore instance
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            // Redirect to login if not authenticated
            redirectToLogin();
            return;
        }

        // Bind UI elements
        workerStaffNumberTextView = findViewById(R.id.worker_staff_number_text_view);
        requestTimeOffButton = findViewById(R.id.request_time_off_button);
        logoutButton = findViewById(R.id.logout_button);
        clockInButton = findViewById(R.id.clock_in_button);
        clockOutButton = findViewById(R.id.clock_out_button);
        swapShiftButton = findViewById(R.id.swap_shift_button);
        viewRosterButton = findViewById(R.id.view_roster_button); // New button for viewing roster

        // Load worker's staff number from Firestore
        loadStaffNumber(currentUser.getUid());

        // Set up button listeners
        setupButtonListeners();
    }

    private void setupButtonListeners() {
        // Request time-off button
        requestTimeOffButton.setOnClickListener(v -> {
            Intent requestTimeOffIntent = new Intent(GeneralWorkerActivity.this, LeaveRequestActivity.class);
            startActivity(requestTimeOffIntent);
        });

        // Logout button
        logoutButton.setOnClickListener(v -> {
            mAuth.signOut();
            redirectToLogin();
        });

        // Clock in button
        clockInButton.setOnClickListener(v -> {
            if (isStaffNumberValid()) {
                recordAttendanceToFirestore(staffNumber, role, "Clock In");
            } else {
                Toast.makeText(GeneralWorkerActivity.this, "Please wait, staff number is still loading.", Toast.LENGTH_SHORT).show();
            }
        });

        // Clock out button
        clockOutButton.setOnClickListener(v -> {
            if (isStaffNumberValid()) {
                recordAttendanceToFirestore(staffNumber, role, "Clock Out");
            } else {
                Toast.makeText(GeneralWorkerActivity.this, "Please wait, staff number is still loading.", Toast.LENGTH_SHORT).show();
            }
        });

        // Shift swap button
        swapShiftButton.setOnClickListener(v -> {
            if (isStaffNumberValid()) {
                Intent intent = new Intent(GeneralWorkerActivity.this, SwapShiftActivity.class);
                intent.putExtra("staffNumber", staffNumber);
                startActivity(intent);
            } else {
                Toast.makeText(GeneralWorkerActivity.this, "Please wait, staff number is still loading.", Toast.LENGTH_SHORT).show();
            }
        });

        // View roster button
        viewRosterButton.setOnClickListener(v -> {
            if (isStaffNumberValid()) {
                Intent intent = new Intent(GeneralWorkerActivity.this, ViewRosterActivity.class);
                intent.putExtra("staffNumber", staffNumber);
                startActivity(intent);
            } else {
                Toast.makeText(GeneralWorkerActivity.this, "Please wait, staff number is still loading.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadStaffNumber(String userId) {
        // Fetch staff number from Firestore
        db.collection("Users").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        staffNumber = documentSnapshot.getString("staffNumber");
                        if (staffNumber != null && !staffNumber.isEmpty()) {
                            workerStaffNumberTextView.setText("Staff Number: " + staffNumber); // Display the staff number
                        } else {
                            Toast.makeText(GeneralWorkerActivity.this, "Staff number is not available. Please contact your administrator.", Toast.LENGTH_SHORT).show();
                            Log.e("GeneralWorkerActivity", "Staff number is null or empty.");
                        }
                    } else {
                        Toast.makeText(GeneralWorkerActivity.this, "User data not found. Please contact your administrator.", Toast.LENGTH_SHORT).show();
                        Log.e("GeneralWorkerActivity", "User document does not exist.");
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(GeneralWorkerActivity.this, "Error retrieving staff number: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("GeneralWorkerActivity", "Error loading staff number: " + e.getMessage(), e);
                });
    }

    private void recordAttendanceToFirestore(String staffNumber, String role, String action) {
        String currentTime = getCurrentDateTime();
        Map<String, Object> attendanceData = new HashMap<>();
        attendanceData.put("staffNumber", staffNumber);
        attendanceData.put("role", role);
        attendanceData.put("action", action);
        attendanceData.put("timestamp", currentTime);

        // Store the attendance data in Firestore under the "AttendanceRecords" collection
        db.collection("AttendanceRecords")
                .add(attendanceData)
                .addOnSuccessListener(documentReference -> Toast.makeText(GeneralWorkerActivity.this, action + " recorded successfully at " + currentTime, Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(GeneralWorkerActivity.this, "Failed to record " + action + ": " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    private boolean isStaffNumberValid() {
        return staffNumber != null && !staffNumber.isEmpty();
    }

    private void redirectToLogin() {
        Intent loginIntent = new Intent(GeneralWorkerActivity.this, LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }
}

