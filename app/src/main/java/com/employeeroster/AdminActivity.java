package com.employeeroster;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class AdminActivity extends AppCompatActivity {

    private Button createUserButton, manageRosterButton, manageTimeOffButton, manageShiftsButton, logoutButton;
    private FirebaseAuth mAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Bind UI elements
        createUserButton = findViewById(R.id.create_user_button);
        manageRosterButton = findViewById(R.id.manage_roster_button);
        manageTimeOffButton = findViewById(R.id.manage_time_off_button);
        manageShiftsButton = findViewById(R.id.manage_shifts_button);
        logoutButton = findViewById(R.id.logout_button);

        // Set up create user button
        createUserButton.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, CreateUserActivity.class);
            startActivity(intent);
        });

        // Set up manage roster button
        manageRosterButton.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, RosterActivity.class);
            startActivity(intent);
        });

        // Set up manage time off button
        manageTimeOffButton.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, TimeOffManagerActivity.class);
            startActivity(intent);
        });

        // Set up manage shifts button with error handling
        manageShiftsButton.setOnClickListener(v -> {
                Intent intent = new Intent(AdminActivity.this, ShiftManager.class);
                startActivity(intent);
        });

        // Set up logout button
        logoutButton.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(AdminActivity.this, LoginActivity.class));
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        // Handle back press
        new AlertDialog.Builder(this)
                .setTitle("Exit")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton(android.R.string.yes, (dialog, which) -> super.onBackPressed())
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
