package com.studyagent.dto;

import java.util.List;

public class RagChatResponse {

    private String question;
    private String answer;
    private List<String> sources;

    public RagChatResponse(
            String question,
            String answer,
            List<String> sources
    ) {
        this.question = question;
        this.answer = answer;
        this.sources = sources;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public List<String> getSources() {
        return sources;
    }
}