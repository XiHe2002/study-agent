package com.studyagent.dto;

import java.util.List;

public class QuizReviewResponse {

    private Long attemptId;
    private Long questionId;
    private List<String> weakPoints;
    private String explanation;
    private List<RetrievedChunkResponse> sources;

    public QuizReviewResponse(
            Long attemptId,
            Long questionId,
            List<String> weakPoints,
            String explanation,
            List<RetrievedChunkResponse> sources
    ) {
        this.attemptId = attemptId;
        this.questionId = questionId;
        this.weakPoints = weakPoints;
        this.explanation = explanation;
        this.sources = sources;
    }

    public Long getAttemptId() {
        return attemptId;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public List<String> getWeakPoints() {
        return weakPoints;
    }

    public String getExplanation() {
        return explanation;
    }

    public List<RetrievedChunkResponse> getSources() {
        return sources;
    }
}