package com.employeeroster;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LogoutActivity extends AppCompatActivity {

    private Button confirmLogoutButton, cancelLogoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);

        confirmLogoutButton = findViewById(R.id.confirm_logout_button);
        cancelLogoutButton = findViewById(R.id.cancel_logout_button);

        confirmLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(LogoutActivity.this, LoginActivity.class));
                finish(); // Close the LogoutActivity
            }
        });

        cancelLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Close the LogoutActivity and return to the previous screen
            }
        });
    }
}
