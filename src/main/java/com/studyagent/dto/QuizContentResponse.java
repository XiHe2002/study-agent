package com.studyagent.dto;

import java.util.List;

public class QuizContentResponse {

    private List<QuizQuestionResponse> questions;

    public QuizContentResponse() {
    }

    public List<QuizQuestionResponse> getQuestions() {
        return questions;
    }

    public void setQuestions(
            List<QuizQuestionResponse> questions
    ) {
        this.questions = questions;
    }
}