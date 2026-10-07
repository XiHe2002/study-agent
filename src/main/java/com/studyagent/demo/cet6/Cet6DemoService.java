package com.studyagent.demo.cet6;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.studyagent.service.AiService;

import java.util.HashMap;
import java.util.Map;

import com.studyagent.dto.WeakPointSummaryResponse;

import java.util.LinkedHashSet;
import java.util.Set;

@Service
public class Cet6DemoService {

    private final ObjectMapper objectMapper;
    private final AiService aiService;

    public Cet6DemoService(
            ObjectMapper objectMapper,
            AiService aiService
    ) {
        this.objectMapper = objectMapper;
        this.aiService = aiService;
    }

    public List<Cet6PreviewResponse> previewReadingMistakes()
            throws IOException {

        Path attemptPath = Path.of(
                "data",
                "cet6",
                "cet6_demo_attempt.json"
        );

        Path referencePath = Path.of(
                "data",
                "cet6",
                "cet6_reference.json"
        );

        Cet6AttemptData attempt =
                objectMapper.readValue(
                        attemptPath.toFile(),
                        Cet6AttemptData.class
                );

        JsonNode referenceRoot =
                objectMapper.readTree(
                        referencePath.toFile()
                );

        JsonNode answerKey =
                referenceRoot.get("objectiveAnswerKey");

        List<Cet6PreviewResponse> results =
                new ArrayList<>();

        for (Cet6QuestionData question
                : attempt.getObjectiveQuestions()) {

            if (!question.getSection().startsWith("reading")) {
                continue;
            }

            if (!"real".equals(question.getAnswerSource())) {
                continue;
            }

            if (!Boolean.TRUE.equals(question.getIsAnswered())) {
                continue;
            }

            String referenceAnswer =
                    answerKey
                            .get(
                                    String.valueOf(
                                            question.getQuestionId()
                                    )
                            )
                            .asText();

            boolean correct =
                    referenceAnswer.equalsIgnoreCase(
                            question.getUserAnswer()
                    );

            if (correct) {
                continue;
            }

            results.add(
                    new Cet6PreviewResponse(
                            question.getQuestionId(),
                            question.getSection(),
                            question.getUserAnswer(),
                            referenceAnswer,
                            false,
                            question.getAnswerSource()
                    )
            );

            if (results.size() == 3) {
                break;
            }
        }

        return results;
    }

    public Cet6DiagnosisResponse diagnoseFirstReadingMistake()
            throws IOException {

        List<Cet6PreviewResponse> mistakes =
                previewReadingMistakes();

        if (mistakes.isEmpty()) {
            throw new IllegalArgumentException(
                    "没有找到可诊断的阅读错题"
            );
        }

        return diagnoseMistake(
                mistakes.get(0)
        );
    }

    private Cet6DiagnosisResponse diagnoseMistake(
            Cet6PreviewResponse mistake
    ) throws IOException {

        String questionContext =
                loadQuestionContext(
                        mistake.getExamQuestionNo()
                );

        String prompt = """
        你是 StudyAgent 的 CET-6 阅读错题诊断模块。

        这道题已经由 Java 根据标准答案确定为回答错误，
        你不需要重新判断对错，也不能修改标准答案。

        请根据提供的阅读材料、当前题目、用户答案和标准答案，
        分析用户为什么会答错。

        不同题型重点关注：

        1. 如果是选词填空：
           重点分析词性、句法结构、固定搭配和上下文语义。

        2. 如果是段落匹配：
           重点分析信息定位、同义替换、段落主旨和细节匹配。

        3. 如果是仔细阅读选择题：
           重点分析细节定位、同义替换、主旨判断、推理判断
           以及干扰项辨析。

        请完成：
        1. 解释用户答案为什么不合适。
        2. 解释标准答案为什么有原文依据。
        3. 指出用户可能暴露的阅读能力薄弱点。
        4. weakPointTags 使用简短、稳定的中文标签。

        请严格返回：

        {
          "feedback": "具体诊断",
          "weakPoints": [
            "具体薄弱点"
          ],
          "weakPointTags": [
            "稳定标签"
          ]
        }

        weakPointTags 优先从以下标签中选择：
        “词性辨析”
        “固定搭配”
        “上下文语义”
        “句法结构”
        “信息定位”
        “同义替换”
        “段落主旨”
        “细节匹配”
        “细节定位”
        “主旨判断”
        “推理判断”
        “干扰项辨析”

        注意：
        1. 只返回 JSON。
        2. 不要返回 Markdown。
        3. 不要重新判断正确答案。
        4. 除非确有必要，不要创造意思高度相近的新标签。

        【题号】
        %d

        【题目上下文】
        %s

        【用户答案】
        %s

        【标准答案】
        %s
        """.formatted(
                mistake.getExamQuestionNo(),
                questionContext,
                mistake.getUserAnswer(),
                mistake.getReferenceAnswer()
        );

        String analysisJson =
                aiService.chat(prompt);

        Cet6AiAnalysis analysis;

        try {
            analysis = objectMapper.readValue(
                    analysisJson,
                    Cet6AiAnalysis.class
            );
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(
                    "AI 返回的 CET-6 诊断 JSON 格式不正确",
                    e
            );
        }

        return new Cet6DiagnosisResponse(
                mistake.getExamQuestionNo(),
                mistake.getUserAnswer(),
                mistake.getReferenceAnswer(),
                0,
                analysis.getFeedback(),
                analysis.getWeakPoints(),
                analysis.getWeakPointTags(),
                mistake.getAnswerSource()
        );
    }

    public List<Cet6DiagnosisResponse>
    diagnoseFirstThreeReadingMistakes()
            throws IOException {

        List<Cet6PreviewResponse> mistakes =
                previewReadingMistakes();

        List<Cet6DiagnosisResponse> results =
                new ArrayList<>();

        for (Cet6PreviewResponse mistake : mistakes) {

            results.add(
                    diagnoseMistake(mistake)
            );
        }

        return results;
    }

    public Cet6MiniProfileResponse buildMiniProfile()
            throws IOException {

        List<Cet6DiagnosisResponse> diagnoses =
                diagnoseFirstThreeReadingMistakes();

        Map<String, Integer> counts =
                new HashMap<>();

        for (Cet6DiagnosisResponse diagnosis : diagnoses) {

            for (String tag : diagnosis.getWeakPointTags()) {

                if (tag == null || tag.isBlank()) {
                    continue;
                }

                counts.merge(
                        tag,
                        1,
                        Integer::sum
                );
            }
        }

        List<WeakPointSummaryResponse> weakPoints =
                counts.entrySet()
                        .stream()
                        .sorted(
                                Map.Entry
                                        .<String, Integer>comparingByValue()
                                        .reversed()
                        )
                        .map(entry ->
                                new WeakPointSummaryResponse(
                                        entry.getKey(),
                                        entry.getValue()
                                )
                        )
                        .toList();

        String reviewSuggestion;

        if (weakPoints.isEmpty()) {

            reviewSuggestion =
                    "当前没有识别到明显薄弱点。";

        } else {

            List<String> priorityWeakPoints =
                    weakPoints.stream()
                            .limit(3)
                            .map(
                                    WeakPointSummaryResponse::getWeakPoint
                            )
                            .toList();

            reviewSuggestion =
                    "当前阅读选词填空的主要薄弱点集中在："
                            + String.join(
                            "、",
                            priorityWeakPoints
                    )
                            + "。建议优先针对这些能力进行复习。";
        }

        return new Cet6MiniProfileResponse(
                diagnoses.size(),
                weakPoints,
                reviewSuggestion
        );
    }

    private String loadQuestionContext(
            Integer questionId
    ) throws IOException {

        Path contextPath = Path.of(
                "data",
                "cet6",
                "cet6_reading_contexts.json"
        );

        JsonNode root =
                objectMapper.readTree(
                        contextPath.toFile()
                );

        String contextKey = null;

        for (JsonNode question : root.get("questions")) {

            if (question.get("questionId").asInt()
                    == questionId) {

                contextKey =
                        question.get("contextKey").asText();

                break;
            }
        }

        if (contextKey == null) {
            throw new IllegalArgumentException(
                    "没有找到题目上下文，questionId="
                            + questionId
            );
        }

        JsonNode contextNode =
                root.get("contexts")
                        .get(contextKey);

        if (contextNode == null) {
            throw new IllegalStateException(
                    "题目上下文配置不存在，contextKey="
                            + contextKey
            );
        }

        StringBuilder context =
                new StringBuilder();

        context.append("【阅读材料】\n")
                .append(
                        contextNode
                                .get("passage")
                                .asText()
                );

// 找到当前题目的完整配置
        JsonNode currentQuestion = null;

        for (JsonNode question : root.get("questions")) {

            if (question.get("questionId").asInt()
                    == questionId) {

                currentQuestion = question;
                break;
            }
        }

        if (currentQuestion == null) {
            throw new IllegalArgumentException(
                    "没有找到题目配置，questionId="
                            + questionId
            );
        }

// 加入题干
        JsonNode questionText =
                currentQuestion.get("questionText");

        if (questionText != null) {

            context.append("\n\n【当前题目】\n")
                    .append(questionText.asText());
        }

// Section A：加入词库
        JsonNode wordBank =
                contextNode.get("wordBank");

        if (wordBank != null
                && wordBank.isObject()) {

            context.append("\n\n【候选词】\n");

            wordBank.fields()
                    .forEachRemaining(entry ->
                            context.append(entry.getKey())
                                    .append(") ")
                                    .append(
                                            entry.getValue().asText()
                                    )
                                    .append("\n")
                    );
        }

// Section C：加入 A/B/C/D
        JsonNode options =
                currentQuestion.get("options");

        if (options != null
                && options.isObject()) {

            context.append("\n\n【选项】\n");

            options.fields()
                    .forEachRemaining(entry ->
                            context.append(entry.getKey())
                                    .append(") ")
                                    .append(
                                            entry.getValue().asText()
                                    )
                                    .append("\n")
                    );
        }

        return context.toString();
    }

    public Cet6DiagnosisResponse diagnoseReadingQuestion(
            Integer examQuestionNo
    ) throws IOException {

        if (examQuestionNo == null
                || examQuestionNo < 26
                || examQuestionNo > 55) {

            throw new IllegalArgumentException(
                    "阅读题号必须在 26 到 55 之间"
            );
        }

        Path attemptPath = Path.of(
                "data",
                "cet6",
                "cet6_demo_attempt.json"
        );

        Path referencePath = Path.of(
                "data",
                "cet6",
                "cet6_reference.json"
        );

        Cet6AttemptData attempt =
                objectMapper.readValue(
                        attemptPath.toFile(),
                        Cet6AttemptData.class
                );

        JsonNode referenceRoot =
                objectMapper.readTree(
                        referencePath.toFile()
                );

        Cet6QuestionData targetQuestion = null;

        for (Cet6QuestionData question
                : attempt.getObjectiveQuestions()) {

            if (question.getQuestionId()
                    .equals(examQuestionNo)) {

                targetQuestion = question;
                break;
            }
        }

        if (targetQuestion == null) {
            throw new IllegalArgumentException(
                    "没有找到题目，questionNo="
                            + examQuestionNo
            );
        }

        if (!"real".equals(
                targetQuestion.getAnswerSource()
        )) {
            throw new IllegalArgumentException(
                    "该题不是用户真实作答数据"
            );
        }

        if (!Boolean.TRUE.equals(
                targetQuestion.getIsAnswered()
        )) {
            throw new IllegalArgumentException(
                    "该题用户未作答"
            );
        }

        String referenceAnswer =
                referenceRoot
                        .get("objectiveAnswerKey")
                        .get(
                                String.valueOf(
                                        examQuestionNo
                                )
                        )
                        .asText();

        boolean correct =
                referenceAnswer.equalsIgnoreCase(
                        targetQuestion.getUserAnswer()
                );

        if (correct) {
            throw new IllegalArgumentException(
                    "该题回答正确，无需进行错题诊断"
            );
        }

        Cet6PreviewResponse mistake =
                new Cet6PreviewResponse(
                        examQuestionNo,
                        targetQuestion.getSection(),
                        targetQuestion.getUserAnswer(),
                        referenceAnswer,
                        false,
                        targetQuestion.getAnswerSource()
                );

        return diagnoseMistake(mistake);
    }

    public Cet6ReadingReportResponse buildReadingReport()
            throws IOException {

        Path attemptPath = Path.of(
                "data",
                "cet6",
                "cet6_real_attempt.json"
        );

        Path referencePath = Path.of(
                "data",
                "cet6",
                "cet6_reference.json"
        );

        Cet6AttemptData attempt =
                objectMapper.readValue(
                        attemptPath.toFile(),
                        Cet6AttemptData.class
                );

        JsonNode referenceRoot =
                objectMapper.readTree(
                        referencePath.toFile()
                );

        JsonNode answerKey =
                referenceRoot.get("objectiveAnswerKey");

        Map<String, int[]> sectionCounts =
                new HashMap<>();

        List<Cet6PreviewResponse> wrongQuestions =
                new ArrayList<>();

        int total = 0;
        int correctCount = 0;

        for (Cet6QuestionData question
                : attempt.getObjectiveQuestions()) {

            if (!question.getSection()
                    .startsWith("reading")) {
                continue;
            }

            if (!"real".equals(
                    question.getAnswerSource()
            )) {
                continue;
            }

            if (!Boolean.TRUE.equals(
                    question.getIsAnswered()
            )) {
                continue;
            }

            String referenceAnswer =
                    answerKey
                            .get(
                                    String.valueOf(
                                            question.getQuestionId()
                                    )
                            )
                            .asText();

            boolean correct =
                    referenceAnswer.equalsIgnoreCase(
                            question.getUserAnswer()
                    );

            total++;

            int[] counts =
                    sectionCounts.computeIfAbsent(
                            question.getSection(),
                            key -> new int[2]
                    );

            counts[0]++;

            if (correct) {
                correctCount++;
                counts[1]++;
            } else {

                wrongQuestions.add(
                        new Cet6PreviewResponse(
                                question.getQuestionId(),
                                question.getSection(),
                                question.getUserAnswer(),
                                referenceAnswer,
                                false,
                                question.getAnswerSource()
                        )
                );
            }
        }

        List<Cet6SectionStat> sections =
                sectionCounts.entrySet()
                        .stream()
                        .map(entry -> {

                            int sectionTotal =
                                    entry.getValue()[0];

                            int sectionCorrect =
                                    entry.getValue()[1];

                            return new Cet6SectionStat(
                                    entry.getKey(),
                                    sectionTotal,
                                    sectionCorrect,
                                    sectionTotal
                                            - sectionCorrect,
                                    sectionTotal == 0
                                            ? 0
                                            : sectionCorrect
                                            * 100.0
                                            / sectionTotal
                            );
                        })
                        .toList();

        int wrongCount =
                total - correctCount;

        double accuracy =
                total == 0
                        ? 0
                        : correctCount
                        * 100.0
                        / total;

        return new Cet6ReadingReportResponse(
                total,
                correctCount,
                wrongCount,
                accuracy,
                sections,
                wrongQuestions
        );
    }

    public List<Cet6SectionDiagnosisResponse>
    diagnoseReadingSections() throws IOException {

        Cet6ReadingReportResponse report =
                buildReadingReport();

        List<Cet6SectionDiagnosisResponse> results =
                new ArrayList<>();

        List<String> sectionOrder = List.of(
                "reading_section_a",
                "reading_section_b",
                "reading_section_c"
        );

        for (String section : sectionOrder) {

            List<Cet6PreviewResponse> mistakes =
                    report.getWrongQuestions()
                            .stream()
                            .filter(question ->
                                    section.equals(
                                            question.getSection()
                                    )
                            )
                            .toList();

            if (mistakes.isEmpty()) {
                continue;
            }

            results.add(
                    diagnoseSection(
                            section,
                            mistakes
                    )
            );
        }

        return results;
    }

    private Cet6SectionDiagnosisResponse diagnoseSection(
            String section,
            List<Cet6PreviewResponse> mistakes
    ) throws IOException {

        Path contextPath = Path.of(
                "data",
                "cet6",
                "cet6_reading_contexts.json"
        );

        JsonNode root =
                objectMapper.readTree(
                        contextPath.toFile()
                );

        StringBuilder diagnosisContext =
                new StringBuilder();

        Set<String> addedContextKeys =
                new LinkedHashSet<>();

        for (Cet6PreviewResponse mistake : mistakes) {

            JsonNode currentQuestion = null;

            for (JsonNode question
                    : root.get("questions")) {

                if (question.get("questionId").asInt()
                        == mistake.getExamQuestionNo()) {

                    currentQuestion = question;
                    break;
                }
            }

            if (currentQuestion == null) {
                throw new IllegalArgumentException(
                        "没有找到题目配置，questionId="
                                + mistake.getExamQuestionNo()
                );
            }

            String contextKey =
                    currentQuestion
                            .get("contextKey")
                            .asText();

            /*
             * 同一篇文章只加入一次。
             *
             * Section A 有 8 道错题，
             * 不能把同一篇文章重复发送 8 次给大模型。
             */
            if (addedContextKeys.add(contextKey)) {

                JsonNode contextNode =
                        root.get("contexts")
                                .get(contextKey);

                diagnosisContext
                        .append("\n\n【阅读材料】\n")
                        .append(
                                contextNode
                                        .get("passage")
                                        .asText()
                        );

                JsonNode wordBank =
                        contextNode.get("wordBank");

                if (wordBank != null
                        && wordBank.isObject()) {

                    diagnosisContext
                            .append("\n\n【候选词】\n");

                    wordBank.fields()
                            .forEachRemaining(entry ->
                                    diagnosisContext
                                            .append(entry.getKey())
                                            .append(") ")
                                            .append(
                                                    entry
                                                            .getValue()
                                                            .asText()
                                            )
                                            .append("\n")
                            );
                }
            }

            diagnosisContext
                    .append("\n\n----------------")
                    .append("\n【错题 ")
                    .append(
                            mistake.getExamQuestionNo()
                    )
                    .append("】\n");

            JsonNode questionText =
                    currentQuestion.get(
                            "questionText"
                    );

            if (questionText != null) {

                diagnosisContext
                        .append("题目：")
                        .append(
                                questionText.asText()
                        )
                        .append("\n");
            }

            JsonNode options =
                    currentQuestion.get("options");

            if (options != null
                    && options.isObject()) {

                diagnosisContext
                        .append("选项：\n");

                options.fields()
                        .forEachRemaining(entry ->
                                diagnosisContext
                                        .append(entry.getKey())
                                        .append(") ")
                                        .append(
                                                entry
                                                        .getValue()
                                                        .asText()
                                        )
                                        .append("\n")
                        );
            }

            diagnosisContext
                    .append("用户答案：")
                    .append(
                            mistake.getUserAnswer()
                    )
                    .append("\n")
                    .append("标准答案：")
                    .append(
                            mistake.getReferenceAnswer()
                    )
                    .append("\n");
        }

        String prompt = """
            你是 StudyAgent 的 CET-6 阅读能力诊断模块。

            下面是一名学习者在同一种阅读题型中的多道真实错题。
            每道题的正确答案已经由 Java 根据标准答案确定，
            你不能修改这些正确答案。

            你的任务不是逐题重复讲答案，
            而是结合这些错题，诊断学习者在该题型中的共同问题。

            请重点分析：
            1. 多道错题之间是否存在共同错误模式。
            2. 学习者最主要的 2～5 个阅读薄弱点。
            3. 哪些能力应该优先复习。
            4. weakPointTags 使用稳定、简短的中文标签。

            标签优先从以下范围选择：

            “词性辨析”
            “固定搭配”
            “上下文语义”
            “句法结构”
            “信息定位”
            “同义替换”
            “段落主旨”
            “细节匹配”
            “细节定位”
            “主旨判断”
            “推理判断”
            “干扰项辨析”

            严格返回 JSON：

            {
              "feedback": "对该题型整体表现的诊断",
              "weakPoints": [
                "具体薄弱点1",
                "具体薄弱点2"
              ],
              "weakPointTags": [
                "稳定标签1",
                "稳定标签2"
              ]
            }

            注意：
            1. 只返回 JSON。
            2. 不要返回 Markdown。
            3. 不要重新判定答案。
            4. 不要因为一题偶然错误就过度推断。
            5. 优先总结多道错题共同体现出的能力问题。

            【题型】
            %s

            【错题及阅读材料】
            %s
            """.formatted(
                section,
                diagnosisContext.toString()
        );

        String analysisJson =
                aiService.chat(prompt);

        Cet6AiAnalysis analysis;

        try {
            analysis = objectMapper.readValue(
                    analysisJson,
                    Cet6AiAnalysis.class
            );
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(
                    "AI 返回的 CET-6 题型诊断 JSON 格式不正确",
                    e
            );
        }

        return new Cet6SectionDiagnosisResponse(
                section,
                mistakes.size(),
                analysis.getFeedback(),
                analysis.getWeakPoints(),
                analysis.getWeakPointTags()
        );
    }

    public Cet6ReadingProfileResponse
    buildReadingProfile() throws IOException {

        Cet6ReadingReportResponse report =
                buildReadingReport();

        List<Cet6SectionDiagnosisResponse> diagnoses =
                diagnoseReadingSections();

        Cet6SectionStat weakestSection =
                report.getSections()
                        .stream()
                        .min(
                                (a, b) ->
                                        Double.compare(
                                                a.getAccuracy(),
                                                b.getAccuracy()
                                        )
                        )
                        .orElseThrow(
                                () -> new IllegalStateException(
                                        "没有阅读成绩数据"
                                )
                        );

        Cet6SectionDiagnosisResponse
                priorityDiagnosis =
                diagnoses.stream()
                        .filter(diagnosis ->
                                weakestSection
                                        .getSection()
                                        .equals(
                                                diagnosis
                                                        .getSection()
                                        )
                        )
                        .findFirst()
                        .orElseThrow(
                                () -> new IllegalStateException(
                                        "没有找到对应题型诊断"
                                )
                        );

        String reviewSuggestion =
                buildReadingReviewSuggestion(
                        weakestSection,
                        priorityDiagnosis
                );

        return new Cet6ReadingProfileResponse(
                report,
                diagnoses,
                weakestSection.getSection(),
                weakestSection.getAccuracy(),
                priorityDiagnosis.getWeakPointTags(),
                reviewSuggestion
        );
    }

    private String buildReadingReviewSuggestion(
            Cet6SectionStat weakestSection,
            Cet6SectionDiagnosisResponse diagnosis
    ) {

        String sectionName =
                switch (weakestSection.getSection()) {

                    case "reading_section_a" ->
                            "Section A 选词填空";

                    case "reading_section_b" ->
                            "Section B 段落匹配";

                    case "reading_section_c" ->
                            "Section C 仔细阅读";

                    default ->
                            weakestSection.getSection();
                };

        String weakPoints =
                String.join(
                        "、",
                        diagnosis.getWeakPointTags()
                );

        return "当前应优先复习"
                + sectionName
                + "，该部分正确率为 "
                + String.format(
                "%.1f",
                weakestSection.getAccuracy()
        )
                + "%。"
                + "主要薄弱点包括："
                + weakPoints
                + "。建议先针对这些能力进行专项训练，"
                + "再重新完成同类型题目验证是否掌握。";
    }

    public Cet6ObjectiveSectionReport
    buildDemoListeningReport() throws IOException {

        Path attemptPath = Path.of(
                "data",
                "cet6",
                "cet6_demo_attempt.json"
        );

        Path referencePath = Path.of(
                "data",
                "cet6",
                "cet6_reference.json"
        );

        Cet6AttemptData attempt =
                objectMapper.readValue(
                        attemptPath.toFile(),
                        Cet6AttemptData.class
                );

        JsonNode referenceRoot =
                objectMapper.readTree(
                        referencePath.toFile()
                );

        JsonNode answerKey =
                referenceRoot.get("objectiveAnswerKey");

        int total = 0;
        int correctCount = 0;

        List<Cet6PreviewResponse> wrongQuestions =
                new ArrayList<>();

        for (Cet6QuestionData question
                : attempt.getObjectiveQuestions()) {

            if (!"listening".equals(
                    question.getSection()
            )) {
                continue;
            }

            if (!"synthetic".equals(
                    question.getAnswerSource()
            )) {
                continue;
            }

            if (!Boolean.TRUE.equals(
                    question.getIsAnswered()
            )) {
                continue;
            }

            String referenceAnswer =
                    answerKey
                            .get(
                                    String.valueOf(
                                            question.getQuestionId()
                                    )
                            )
                            .asText();

            boolean correct =
                    referenceAnswer.equalsIgnoreCase(
                            question.getUserAnswer()
                    );

            total++;

            if (correct) {

                correctCount++;

            } else {

                wrongQuestions.add(
                        new Cet6PreviewResponse(
                                question.getQuestionId(),
                                question.getSection(),
                                question.getUserAnswer(),
                                referenceAnswer,
                                false,
                                question.getAnswerSource()
                        )
                );
            }
        }

        int wrongCount =
                total - correctCount;

        double accuracy =
                total == 0
                        ? 0
                        : correctCount
                        * 100.0
                        / total;

        return new Cet6ObjectiveSectionReport(
                "listening",
                "synthetic",
                total,
                correctCount,
                wrongCount,
                accuracy,
                wrongQuestions
        );
    }

    public Cet6SubjectiveEvaluationResponse
    evaluateDemoWriting() throws IOException {

        Path attemptPath = Path.of(
                "data",
                "cet6",
                "cet6_demo_attempt.json"
        );

        Path referencePath = Path.of(
                "data",
                "cet6",
                "cet6_reference.json"
        );

        Cet6AttemptData attempt =
                objectMapper.readValue(
                        attemptPath.toFile(),
                        Cet6AttemptData.class
                );

        JsonNode referenceRoot =
                objectMapper.readTree(
                        referencePath.toFile()
                );

        Cet6SubjectiveAnswerData writing =
                attempt.getWriting();

        if (writing == null
                || !Boolean.TRUE.equals(
                writing.getIsAnswered()
        )) {

            throw new IllegalArgumentException(
                    "Demo Writing 没有作答内容"
            );
        }

        if (!"synthetic".equals(
                writing.getAnswerSource()
        )) {

            throw new IllegalArgumentException(
                    "该接口只允许评价 synthetic Writing 数据"
            );
        }

        JsonNode writingReference =
                referenceRoot.get("writing");

        String promptSummary =
                writingReference
                        .get("promptSummary")
                        .asText();

        String referenceEssay =
                writingReference
                        .get("referenceEssay")
                        .asText();

        String userAnswer =
                writing.getUserAnswer();

        int wordCount =
                userAnswer.isBlank()
                        ? 0
                        : userAnswer
                        .trim()
                        .split("\\s+")
                        .length;

        String prompt = """
            你是 StudyAgent 的 CET-6 作文诊断模块。

            当前答案是 synthetic demo 数据，
            仅用于测试系统能力，不代表用户真实英语水平。

            请根据作文题目要求评价这篇文章。

            注意：
            1. referenceEssay 只是参考范文，不是唯一正确答案。
            2. 不要因为用户表达与参考范文不同而扣分。
            3. score 为 0 到 100 的 AI 诊断分，
               不是官方 CET-6 分数。
            4. 重点评价：
               - 任务完成度
               - 文章结构与逻辑
               - 语法准确性
               - 词汇与搭配
               - 表达自然度
            5. 明确指出具体语言错误及改进方向。
            6. weakPointTags 优先使用稳定标签。

            weakPointTags 优先从以下范围选择：
            “主谓一致”
            “动词形式”
            “名词单复数”
            “冠词使用”
            “词汇搭配”
            “句式多样性”
            “逻辑衔接”
            “任务回应”
            “表达自然度”

            严格返回 JSON：

            {
              "score": 70,
              "feedback": "整体评价",
              "strengths": [
                "做得较好的地方"
              ],
              "weakPoints": [
                "具体问题"
              ],
              "weakPointTags": [
                "稳定标签"
              ]
            }

            注意：
            1. 只返回 JSON。
            2. 不要返回 Markdown。
            3. score 必须是 0 到 100 的整数。

            【作文要求】
            %s

            【参考范文】
            %s

            【待评价作文】
            %s
            """.formatted(
                promptSummary,
                referenceEssay,
                userAnswer
        );

        String analysisJson =
                aiService.chat(prompt);

        Cet6SubjectiveAiAnalysis analysis;

        try {
            analysis = objectMapper.readValue(
                    analysisJson,
                    Cet6SubjectiveAiAnalysis.class
            );
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(
                    "AI 返回的 CET-6 Writing JSON 格式不正确",
                    e
            );
        }

        if (analysis.getScore() == null
                || analysis.getScore() < 0
                || analysis.getScore() > 100) {

            throw new IllegalStateException(
                    "AI 返回的 Writing 评分不合法"
            );
        }

        if (analysis.getStrengths() == null) {
            analysis.setStrengths(
                    new ArrayList<>()
            );
        }

        if (analysis.getWeakPoints() == null) {
            analysis.setWeakPoints(
                    new ArrayList<>()
            );
        }

        if (analysis.getWeakPointTags() == null) {
            analysis.setWeakPointTags(
                    new ArrayList<>()
            );
        }

        return new Cet6SubjectiveEvaluationResponse(
                "writing",
                writing.getAnswerSource(),
                "ai_diagnostic_score",
                analysis.getScore(),
                wordCount,
                analysis.getFeedback(),
                analysis.getStrengths(),
                analysis.getWeakPoints(),
                analysis.getWeakPointTags()
        );
    }

    public Cet6SubjectiveEvaluationResponse
    evaluateDemoTranslation() throws IOException {

        Path attemptPath = Path.of(
                "data",
                "cet6",
                "cet6_demo_attempt.json"
        );

        Path referencePath = Path.of(
                "data",
                "cet6",
                "cet6_reference.json"
        );

        Cet6AttemptData attempt =
                objectMapper.readValue(
                        attemptPath.toFile(),
                        Cet6AttemptData.class
                );

        JsonNode referenceRoot =
                objectMapper.readTree(
                        referencePath.toFile()
                );

        Cet6SubjectiveAnswerData translation =
                attempt.getTranslation();

        if (translation == null
                || !Boolean.TRUE.equals(
                translation.getIsAnswered()
        )) {

            throw new IllegalArgumentException(
                    "Demo Translation 没有作答内容"
            );
        }

        if (!"synthetic".equals(
                translation.getAnswerSource()
        )) {

            throw new IllegalArgumentException(
                    "该接口只允许评价 synthetic Translation 数据"
            );
        }

        JsonNode translationReference =
                referenceRoot.get("translation");

        String sourceText =
                translationReference
                        .get("sourceText")
                        .asText();

        String referenceTranslation =
                translationReference
                        .get("referenceTranslation")
                        .asText();

        String userAnswer =
                translation.getUserAnswer();

        int wordCount =
                userAnswer.isBlank()
                        ? 0
                        : userAnswer
                        .trim()
                        .split("\\s+")
                        .length;

        String prompt = """
            你是 StudyAgent 的 CET-6 翻译诊断模块。

            当前答案是 synthetic demo 数据，
            仅用于测试系统能力，不代表用户真实英语水平。

            请根据中文原文评价用户译文。

            注意：
            1. referenceTranslation 只是参考译文，不是唯一正确答案。
            2. 不要因为用户表达与参考译文不同而机械扣分。
            3. score 为 0 到 100 的 AI 诊断分，
               不是官方 CET-6 分数。
            4. 重点评价：
               - 原文信息是否完整传达
               - 语义准确性
               - 语法准确性
               - 词汇与搭配
               - 表达自然度
            5. 明确指出具体翻译错误和改进方向。
            6. weakPointTags 使用稳定、简短的中文标签。

            weakPointTags 优先从以下范围选择：
            “主谓一致”
            “动词形式”
            “名词单复数”
            “冠词使用”
            “词汇搭配”
            “信息完整性”
            “语义准确性”
            “表达自然度”
            “句法结构”

            严格返回 JSON：

            {
              "score": 70,
              "feedback": "整体评价",
              "strengths": [
                "做得较好的地方"
              ],
              "weakPoints": [
                "具体问题"
              ],
              "weakPointTags": [
                "稳定标签"
              ]
            }

            注意：
            1. 只返回 JSON。
            2. 不要返回 Markdown。
            3. score 必须是 0 到 100 的整数。

            【中文原文】
            %s

            【参考译文】
            %s

            【用户译文】
            %s
            """.formatted(
                sourceText,
                referenceTranslation,
                userAnswer
        );

        String analysisJson =
                aiService.chat(prompt);

        Cet6SubjectiveAiAnalysis analysis;

        try {
            analysis = objectMapper.readValue(
                    analysisJson,
                    Cet6SubjectiveAiAnalysis.class
            );
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(
                    "AI 返回的 CET-6 Translation JSON 格式不正确",
                    e
            );
        }

        if (analysis.getScore() == null
                || analysis.getScore() < 0
                || analysis.getScore() > 100) {

            throw new IllegalStateException(
                    "AI 返回的 Translation 评分不合法"
            );
        }

        if (analysis.getStrengths() == null) {
            analysis.setStrengths(
                    new ArrayList<>()
            );
        }

        if (analysis.getWeakPoints() == null) {
            analysis.setWeakPoints(
                    new ArrayList<>()
            );
        }

        if (analysis.getWeakPointTags() == null) {
            analysis.setWeakPointTags(
                    new ArrayList<>()
            );
        }

        return new Cet6SubjectiveEvaluationResponse(
                "translation",
                translation.getAnswerSource(),
                "ai_diagnostic_score",
                analysis.getScore(),
                wordCount,
                analysis.getFeedback(),
                analysis.getStrengths(),
                analysis.getWeakPoints(),
                analysis.getWeakPointTags()
        );
    }

    public Cet6DemoReportResponse
    buildFinalDemoReport() throws IOException {

        Cet6ReadingProfileResponse reading =
                buildReadingProfile();

        Cet6ObjectiveSectionReport listening =
                buildDemoListeningReport();

        Cet6SubjectiveEvaluationResponse writing =
                evaluateDemoWriting();

        Cet6SubjectiveEvaluationResponse translation =
                evaluateDemoTranslation();

        return new Cet6DemoReportResponse(
                "CET-6 2026-06 第二套",
                "Reading 使用用户真实作答数据；"
                        + "Listening、Writing、Translation "
                        + "使用 synthetic demo 数据，"
                        + "仅用于验证 StudyAgent 功能，"
                        + "不代表用户真实英语水平；"
                        + "Writing 和 Translation 的 score "
                        + "均为 AI diagnostic score，"
                        + "不是官方 CET-6 分数。",
                reading,
                listening,
                writing,
                translation
        );
    }
}