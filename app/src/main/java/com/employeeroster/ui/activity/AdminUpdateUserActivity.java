package com.employeeroster.ui.activity;


import com.employeeroster.R;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AdminUpdateUserActivity extends AppCompatActivity {

    private EditText staffNumberInput, firstNameInput, lastNameInput, contactNumberInput, idNumberInput, emailInput;
    private Spinner roleSpinner;
    private Button searchUserButton, updateUserButton;

    private FirebaseFirestore db;
    private static final String TAG = "AdminUpdateUserActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_update_user);

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Bind UI elements
        staffNumberInput = findViewById(R.id.staff_number_input);
        searchUserButton = findViewById(R.id.search_user_button);
        firstNameInput = findViewById(R.id.first_name_input);
        lastNameInput = findViewById(R.id.last_name_input);
        contactNumberInput = findViewById(R.id.contact_number_input);
        idNumberInput = findViewById(R.id.id_number_input);
        emailInput = findViewById(R.id.email_input);
        roleSpinner = findViewById(R.id.role_spinner);
        updateUserButton = findViewById(R.id.update_user_button);

        // Set up the Spinner adapter
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.user_roles, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(adapter);

        // Set up button listeners
        searchUserButton.setOnClickListener(v -> searchUser());
        updateUserButton.setOnClickListener(v -> updateUser());
    }

    private void searchUser() {
        String staffNumber = staffNumberInput.getText().toString().trim();
        if (TextUtils.isEmpty(staffNumber)) {
            Toast.makeText(this, "Please enter a staff number to search.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Fetch user details from Firestore based on staff number
        db.collection("Users").whereEqualTo("staffNumber", staffNumber).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);
                        populateUserDetails(document);
                    } else {
                        Toast.makeText(this, "No user found with the given staff number.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error retrieving user data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error fetching user data", e);
                });
    }

    private void populateUserDetails(DocumentSnapshot document) {
        firstNameInput.setText(document.getString("firstName"));
        lastNameInput.setText(document.getString("lastName"));
        contactNumberInput.setText(document.getString("contactNumber"));
        idNumberInput.setText(document.getString("idNumber"));
        emailInput.setText(document.getString("email"));
        String role = document.getString("jobRole");
        if (role != null) {
            setSpinnerSelection(roleSpinner, role);
        }
    }

    private void setSpinnerSelection(Spinner spinner, String value) {
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinner.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).equals(value)) {
                spinner.setSelection(i);
                break;
            }
        }
    }

    private void updateUser() {
        String staffNumber = staffNumberInput.getText().toString().trim();
        if (TextUtils.isEmpty(staffNumber)) {
            Toast.makeText(this, "Staff number is required to update the user.", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> updatedUserData = new HashMap<>();
        updatedUserData.put("firstName", firstNameInput.getText().toString().trim());
        updatedUserData.put("lastName", lastNameInput.getText().toString().trim());
        updatedUserData.put("contactNumber", contactNumberInput.getText().toString().trim());
        updatedUserData.put("idNumber", idNumberInput.getText().toString().trim());
        updatedUserData.put("email", emailInput.getText().toString().trim());
        updatedUserData.put("jobRole", roleSpinner.getSelectedItem().toString());
        updatedUserData.put("staffNumber", staffNumber);

        db.collection("Users").whereEqualTo("staffNumber", staffNumber).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        String userId = queryDocumentSnapshots.getDocuments().get(0).getId();
                        db.collection("Users").document(userId).update(updatedUserData)
                                .addOnSuccessListener(aVoid -> Toast.makeText(AdminUpdateUserActivity.this, "User details updated successfully.", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e -> Toast.makeText(AdminUpdateUserActivity.this, "Error updating user: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(AdminUpdateUserActivity.this, "Error updating user data: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}

