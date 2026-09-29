package com.studyagent.dto;

public class QuizQuestionResponse {

    private String question;
    private String referenceAnswer;

    public QuizQuestionResponse() {
    }

    public QuizQuestionResponse(
            String question,
            String referenceAnswer
    ) {
        this.question = question;
        this.referenceAnswer = referenceAnswer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getReferenceAnswer() {
        return referenceAnswer;
    }

    public void setReferenceAnswer(String referenceAnswer) {
        this.referenceAnswer = referenceAnswer;
    }
}