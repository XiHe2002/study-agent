package com.studyagent.dto;

import java.util.List;

public class StudyProfileResponse {

    private Integer totalAttempts;
    private Integer attemptedQuestions;
    private Integer masteredQuestions;
    private Integer unresolvedQuestions;
    private List<String> currentWeakPoints;
    private String reviewSuggestion;
    private List<WeakPointSummaryResponse> historicalWeakPoints;

    public StudyProfileResponse(
            Integer totalAttempts,
            Integer attemptedQuestions,
            Integer masteredQuestions,
            Integer unresolvedQuestions,
            List<String> currentWeakPoints,
            List<WeakPointSummaryResponse> historicalWeakPoints,
            String reviewSuggestion
    ) {
        this.totalAttempts = totalAttempts;
        this.attemptedQuestions = attemptedQuestions;
        this.masteredQuestions = masteredQuestions;
        this.unresolvedQuestions = unresolvedQuestions;
        this.currentWeakPoints = currentWeakPoints;
        this.historicalWeakPoints = historicalWeakPoints;
        this.reviewSuggestion = reviewSuggestion;
    }

    public Integer getTotalAttempts() {
        return totalAttempts;
    }

    public Integer getAttemptedQuestions() {
        return attemptedQuestions;
    }

    public Integer getMasteredQuestions() {
        return masteredQuestions;
    }

    public Integer getUnresolvedQuestions() {
        return unresolvedQuestions;
    }

    public List<String> getCurrentWeakPoints() {
        return currentWeakPoints;
    }

    public String getReviewSuggestion() {
        return reviewSuggestion;
    }

    public List<WeakPointSummaryResponse> getHistoricalWeakPoints() {
        return historicalWeakPoints;
    }
}