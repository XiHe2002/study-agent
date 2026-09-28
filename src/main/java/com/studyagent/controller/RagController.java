package com.studyagent.controller;

import com.studyagent.dto.RagChatRequest;
import com.studyagent.dto.RagChatResponse;
import com.studyagent.service.RagService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rag")
public class RagController {

    private final RagService ragService;

    public RagController(RagService ragService) {
        this.ragService = ragService;
    }

    @PostMapping("/chat")
    public RagChatResponse chat(
            @RequestBody RagChatRequest request
    ) {
        return ragService.chat(
                request.getQuestion()
        );
    }
}