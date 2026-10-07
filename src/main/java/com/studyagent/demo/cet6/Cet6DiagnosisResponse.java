package com.studyagent.demo.cet6;

import java.util.List;

public class Cet6DiagnosisResponse {

    private Integer examQuestionNo;
    private String userAnswer;
    private String referenceAnswer;
    private int score;
    private String feedback;
    private List<String> weakPoints;
    private List<String> weakPointTags;
    private String answerSource;

    public Cet6DiagnosisResponse(
            Integer examQuestionNo,
            String userAnswer,
            String referenceAnswer,
            int score,
            String feedback,
            List<String> weakPoints,
            List<String> weakPointTags,
            String answerSource
    ) {
        this.examQuestionNo = examQuestionNo;
        this.userAnswer = userAnswer;
        this.referenceAnswer = referenceAnswer;
        this.score = score;
        this.feedback = feedback;
        this.weakPoints = weakPoints;
        this.weakPointTags = weakPointTags;
        this.answerSource = answerSource;
    }

    public Integer getExamQuestionNo() {
        return examQuestionNo;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public String getReferenceAnswer() {
        return referenceAnswer;
    }

    public int getScore() {
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

    public String getAnswerSource() {
        return answerSource;
    }
}