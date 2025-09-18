package com.example.pollypress.model;

public class TicketModel {
    private Long id;
    private boolean resolved;
    private String createdAt;
    private String resolvedAt;

    // nested reporter/admin IDs & usernames (for display)
    private Long reporterId;
    private String reporterUsername;
    private Long adminId;
    private String adminUsername;

    // constructors, getters, setters...
    public TicketModel() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public boolean isResolved() { return resolved; }
    public void setResolved(boolean resolved) { this.resolved = resolved; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getResolvedAt() { return resolvedAt; }
    public void setResolvedAt(String resolvedAt) { this.resolvedAt = resolvedAt; }

    public Long getReporterId() { return reporterId; }
    public void setReporterId(Long reporterId) { this.reporterId = reporterId; }
    public String getReporterUsername() { return reporterUsername; }
    public void setReporterUsername(String reporterUsername) { this.reporterUsername = reporterUsername; }

    public Long getAdminId() { return adminId; }
    public void setAdminId(Long adminId) { this.adminId = adminId; }
    public String getAdminUsername() { return adminUsername; }
    public void setAdminUsername(String adminUsername) { this.adminUsername = adminUsername; }
}
