package com.studyagent.service;

import com.studyagent.dto.LearningRecordResponse;
import com.studyagent.dto.StudyResponse;
import com.studyagent.entity.LearningRecord;
import com.studyagent.mapper.LearningRecordMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import com.studyagent.dto.QuizAttemptResponse;
import com.studyagent.dto.StudyProfileResponse;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.studyagent.dto.WeakPointSummaryResponse;

@Service
public class StudyService {

    private final LearningRecordMapper learningRecordMapper;
    private final AiService aiService;
    private final QuizService quizService;

    public StudyService(
            LearningRecordMapper learningRecordMapper,
            AiService aiService,
            QuizService quizService
    ) {
        this.learningRecordMapper = learningRecordMapper;
        this.aiService = aiService;
        this.quizService = quizService;
    }

    public StudyResponse diagnose(String topic, String description) {

        LearningRecord record = new LearningRecord();
        record.setTopic(topic);
        record.setDescription(description);

        learningRecordMapper.insert(record);

        String message =
                aiService.diagnoseStudyProblem(topic, description);

        return new StudyResponse(
                topic,
                description,
                message
        );
    }

    public List<LearningRecordResponse> getAllRecords() {

        List<LearningRecord> records =
                learningRecordMapper.selectList(null);

        List<LearningRecordResponse> responses =
                new ArrayList<>();

        for (LearningRecord record : records) {

            LearningRecordResponse response =
                    new LearningRecordResponse(
                            record.getId(),
                            record.getTopic(),
                            record.getDescription(),
                            record.getCreatedAt()
                    );

            responses.add(response);
        }

        return responses;
    }

    public StudyProfileResponse getStudyProfile() {

        List<QuizAttemptResponse> attempts =
                quizService.getAttempts();

        List<QuizAttemptResponse> mistakes =
                quizService.getMistakeAttempts(
                        attempts
                );

        List<WeakPointSummaryResponse> historicalWeakPoints =
                quizService.getWeakPointSummary(
                        attempts
                );

        int totalAttempts =
                attempts.size();

        int attemptedQuestions =
                (int) attempts.stream()
                        .map(QuizAttemptResponse::getQuestionId)
                        .distinct()
                        .count();

        int unresolvedQuestions =
                mistakes.size();

        int masteredQuestions =
                Math.max(
                        0,
                        attemptedQuestions - unresolvedQuestions
                );

        Set<String> currentWeakPointSet =
                new LinkedHashSet<>();

        for (QuizAttemptResponse mistake : mistakes) {

            List<String> weakPointTags =
                    mistake.getWeakPointTags();

            if (weakPointTags != null
                    && !weakPointTags.isEmpty()) {

                for (String tag : weakPointTags) {

                    if (tag != null && !tag.isBlank()) {
                        currentWeakPointSet.add(tag);
                    }
                }

            } else {

                // 兼容旧数据：
                // 旧 Attempt 没有 weakPointTags 时，
                // 暂时退回使用 weakPoints
                List<String> weakPoints =
                        mistake.getWeakPoints();

                if (weakPoints != null) {

                    for (String weakPoint : weakPoints) {

                        if (weakPoint != null
                                && !weakPoint.isBlank()) {

                            currentWeakPointSet.add(
                                    weakPoint
                            );
                        }
                    }
                }
            }
        }

        List<String> currentWeakPoints =
                currentWeakPointSet.stream()
                        .toList();

        List<String> priorityWeakPoints =
                currentWeakPoints.stream()
                        .limit(3)
                        .toList();

        String reviewSuggestion;

        if (unresolvedQuestions == 0) {

            reviewSuggestion =
                    "当前没有明显待复习题，可以继续学习新的知识内容。";

        } else if (currentWeakPoints.isEmpty()) {

            reviewSuggestion =
                    "当前仍有 "
                            + unresolvedQuestions
                            + " 道题未掌握，建议优先复习这些题目。";

        } else {

            reviewSuggestion =
                    "当前仍有 "
                            + unresolvedQuestions
                            + " 道题未掌握，建议优先复习："
                            + String.join(
                            "、",
                            priorityWeakPoints
                    )
                            + "。";
        }

        return new StudyProfileResponse(
                totalAttempts,
                attemptedQuestions,
                masteredQuestions,
                unresolvedQuestions,
                currentWeakPoints,
                historicalWeakPoints,
                reviewSuggestion
        );
    }
}