package com.employeeroster.data.repository;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.employeeroster.util.Result;
import com.employeeroster.util.ResultCallback;

public class UserRepository {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    public UserRepository() {
        this.mAuth = FirebaseAuth.getInstance();
        this.db = FirebaseFirestore.getInstance();
    }

    // Create Employee Profile
    public void createNewUser(String firstName, String lastName, String idNumber, String email, String password, String jobRole, String contactNumber, ResultCallback<Void> callback) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            String userId = firebaseUser.getUid();
                            saveUserToFirestore(userId, firstName, lastName, idNumber, email, jobRole, contactNumber, callback);
                        } else {
                            callback.onComplete(Result.error("Failed to retrieve user details"));
                        }
                    } else {
                        String message = task.getException() != null ? task.getException().getMessage() : "Failed to create user";
                        callback.onComplete(Result.error(message));
                    }
                });
    }

    // Edit Employee Profile
    public void editUserProfile(String userId, String firstName, String lastName, String idNumber, String email, String jobRole, String contactNumber, ResultCallback<Void> callback) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("firstName", firstName);
        userMap.put("lastName", lastName);
        userMap.put("idNumber", idNumber);
        userMap.put("email", email);
        userMap.put("jobRole", jobRole);
        userMap.put("contactNumber", contactNumber);

        db.collection("Users").document(userId)
                .update(userMap)
                .addOnSuccessListener(aVoid -> callback.onComplete(Result.success(null)))
                .addOnFailureListener(e -> callback.onComplete(Result.error(e.getMessage())));
    }

    // Delete Employee Profile
    public void deleteUserProfile(String userId, ResultCallback<Void> callback) {
        db.collection("Users").document(userId)
                .delete()
                .addOnSuccessListener(aVoid -> callback.onComplete(Result.success(null)))
                .addOnFailureListener(e -> callback.onComplete(Result.error(e.getMessage())));
    }

    private void saveUserToFirestore(String userId, String firstName, String lastName, String idNumber, String email, String jobRole, String contactNumber, ResultCallback<Void> callback) {
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
                .addOnSuccessListener(aVoid -> callback.onComplete(Result.success(null)))
                .addOnFailureListener(e -> callback.onComplete(Result.error(e.getMessage())));
    }

    private String generateStaffNumber() {
        Random random = new Random();
        int staffNumber = 100000 + random.nextInt(900000); // Generates a random 6-digit number
        return String.valueOf(staffNumber);
    }
}
