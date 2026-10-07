package com.studyagent.demo.cet6;

import com.studyagent.dto.WeakPointSummaryResponse;

import java.util.List;

public class Cet6MiniProfileResponse {

    private int diagnosedQuestions;
    private List<WeakPointSummaryResponse> weakPoints;
    private String reviewSuggestion;

    public Cet6MiniProfileResponse(
            int diagnosedQuestions,
            List<WeakPointSummaryResponse> weakPoints,
            String reviewSuggestion
    ) {
        this.diagnosedQuestions = diagnosedQuestions;
        this.weakPoints = weakPoints;
        this.reviewSuggestion = reviewSuggestion;
    }

    public int getDiagnosedQuestions() {
        return diagnosedQuestions;
    }

    public List<WeakPointSummaryResponse> getWeakPoints() {
        return weakPoints;
    }

    public String getReviewSuggestion() {
        return reviewSuggestion;
    }
}