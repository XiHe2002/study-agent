package com.studyagent.controller;

import com.studyagent.dto.AiChatRequest;
import com.studyagent.dto.AiChatResponse;
import com.studyagent.service.AiService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    private final AiService aiService;

    public AiController(AiService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/chat")
    public AiChatResponse chat(@RequestBody AiChatRequest request) {

        String answer = aiService.chat(request.getMessage());

        return new AiChatResponse(answer);
    }
}