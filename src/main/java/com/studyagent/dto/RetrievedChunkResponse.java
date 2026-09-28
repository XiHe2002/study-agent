package com.studyagent.dto;

public class RetrievedChunkResponse {

    private Long documentId;
    private String filename;
    private Integer chunkIndex;
    private double score;
    private String content;

    public RetrievedChunkResponse(
            Long documentId,
            String filename,
            Integer chunkIndex,
            double score,
            String content
    ) {
        this.documentId = documentId;
        this.filename = filename;
        this.chunkIndex = chunkIndex;
        this.score = score;
        this.content = content;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public String getFilename() {
        return filename;
    }

    public Integer getChunkIndex() {
        return chunkIndex;
    }

    public double getScore() {
        return score;
    }

    public String getContent() {
        return content;
    }
}