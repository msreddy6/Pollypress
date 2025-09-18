package com.example.pollypress.model;

import java.util.List;

public class PollModel {
    private Long id;
    private String title;
    private String description;
    private List<String> options;
    private String createdAt;

    public PollModel() {}

    public PollModel(Long id, String title, String description, List<String> options, String createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.options = options;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<String> getOptions() { return options; }
    public void setOptions(List<String> options) { this.options = options; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
