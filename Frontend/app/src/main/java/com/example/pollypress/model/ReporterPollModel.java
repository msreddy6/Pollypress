package com.example.pollypress.model;

import java.util.List;

public class ReporterPollModel {
    private Long id;
    private String title;
    private String description;
    private List<String> options;
    private String createdAt;
    private String status;

    public ReporterPollModel(Long id, String title, String description, List<String> options, String createdAt, String status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.options = options;
        this.createdAt = createdAt;
        this.status = status;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public List<String> getOptions() { return options; }
    public String getCreatedAt() { return createdAt; }
    public String getStatus() { return status; }
}
