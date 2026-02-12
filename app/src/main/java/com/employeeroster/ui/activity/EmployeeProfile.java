package com.employeeroster.ui.activity;

import com.employeeroster.R;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.HashMap;
import java.util.Map;

public class EmployeeProfile extends AppCompatActivity {

    private EditText firstNameInput, lastNameInput, emailInput;
    private Button updateProfileButton;
    private DatabaseReference userDatabaseReference;
    private FirebaseAuth mAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_profile);

        // Initialize Firebase Auth and Database reference
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            userDatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid());
        }

        // Bind UI elements
        firstNameInput = findViewById(R.id.first_name_input);
        lastNameInput = findViewById(R.id.last_name_input);
        emailInput = findViewById(R.id.email_input);
        updateProfileButton = findViewById(R.id.update_profile_button);

        // Load user profile
        loadUserProfile();

        // Set up update profile button
        updateProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = firstNameInput.getText().toString().trim();
                String lastName = lastNameInput.getText().toString().trim();
                String email = emailInput.getText().toString().trim();

                if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()) {
                    Toast.makeText(EmployeeProfile.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                updateUserProfile(firstName, lastName, email);
            }
        });
    }

    private void loadUserProfile() {
        userDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String firstName = dataSnapshot.child("firstName").getValue(String.class);
                String lastName = dataSnapshot.child("lastName").getValue(String.class);
                String email = dataSnapshot.child("email").getValue(String.class);

                if (firstName != null) firstNameInput.setText(firstName);
                if (lastName != null) lastNameInput.setText(lastName);
                if (email != null) emailInput.setText(email);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(EmployeeProfile.this, "Failed to load profile: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUserProfile(String firstName, String lastName, String email) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("firstName", firstName);
        updates.put("lastName", lastName);
        updates.put("email", email);

        userDatabaseReference.updateChildren(updates)
                .addOnSuccessListener(aVoid -> Toast.makeText(EmployeeProfile.this, "Profile updated successfully", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(EmployeeProfile.this, "Failed to update profile: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
