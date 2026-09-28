package com.studyagent.dto;

import java.time.LocalDateTime;

public class KnowledgeDocumentResponse {

    private Long id;
    private String originalFilename;
    private Long fileSize;
    private String contentType;
    private LocalDateTime createdAt;

    public KnowledgeDocumentResponse(
            Long id,
            String originalFilename,
            Long fileSize,
            String contentType,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.originalFilename = originalFilename;
        this.fileSize = fileSize;
        this.contentType = contentType;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public String getContentType() {
        return contentType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}