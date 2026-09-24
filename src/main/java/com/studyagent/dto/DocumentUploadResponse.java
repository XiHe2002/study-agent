package com.studyagent.dto;

public class DocumentUploadResponse {

    private String originalFilename;
    private String storedFilename;
    private long size;
    private String contentType;
    private String content;

    public DocumentUploadResponse(
            String originalFilename,
            String storedFilename,
            long size,
            String contentType,
            String content
    ) {
        this.originalFilename = originalFilename;
        this.storedFilename = storedFilename;
        this.size = size;
        this.contentType = contentType;
        this.content = content;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public String getStoredFilename() {
        return storedFilename;
    }

    public long getSize() {
        return size;
    }

    public String getContentType() {
        return contentType;
    }

    public String getContent() {
        return content;
    }
}