package com.studyagent.dto;

import java.util.List;

public class AnswerAnalysisResponse {

    private Integer score;
    private String feedback;
    private List<String> weakPoints;

    public AnswerAnalysisResponse() {
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public List<String> getWeakPoints() {
        return weakPoints;
    }

    public void setWeakPoints(List<String> weakPoints) {
        this.weakPoints = weakPoints;
    }
}