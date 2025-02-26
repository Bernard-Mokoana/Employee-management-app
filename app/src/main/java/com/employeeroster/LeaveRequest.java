package com.employeeroster;

public class LeaveRequest {
    private String requestId;
    private String leaveType;
    private String startDate;
    private String endDate;
    private String notes;
    private String status; // Can be "pending", "approved", "rejected"
    private String approvedBy;

    // Required empty constructor for Firebase
    public LeaveRequest() {}

    public LeaveRequest(String requestId, String leaveType, String startDate, String endDate, String notes) {
        this.requestId = requestId;
        this.leaveType = leaveType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.notes = notes;
        this.status = "pending"; // Default status
        this.approvedBy = null;   // Initially no one has approved/rejected
    }

    public String getRequestId() {
        return requestId;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getNotes() {
        return notes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }
}
