package com.employeeroster.data.repository;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class StoreUserDetails {

    private static final String TAG = "StoreUserDetails";

    public static void storeUserDetails(String userId, String firstName, String lastName, String role, String email, String password, String idNumber, String staffNumber, Context context) {
        // Initialize Firestore instance
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if (userId == null || userId.isEmpty()) {
            Toast.makeText(context, "Invalid user ID", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("firstName", firstName);
        userDetails.put("lastName", lastName);
        userDetails.put("role", role);
        userDetails.put("email", email);
        userDetails.put("password", password);  // Storing passwords in plaintext is insecure, consider other options
        userDetails.put("idNumber", idNumber);
        userDetails.put("staffNumber", staffNumber);

        // Attempt to store user details in Firestore
        db.collection("Users")
                .document(userId)
                .set(userDetails)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "User details successfully saved.");
                    Toast.makeText(context, "User details saved.", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error saving user details: " + e.getMessage(), e);
                    Toast.makeText(context, "Error saving user details: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
