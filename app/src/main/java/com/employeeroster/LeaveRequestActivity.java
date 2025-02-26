package com.employeeroster;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LeaveRequestActivity extends AppCompatActivity {

    private EditText leaveTypeEditText, startDateEditText, endDateEditText, notesEditText;
    private Button submitLeaveButton;
    private DatabaseReference leaveRequestDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_request);

        // Initialize Firebase Database reference
        leaveRequestDatabaseReference = FirebaseDatabase.getInstance().getReference("LeaveRequests");

        // Bind UI elements
        leaveTypeEditText = findViewById(R.id.leave_type_edit_text);
        startDateEditText = findViewById(R.id.start_date_edit_text);
        endDateEditText = findViewById(R.id.end_date_edit_text);
        notesEditText = findViewById(R.id.notes_edit_text);
        submitLeaveButton = findViewById(R.id.submit_leave_button);

        // Set up submit button action
        submitLeaveButton.setOnClickListener(v -> submitLeaveRequest());
    }

    private void submitLeaveRequest() {
        // Get values from form fields
        String leaveType = leaveTypeEditText.getText().toString().trim();
        String startDate = startDateEditText.getText().toString().trim();
        String endDate = endDateEditText.getText().toString().trim();
        String notes = notesEditText.getText().toString().trim();

        // Validate form input
        if (TextUtils.isEmpty(leaveType)) {
            Toast.makeText(this, "Please enter the type of leave", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(startDate)) {
            Toast.makeText(this, "Please enter the start date", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(endDate)) {
            Toast.makeText(this, "Please enter the end date", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a unique ID for the leave request
        String requestId = leaveRequestDatabaseReference.push().getKey();

        // Create LeaveRequest object
        LeaveRequest leaveRequest = new LeaveRequest(requestId, leaveType, startDate, endDate, notes);

        // Store the leave request in Firebase
        if (requestId != null) {
            leaveRequestDatabaseReference.child(requestId).setValue(leaveRequest)
                    .addOnSuccessListener(aVoid -> Toast.makeText(LeaveRequestActivity.this, "Leave request submitted", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(LeaveRequestActivity.this, "Failed to submit leave request", Toast.LENGTH_SHORT).show());
        }
    }
}
