package com.employeeroster;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.List;

public class LeaveRequestAdapter extends RecyclerView.Adapter<LeaveRequestAdapter.ViewHolder> {

    private Context context;
    private List<LeaveRequest> leaveRequests;
    private DatabaseReference leaveRequestDatabaseReference;

    public LeaveRequestAdapter(Context context, List<LeaveRequest> leaveRequests) {
        this.context = context;
        this.leaveRequests = leaveRequests;
        leaveRequestDatabaseReference = FirebaseDatabase.getInstance().getReference("LeaveRequests");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.leave_request_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LeaveRequest request = leaveRequests.get(position);
        holder.leaveTypeTextView.setText(request.getLeaveType());
        holder.startDateTextView.setText(request.getStartDate());
        holder.endDateTextView.setText(request.getEndDate());
        holder.notesTextView.setText(request.getNotes());

        holder.approveButton.setOnClickListener(v -> updateRequestStatus(request.getRequestId(), "approved", "Manager"));
        holder.rejectButton.setOnClickListener(v -> updateRequestStatus(request.getRequestId(), "rejected", "Manager"));
    }

    @Override
    public int getItemCount() {
        return leaveRequests.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView leaveTypeTextView, startDateTextView, endDateTextView, notesTextView;
        public Button approveButton, rejectButton;

        public ViewHolder(View itemView) {
            super(itemView);
            leaveTypeTextView = itemView.findViewById(R.id.leave_type_text_view);
            startDateTextView = itemView.findViewById(R.id.start_date_text_view);
            endDateTextView = itemView.findViewById(R.id.end_date_text_view);
            notesTextView = itemView.findViewById(R.id.notes_text_view);
            approveButton = itemView.findViewById(R.id.approve_button);
            rejectButton = itemView.findViewById(R.id.reject_button);
        }
    }

    private void updateRequestStatus(String requestId, String status, String approvedBy) {
        leaveRequestDatabaseReference.child(requestId).child("status").setValue(status);
        leaveRequestDatabaseReference.child(requestId).child("approvedBy").setValue(approvedBy)
                .addOnSuccessListener(aVoid -> Toast.makeText(context, "Request " + status, Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(context, "Failed to update status", Toast.LENGTH_SHORT).show());
    }
}
