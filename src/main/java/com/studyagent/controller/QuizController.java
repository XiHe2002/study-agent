package com.studyagent.controller;

import com.studyagent.dto.QuizGenerateRequest;
import com.studyagent.dto.QuizGenerateResponse;
import com.studyagent.service.QuizService;
import org.springframework.web.bind.annotation.*;

import com.studyagent.dto.QuizSubmitRequest;
import com.studyagent.dto.QuizSubmitResponse;

import com.studyagent.dto.QuizAttemptResponse;

import java.util.List;

import com.studyagent.dto.WeakPointSummaryResponse;

import com.studyagent.dto.QuizReviewRequest;
import com.studyagent.dto.QuizReviewResponse;

import com.studyagent.dto.QuizReviewGenerateRequest;

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

    @PostMapping("/submit")
    public QuizSubmitResponse submitAnswer(
            @RequestBody QuizSubmitRequest request
    ) {

        return quizService.submitAnswer(
                request.getQuestionId(),
                request.getAnswer()
        );
    }

    @GetMapping("/attempts")
    public List<QuizAttemptResponse> getAttempts() {
        return quizService.getAttempts();
    }

    @GetMapping("/weak-points")
    public List<WeakPointSummaryResponse> getWeakPoints() {
        return quizService.getWeakPointSummary();
    }

    @GetMapping("/mistakes")
    public List<QuizAttemptResponse> getMistakes() {
        return quizService.getMistakeAttempts();
    }

    @PostMapping("/review")
    public QuizReviewResponse reviewMistake(
            @RequestBody QuizReviewRequest request
    ) {
        return quizService.generateReview(
                request.getAttemptId()
        );
    }

    @PostMapping("/review/generate")
    public QuizGenerateResponse generateReviewQuiz(
            @RequestBody QuizReviewGenerateRequest request
    ) {

        return quizService.generateReviewQuiz(
                request.getAttemptId(),
                request.getQuestionCount()
        );
    }
}