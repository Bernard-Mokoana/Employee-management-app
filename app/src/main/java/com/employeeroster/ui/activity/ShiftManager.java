package com.employeeroster.ui.activity;


import com.employeeroster.R;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class ShiftManager extends AppCompatActivity {

    private EditText shiftNameInput, startTimeInput, endTimeInput;
    private Button addShiftButton, deleteShiftButton;
    private ListView shiftsListView;
    private DatabaseReference shiftsDatabaseReference;
    private List<Shift> shiftsList;
    private Shift selectedShift;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shift_manager);

        // Initialize Firebase Database reference
        shiftsDatabaseReference = FirebaseDatabase.getInstance().getReference("Shifts");

        // Bind UI elements
        shiftNameInput = findViewById(R.id.shift_name_input);
        startTimeInput = findViewById(R.id.start_time_input);
        endTimeInput = findViewById(R.id.end_time_input);
        addShiftButton = findViewById(R.id.add_shift_button);
        deleteShiftButton = findViewById(R.id.delete_shift_button);
        shiftsListView = findViewById(R.id.shifts_list_view);
        shiftsList = new ArrayList<>();

        // Load shifts from Firebase
        loadShifts();

        // Set up add shift button
        addShiftButton.setOnClickListener(v -> {
            String shiftName = shiftNameInput.getText().toString().trim();
            String startTime = startTimeInput.getText().toString().trim();
            String endTime = endTimeInput.getText().toString().trim();

            if (shiftName.isEmpty() || startTime.isEmpty() || endTime.isEmpty()) {
                Toast.makeText(ShiftManager.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            addShift(shiftName, startTime, endTime);
        });

        // Set up delete shift button
        deleteShiftButton.setOnClickListener(v -> {
            if (selectedShift != null) {
                deleteShift(selectedShift.getShiftId());
            } else {
                Toast.makeText(ShiftManager.this, "Please select a shift to delete.", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle list item clicks to select a shift
        shiftsListView.setOnItemClickListener((parent, view, position, id) -> selectedShift = shiftsList.get(position));
    }

    private void loadShifts() {
        shiftsDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                shiftsList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Shift shift = snapshot.getValue(Shift.class);
                    if (shift != null) {
                        shift.setShiftId(snapshot.getKey()); // Store the shift ID for later use (e.g., deletion)
                        shiftsList.add(shift);
                    }
                }
                ArrayAdapter<Shift> adapter = new ArrayAdapter<>(ShiftManager.this, android.R.layout.simple_list_item_1, shiftsList);
                shiftsListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ShiftManager.this, "Failed to load shifts: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addShift(String shiftName, String startTime, String endTime) {
        String shiftId = shiftsDatabaseReference.push().getKey();
        if (shiftId != null) {
            Shift newShift = new Shift(shiftId, shiftName, startTime, endTime);
            shiftsDatabaseReference.child(shiftId).setValue(newShift)
                    .addOnSuccessListener(aVoid -> Toast.makeText(ShiftManager.this, "Shift added successfully", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(ShiftManager.this, "Failed to add shift: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }

    private void deleteShift(String shiftId) {
        shiftsDatabaseReference.child(shiftId).removeValue()
                .addOnSuccessListener(aVoid -> Toast.makeText(ShiftManager.this, "Shift deleted successfully", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(ShiftManager.this, "Failed to delete shift: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    // Shift class to represent each shift object
    public static class Shift {
        private String shiftId;
        private String shiftName;
        private String startTime;
        private String endTime;

        public Shift() {
            // Default constructor required for calls to DataSnapshot.getValue(Shift.class)
        }

        public Shift(String shiftId, String shiftName, String startTime, String endTime) {
            this.shiftId = shiftId;
            this.shiftName = shiftName;
            this.startTime = startTime;
            this.endTime = endTime;
        }

        public String getShiftId() {
            return shiftId;
        }

        public void setShiftId(String shiftId) {
            this.shiftId = shiftId;
        }

        public String getShiftName() {
            return shiftName;
        }

        public String getStartTime() {
            return startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        @Override
        public String toString() {
            return "Shift: " + shiftName + ", Start Time: " + startTime + ", End Time: " + endTime;
        }
    }
}

