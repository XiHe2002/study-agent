package com.studyagent.controller;

import com.studyagent.dto.QuizGenerateRequest;
import com.studyagent.dto.QuizGenerateResponse;
import com.studyagent.service.QuizService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quiz")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @PostMapping("/generate")
    public QuizGenerateResponse generateQuiz(
            @RequestBody QuizGenerateRequest request
    ) {

        return quizService.generateQuiz(
                request.getTopic(),
                request.getQuestionCount()
        );
    }
}