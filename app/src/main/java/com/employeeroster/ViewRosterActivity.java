package com.employeeroster;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ViewRosterActivity extends AppCompatActivity {

    private TextView rosterTextView;
    private FirebaseFirestore db;
    private String staffNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_roster);

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();
        rosterTextView = findViewById(R.id.roster_text_view);

        // Get staff number from intent
        staffNumber = getIntent().getStringExtra("staffNumber");

        // Load the roster for the given staff number
        if (staffNumber != null) {
            loadRosterForStaff(staffNumber);
        } else {
            Toast.makeText(this, "Staff number is not available.", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadRosterForStaff(@NonNull String staffNumber) {
        // Access the roster document using the staff number as the document ID
        db.collection("Roster").document(staffNumber).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        StringBuilder rosterBuilder = new StringBuilder();
                        rosterBuilder.append("Staff Number: ").append(staffNumber).append("\n\n");
                        rosterBuilder.append("Monday: ").append(documentSnapshot.getString("Monday")).append("\n");
                        rosterBuilder.append("Tuesday: ").append(documentSnapshot.getString("Tuesday")).append("\n");
                        rosterBuilder.append("Wednesday: ").append(documentSnapshot.getString("Wednesday")).append("\n");
                        rosterBuilder.append("Thursday: ").append(documentSnapshot.getString("Thursday")).append("\n");
                        rosterBuilder.append("Friday: ").append(documentSnapshot.getString("Friday")).append("\n");
                        rosterBuilder.append("Saturday: ").append(documentSnapshot.getString("Saturday")).append("\n");
                        rosterBuilder.append("Sunday: ").append(documentSnapshot.getString("Sunday")).append("\n");

                        // Display the formatted roster data
                        rosterTextView.setText(rosterBuilder.toString());
                    } else {
                        rosterTextView.setText("No roster available for this staff number.");
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ViewRosterActivity.this, "Error loading roster: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}