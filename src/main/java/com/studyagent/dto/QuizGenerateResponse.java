package com.studyagent.dto;

import java.util.List;

public class QuizGenerateResponse {

    private String topic;
    private List<QuizQuestionViewResponse> questions;
    private List<RetrievedChunkResponse> sources;
    private Long quizId;

    public QuizGenerateResponse(
            Long quizId,
            String topic,
            List<QuizQuestionViewResponse> questions,
            List<RetrievedChunkResponse> sources
    ) {
        this.quizId = quizId;
        this.topic = topic;
        this.questions = questions;
        this.sources = sources;
    }

    public List<QuizQuestionViewResponse> getQuestions() {
        return questions;
    }

    public String getTopic() {
        return topic;
    }


    public List<RetrievedChunkResponse> getSources() {
        return sources;
    }

    public Long getQuizId() {
        return quizId;
    }

}