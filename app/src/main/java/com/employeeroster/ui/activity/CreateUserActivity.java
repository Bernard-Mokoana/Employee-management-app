package com.employeeroster.ui.activity;


import com.employeeroster.R;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.employeeroster.viewmodel.CreateUserViewModel;

public class CreateUserActivity extends AppCompatActivity {

    private EditText firstNameInput, lastNameInput, idNumberInput, emailInput, passwordInput, contactNumberInput;
    private Spinner roleSpinner;
    private Button createUserButton, editUserButton, deleteUserButton;
    private String selectedRole;
    private ProgressDialog progressDialog;
    private CreateUserViewModel viewModel;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        // Initialize ProgressDialog
        progressDialog = new ProgressDialog(this);

        // Bind UI elements
        firstNameInput = findViewById(R.id.first_name_input);
        lastNameInput = findViewById(R.id.last_name_input);
        idNumberInput = findViewById(R.id.id_number_input);
        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);
        contactNumberInput = findViewById(R.id.contact_number_input);
        roleSpinner = findViewById(R.id.role_spinner);
        createUserButton = findViewById(R.id.create_user_button);
        editUserButton = findViewById(R.id.edit_user_button);
        deleteUserButton = findViewById(R.id.delete_user_button);

        // Set up the role spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.user_roles, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(adapter);

        roleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedRole = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedRole = ""; // Default value if nothing is selected
            }
        });

        viewModel = new ViewModelProvider(this).get(CreateUserViewModel.class);
        viewModel.getLoading().observe(this, isLoading -> {
            if (Boolean.TRUE.equals(isLoading)) {
                progressDialog.setMessage("Creating user...");
                progressDialog.show();
            } else {
                progressDialog.dismiss();
            }
        });
        viewModel.getErrorMessage().observe(this, message -> {
            if (message != null) {
                Toast.makeText(CreateUserActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
        viewModel.getSuccessMessage().observe(this, message -> {
            if (message != null) {
                Toast.makeText(CreateUserActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });

        // Set up create user button
        createUserButton.setOnClickListener(v -> {
            String firstName = firstNameInput.getText().toString().trim();
            String lastName = lastNameInput.getText().toString().trim();
            String idNumber = idNumberInput.getText().toString().trim();
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();
            String contactNumber = contactNumberInput.getText().toString().trim();

            if (selectedRole.isEmpty()) {
                Toast.makeText(CreateUserActivity.this, "Please select a role", Toast.LENGTH_SHORT).show();
                return;
            }

            viewModel.createUser(firstName, lastName, idNumber, email, password, selectedRole, contactNumber);
        });

        // Set up edit user button
        editUserButton.setOnClickListener(v -> {
            Intent intent = new Intent(CreateUserActivity.this, AdminUpdateUserActivity.class);
            startActivity(intent);
        });

        // Set up delete user button
        deleteUserButton.setOnClickListener(v -> {
         Intent intent = new Intent(CreateUserActivity.this, DeleteUserActivity.class);
         startActivity(intent);
        });
    }
}

