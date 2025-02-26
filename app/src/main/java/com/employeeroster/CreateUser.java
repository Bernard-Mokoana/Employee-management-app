package com.employeeroster;

import android.content.Context;
import android.util.Patterns;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CreateUser {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private Context context;

    public CreateUser(Context context) {
        this.mAuth = FirebaseAuth.getInstance();
        this.db = FirebaseFirestore.getInstance();
        this.context = context;
    }

    // Create Employee Profile
    public void createNewUser(String firstName, String lastName, String idNumber, String email, String password, String jobRole, String contactNumber) {
        if (!validateInput(firstName, lastName, idNumber, email, password, contactNumber)) {
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            String userId = firebaseUser.getUid();
                            saveUserToFirestore(userId, firstName, lastName, idNumber, email, jobRole, contactNumber);
                        }
                    } else {
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(context, "User with this email already exists!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Failed to create user: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // Edit Employee Profile
    public void editUserProfile(String userId, String firstName, String lastName, String idNumber, String email, String jobRole, String contactNumber) {
        if (!validateInput(firstName, lastName, idNumber, email, "", contactNumber)) {
            return;
        }

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("firstName", firstName);
        userMap.put("lastName", lastName);
        userMap.put("idNumber", idNumber);
        userMap.put("email", email);
        userMap.put("jobRole", jobRole);
        userMap.put("contactNumber", contactNumber);

        db.collection("Users").document(userId)
                .update(userMap)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "User profile updated successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Failed to update user profile: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    // Delete Employee Profile
    public void deleteUserProfile(String userId) {
        db.collection("Users").document(userId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "User profile deleted successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Failed to delete user profile: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private boolean validateInput(String firstName, String lastName, String idNumber, String email, String password, String contactNumber) {
        if (firstName.isEmpty() || lastName.isEmpty() || idNumber.isEmpty() || email.isEmpty() || (password != null && password.isEmpty()) || contactNumber.isEmpty()) {
            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(context, "Please enter a valid email", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (password != null && password.length() < 6) {
            Toast.makeText(context, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void saveUserToFirestore(String userId, String firstName, String lastName, String idNumber, String email, String jobRole, String contactNumber) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("firstName", firstName);
        userMap.put("lastName", lastName);
        userMap.put("idNumber", idNumber);
        userMap.put("email", email);
        userMap.put("jobRole", jobRole);
        userMap.put("contactNumber", contactNumber);
        userMap.put("staffNumber", generateStaffNumber());

        db.collection("Users").document(userId)
                .set(userMap)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "User created successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Failed to save user data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private String generateStaffNumber() {
        Random random = new Random();
        int staffNumber = 100000 + random.nextInt(900000); // Generates a random 6-digit number
        return String.valueOf(staffNumber);
    }
}
