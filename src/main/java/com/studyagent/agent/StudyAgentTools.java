package com.studyagent.agent;

import com.studyagent.dto.StudyProfileResponse;
import com.studyagent.service.StudyService;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Component;

import com.studyagent.dto.QuizAttemptResponse;
import com.studyagent.service.QuizService;

import java.util.List;

import com.studyagent.dto.QuizGenerateResponse;
import dev.langchain4j.agent.tool.P;

@Component
public class StudyAgentTools {

    private final StudyService studyService;

    private final QuizService quizService;

    public StudyAgentTools(
            StudyService studyService,
            QuizService quizService
    ) {
        this.studyService = studyService;
        this.quizService = quizService;
    }

    @Tool("获取用户当前学习画像，包括作答次数、已掌握题目数、未掌握题目数、当前薄弱知识点、历史薄弱知识点和复习建议")
    public StudyProfileResponse getStudyProfile() {
        return studyService.getStudyProfile();
    }

    @Tool("获取用户当前仍未掌握的题目。返回每道当前错题的最近一次作答情况，包括题目、得分、反馈、薄弱点和薄弱知识标签")
    public List<QuizAttemptResponse> getCurrentMistakes() {
        return quizService.getMistakeAttempts();
    }

    @Tool("""
        生成新的针对性复习题，并将题目保存到系统中。
        这是一个会创建新题目的操作型工具。
        只有当用户明确要求“出题”“生成复习题”“生成练习题”或表达等价意图时才能调用。
        如果用户只是询问学习状态、未掌握题目数量、错题情况或薄弱知识点，禁止调用此工具。
        """)
    public QuizGenerateResponse generateReviewQuiz(
            @P("当前错题对应的作答记录 attemptId")
            Long attemptId,

            @P("需要生成的复习题数量，必须在 1 到 10 之间")
            Integer questionCount
    ) {
        return quizService.generateReviewQuiz(
                attemptId,
                questionCount
        );
    }
}