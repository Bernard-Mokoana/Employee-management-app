package com.employeeroster.data.repository;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AttendanceRepository {

    private final DatabaseReference attendanceDatabaseReference;

    public AttendanceRepository() {
        attendanceDatabaseReference = FirebaseDatabase.getInstance().getReference("Attendance");
    }

    /**
     * Method to record clock-in details in the Firebase database.
     */
    public void recordClockIn(Context context, String staffNumber, String role) {
        String currentTime = getCurrentDateTime();
        Map<String, String> attendanceData = new HashMap<>();
        attendanceData.put("staffNumber", staffNumber);
        attendanceData.put("role", role);
        attendanceData.put("clockInTime", currentTime);

        // Push data to Firebase under a unique key for each entry
        attendanceDatabaseReference.child(staffNumber).push().setValue(attendanceData)
                .addOnSuccessListener(aVoid -> Toast.makeText(context, "Clock-in recorded successfully", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(context, "Failed to record clock-in: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    /**
     * Method to record clock-out details in the Firebase database.
     */
    public void recordClockOut(Context context, String staffNumber, String role) {
        String currentTime = getCurrentDateTime();
        Map<String, String> attendanceData = new HashMap<>();
        attendanceData.put("staffNumber", staffNumber);
        attendanceData.put("role", role);
        attendanceData.put("clockOutTime", currentTime);

        // Push data to Firebase under a unique key for each entry
        attendanceDatabaseReference.child(staffNumber).push().setValue(attendanceData)
                .addOnSuccessListener(aVoid -> Toast.makeText(context, "Clock-out recorded successfully", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(context, "Failed to record clock-out: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    /**
     * Helper method to get the current date and time in a specific format.
     */
    private String getCurrentDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(new Date());
    }
}
