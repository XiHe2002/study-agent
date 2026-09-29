package com.studyagent.dto;

import java.util.List;

public class QuizGenerateResponse {

    private String topic;
    private List<QuizQuestionResponse> questions;
    private List<RetrievedChunkResponse> sources;
    private Long quizId;

    public QuizGenerateResponse(
            Long quizId,
            String topic,
            List<QuizQuestionResponse> questions,
            List<RetrievedChunkResponse> sources
    ) {
        this.quizId = quizId;
        this.topic = topic;
        this.questions = questions;
        this.sources = sources;
    }

    public String getTopic() {
        return topic;
    }

    public List<QuizQuestionResponse> getQuestions() {
        return questions;
    }

    public List<RetrievedChunkResponse> getSources() {
        return sources;
    }

    public Long getQuizId() {
        return quizId;
    }

}