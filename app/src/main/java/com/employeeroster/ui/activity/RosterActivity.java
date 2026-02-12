package com.employeeroster.ui.activity;


import com.employeeroster.R;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RosterActivity extends AppCompatActivity {

    private EditText staffNumber1, mondayShift1, tuesdayShift1, wednesdayShift1, thursdayShift1, fridayShift1, saturdayShift1, sundayShift1;
    private Button submitRosterButton;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roster);

        // Initialize Firestore database
        db = FirebaseFirestore.getInstance();

        // Bind UI elements
        staffNumber1 = findViewById(R.id.staff_number_1);
        mondayShift1 = findViewById(R.id.monday_shift_1);
        tuesdayShift1 = findViewById(R.id.tuesday_shift_1);
        wednesdayShift1 = findViewById(R.id.wednesday_shift_1);
        thursdayShift1 = findViewById(R.id.thursday_shift_1);
        fridayShift1 = findViewById(R.id.friday_shift_1);
        saturdayShift1 = findViewById(R.id.saturday_shift_1);
        sundayShift1 = findViewById(R.id.sunday_shift_1);
        submitRosterButton = findViewById(R.id.submit_roster_button);

        // Set up button click listener to submit the roster
        submitRosterButton.setOnClickListener(v -> submitRoster());
    }

    private void submitRoster() {
        String staffNumber = staffNumber1.getText().toString().trim();
        Map<String, String> shifts = new HashMap<>();
        shifts.put("Monday", mondayShift1.getText().toString().trim());
        shifts.put("Tuesday", tuesdayShift1.getText().toString().trim());
        shifts.put("Wednesday", wednesdayShift1.getText().toString().trim());
        shifts.put("Thursday", thursdayShift1.getText().toString().trim());
        shifts.put("Friday", fridayShift1.getText().toString().trim());
        shifts.put("Saturday", saturdayShift1.getText().toString().trim());
        shifts.put("Sunday", sundayShift1.getText().toString().trim());

        if (staffNumber.isEmpty()) {
            Toast.makeText(this, "Staff number is required.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Store the roster data in Firestore
        db.collection("Roster").document(staffNumber).set(shifts)
                .addOnSuccessListener(aVoid -> Toast.makeText(RosterActivity.this, "Roster updated successfully.", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(RosterActivity.this, "Failed to update roster: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}

