package com.studyagent.dto;

import java.util.List;

public class QuizSubmitResponse {

    private Long attemptId;
    private Long questionId;
    private Integer score;
    private String feedback;
    private List<String> weakPoints;
    private List<String> weakPointTags;

    public QuizSubmitResponse(
            Long attemptId,
            Long questionId,
            Integer score,
            String feedback,
            List<String> weakPoints,
            List<String> weakPointTags
    ) {
        this.attemptId = attemptId;
        this.questionId = questionId;
        this.score = score;
        this.feedback = feedback;
        this.weakPoints = weakPoints;
        this.weakPointTags = weakPointTags;
    }

    public Long getAttemptId() {
        return attemptId;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public Integer getScore() {
        return score;
    }

    public String getFeedback() {
        return feedback;
    }

    public List<String> getWeakPoints() {
        return weakPoints;
    }

    public List<String> getWeakPointTags() {
        return weakPointTags;
    }
}