package com.studyagent.controller;

import com.studyagent.agent.StudyAgentAssistant;
import com.studyagent.dto.AgentChatRequest;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/agent")
public class AgentController {

    private final StudyAgentAssistant studyAgentAssistant;

    public AgentController(
            StudyAgentAssistant studyAgentAssistant
    ) {
        this.studyAgentAssistant = studyAgentAssistant;
    }

    @PostMapping("/chat")
    public Map<String, String> chat(
            @RequestBody AgentChatRequest request
    ) {

        if (request == null
                || request.getMessage() == null
                || request.getMessage().isBlank()) {

            throw new IllegalArgumentException(
                    "message 不能为空"
            );
        }

        String answer =
                studyAgentAssistant.chat(
                        request.getMessage()
                );

        return Map.of(
                "answer",
                answer
        );
    }
}