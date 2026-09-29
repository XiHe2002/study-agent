package com.studyagent.dto;

import java.time.LocalDateTime;
import java.util.List;

public class QuizAttemptResponse {

    private Long attemptId;
    private Long questionId;
    private String question;
    private String userAnswer;
    private Integer score;
    private String feedback;
    private List<String> weakPoints;
    private LocalDateTime createdAt;

    public QuizAttemptResponse(
            Long attemptId,
            Long questionId,
            String question,
            String userAnswer,
            Integer score,
            String feedback,
            List<String> weakPoints,
            LocalDateTime createdAt
    ) {
        this.attemptId = attemptId;
        this.questionId = questionId;
        this.question = question;
        this.userAnswer = userAnswer;
        this.score = score;
        this.feedback = feedback;
        this.weakPoints = weakPoints;
        this.createdAt = createdAt;
    }

    public Long getAttemptId() {
        return attemptId;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public String getQuestion() {
        return question;
    }

    public String getUserAnswer() {
        return userAnswer;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}