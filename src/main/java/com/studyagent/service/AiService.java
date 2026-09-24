package com.studyagent.service;

import dev.langchain4j.model.chat.ChatModel;
import org.springframework.stereotype.Service;

@Service
public class AiService {

    private final ChatModel chatModel;

    public AiService(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    public String chat(String message) {
        return chatModel.chat(message);
    }

    public String diagnoseStudyProblem(String topic, String description) {

        String prompt = """
                你是 StudyAgent，一名面向计算机学习者的学习诊断助手。

                学习主题：
                %s

                学习者当前的问题：
                %s

                请完成以下任务：
                1. 判断学习者可能卡在哪些关键知识点。
                2. 用简单易懂的语言解释最核心的问题。
                3. 给出下一步建议学习的 3 个具体内容。
                4. 最后给出 1 道简单的自测题。

                回答尽量简洁、清晰、有层次。
                """.formatted(topic, description);

        return chatModel.chat(prompt);
    }
}