package com.studyagent.dto;

public class DocumentUploadResponse {

    private Long documentId;
    private String originalFilename;
    private long size;
    private String contentType;
    private int chunkCount;
    private int embeddingDimension;

    public DocumentUploadResponse(
            Long documentId,
            String originalFilename,
            long size,
            String contentType,
            int chunkCount,
            int embeddingDimension
    ) {
        this.documentId = documentId;
        this.originalFilename = originalFilename;
        this.size = size;
        this.contentType = contentType;
        this.chunkCount = chunkCount;
        this.embeddingDimension = embeddingDimension;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public long getSize() {
        return size;
    }

    public String getContentType() {
        return contentType;
    }

    public int getChunkCount() {
        return chunkCount;
    }

    public int getEmbeddingDimension() {
        return embeddingDimension;
    }
}