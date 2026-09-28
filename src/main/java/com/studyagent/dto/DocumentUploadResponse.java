package com.studyagent.dto;

import java.util.List;

public class DocumentUploadResponse {

    private String originalFilename;
    private String storedFilename;
    private long size;
    private String contentType;
    private String content;
    private List<String> chunks;
    private int embeddingCount;
    private int embeddingDimension;

    public DocumentUploadResponse(
            String originalFilename,
            String storedFilename,
            long size,
            String contentType,
            String content,
            List<String> chunks,
            int embeddingCount,
            int embeddingDimension
    ) {
        this.originalFilename = originalFilename;
        this.storedFilename = storedFilename;
        this.size = size;
        this.contentType = contentType;
        this.content = content;
        this.chunks = chunks;
        this.embeddingCount = embeddingCount;
        this.embeddingDimension = embeddingDimension;
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
    public List<String> getChunks() {
        return chunks;
    }
    public int getEmbeddingCount() {
        return embeddingCount;
    }

    public int getEmbeddingDimension() {
        return embeddingDimension;
    }
}