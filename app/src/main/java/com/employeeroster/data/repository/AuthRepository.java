package com.employeeroster.data.repository;

import com.employeeroster.util.Result;
import com.employeeroster.util.ResultCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AuthRepository {
    private final FirebaseAuth auth;
    private final FirebaseFirestore db;

    public AuthRepository() {
        this.auth = FirebaseAuth.getInstance();
        this.db = FirebaseFirestore.getInstance();
    }

    public void login(String email, String password, ResultCallback<String> callback) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        String message = task.getException() != null ? task.getException().getMessage() : "Login failed";
                        callback.onComplete(Result.error(message));
                        return;
                    }

                    FirebaseUser user = auth.getCurrentUser();
                    if (user == null) {
                        callback.onComplete(Result.error("User is not available"));
                        return;
                    }

                    fetchUserRole(user.getUid(), callback);
                });
    }

    private void fetchUserRole(String userId, ResultCallback<String> callback) {
        db.collection("Users").document(userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        String message = task.getException() != null ? task.getException().getMessage() : "Failed to retrieve role";
                        callback.onComplete(Result.error(message));
                        return;
                    }

                    DocumentSnapshot document = task.getResult();
                    if (document == null || !document.exists()) {
                        callback.onComplete(Result.error("User document does not exist"));
                        return;
                    }

                    String role = document.getString("jobRole");
                    if (role == null) {
                        callback.onComplete(Result.error("Role not found in Firestore"));
                        return;
                    }

                    callback.onComplete(Result.success(role));
                });
    }
}
