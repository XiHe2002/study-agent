package com.studyagent.dto;

import java.time.LocalDateTime;

public class LearningRecordResponse {

    private Long id;
    private String topic;
    private String description;
    private LocalDateTime createdAt;

    public LearningRecordResponse(
            Long id,
            String topic,
            String description,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.topic = topic;
        this.description = description;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getTopic() {
        return topic;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}