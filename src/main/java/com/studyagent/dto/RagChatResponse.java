package com.studyagent.dto;

import java.util.List;
import com.studyagent.dto.RetrievedChunkResponse;

public class RagChatResponse {

    private String question;
    private String answer;
    private List<RetrievedChunkResponse> sources;

    public RagChatResponse(
            String question,
            String answer,
            List<RetrievedChunkResponse> sources
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

    public List<RetrievedChunkResponse> getSources() {
        return sources;
    }
}