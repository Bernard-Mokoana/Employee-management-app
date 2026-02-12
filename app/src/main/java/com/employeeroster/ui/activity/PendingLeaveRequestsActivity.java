package com.employeeroster.ui.activity;


import com.employeeroster.R;
import com.employeeroster.data.model.LeaveRequest;
import com.employeeroster.ui.adapter.LeaveRequestAdapter;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PendingLeaveRequestsActivity extends AppCompatActivity {

    private RecyclerView leaveRequestsRecyclerView;
    private DatabaseReference leaveRequestDatabaseReference;
    private List<LeaveRequest> pendingRequests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_leave_requests);

        leaveRequestsRecyclerView = findViewById(R.id.leave_requests_recycler_view);
        leaveRequestsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        leaveRequestDatabaseReference = FirebaseDatabase.getInstance().getReference("LeaveRequests");

        // Load pending leave requests
        loadPendingLeaveRequests();
    }

    private void loadPendingLeaveRequests() {
        leaveRequestDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pendingRequests = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    LeaveRequest request = snapshot.getValue(LeaveRequest.class);
                    if (request != null && "pending".equals(request.getStatus())) {
                        pendingRequests.add(request);
                    }
                }

                LeaveRequestAdapter adapter = new LeaveRequestAdapter(PendingLeaveRequestsActivity.this, pendingRequests);
                leaveRequestsRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(PendingLeaveRequestsActivity.this, "Failed to load leave requests: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

