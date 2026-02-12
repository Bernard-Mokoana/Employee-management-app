package com.employeeroster.ui.activity;


import com.employeeroster.R;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.employeeroster.viewmodel.LoginViewModel;

public class LoginActivity extends AppCompatActivity {

    private EditText emailInput, passwordInput;
    private Button loginButton;
    private Button forgotPasswordButton;
    private LoginViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Find UI elements
        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);
        loginButton = findViewById(R.id.login_button);
        forgotPasswordButton = findViewById(R.id.forgot_password_button);

        loginButton.setOnClickListener(v -> loginUser());

        forgotPasswordButton.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class)));

        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        viewModel.getLoading().observe(this, isLoading -> loginButton.setEnabled(!Boolean.TRUE.equals(isLoading)));
        viewModel.getErrorMessage().observe(this, message -> {
            if (message != null) {
                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
        viewModel.getUserRole().observe(this, role -> {
            if (role != null) {
                navigateToRoleActivity(role);
            }
        });

    }

    private void loginUser() {
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();
        viewModel.login(email, password);
    }

    private void navigateToRoleActivity(String role) {
        Intent intent;
        switch (role.toLowerCase()) {
            case "admin":
                intent = new Intent(LoginActivity.this, AdminActivity.class);
                break;
            case "manager":
                intent = new Intent(LoginActivity.this, ManagerActivity.class);
                break;
            case "general stuff":
                intent = new Intent(LoginActivity.this, GeneralWorkerActivity.class);
                break;
            default:
                Toast.makeText(LoginActivity.this, "Invalid role", Toast.LENGTH_SHORT).show();
                return;
        }
        startActivity(intent);
        finish();
    }
}

