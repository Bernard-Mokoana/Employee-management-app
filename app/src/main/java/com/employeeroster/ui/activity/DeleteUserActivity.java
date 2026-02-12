package com.employeeroster.ui.activity;


import com.employeeroster.R;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class DeleteUserActivity extends AppCompatActivity {

    private EditText staffNumberInput;
    private Button deleteUserButton;
    private FirebaseFirestore db;
    private static final String TAG = "DeleteUserActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_user);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Bind UI elements
        staffNumberInput = findViewById(R.id.staff_number_input);
        deleteUserButton = findViewById(R.id.delete_user_button);

        // Set up delete button listener
        deleteUserButton.setOnClickListener(v -> deleteUser());
    }

    private void deleteUser() {
        String staffNumber = staffNumberInput.getText().toString().trim();

        if (TextUtils.isEmpty(staffNumber)) {
            Toast.makeText(this, "Please enter a staff number to delete the user.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Fetch user details from Firestore based on staff number
        db.collection("Users").whereEqualTo("staffNumber", staffNumber).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (!querySnapshot.isEmpty()) {
                            for (QueryDocumentSnapshot document : querySnapshot) {
                                deleteUserFromFirestore(document.getId());
                            }
                        } else {
                            Toast.makeText(DeleteUserActivity.this, "No user found with the given staff number.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(DeleteUserActivity.this, "Failed to retrieve user data: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Error fetching user data", task.getException());
                    }
                });
    }

    private void deleteUserFromFirestore(String userId) {
        db.collection("Users").document(userId)
                .delete()
                .addOnSuccessListener(aVoid -> Toast.makeText(DeleteUserActivity.this, "User deleted successfully.", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> {
                    Toast.makeText(DeleteUserActivity.this, "Failed to delete user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error deleting user", e);
                });
    }
}

