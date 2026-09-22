package com.studyagent.dto;

public class StudyResponse {

    private String topic;
    private String description;
    private String message;

    public StudyResponse(String topic, String description, String message) {
        this.topic = topic;
        this.description = description;
        this.message = message;
    }

    public String getTopic() {
        return topic;
    }

    public String getDescription() {
        return description;
    }

    public String getMessage() {
        return message;
    }
}