package com.employeeroster.data.repository;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RosterManager {

    public static void updateSchedule(String userId, String schedule, Context context) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> scheduleData = new HashMap<>();
        scheduleData.put("schedule", schedule);

        db.collection("roster").document(userId)
                .set(scheduleData)
                .addOnSuccessListener(aVoid -> Toast.makeText(context, "Schedule updated.", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(context, "Error updating schedule: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
