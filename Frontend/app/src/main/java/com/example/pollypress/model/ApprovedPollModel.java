package com.example.pollypress.model;

import java.util.List;

public class ApprovedPollModel {
    private Long id;
    private String title;
    private String description;
    private List<String> options;
    private String approvedAt;

    public ApprovedPollModel() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<String> getOptions() { return options; }
    public void setOptions(List<String> options) { this.options = options; }

    public String getApprovedAt() { return approvedAt; }
    public void setApprovedAt(String approvedAt) { this.approvedAt = approvedAt; }

}
