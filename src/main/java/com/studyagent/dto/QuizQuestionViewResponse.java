package com.studyagent.dto;

public class QuizQuestionViewResponse {

    private Long questionId;
    private String question;

    public QuizQuestionViewResponse(
            Long questionId,
            String question
    ) {
        this.questionId = questionId;
        this.question = question;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public String getQuestion() {
        return question;
    }
}