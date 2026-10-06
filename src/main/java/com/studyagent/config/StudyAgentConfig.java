package com.studyagent.config;

import com.studyagent.agent.StudyAgentAssistant;
import com.studyagent.agent.StudyAgentTools;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StudyAgentConfig {

    @Bean
    public StudyAgentAssistant studyAgentAssistant(
            ChatModel chatModel,
            StudyAgentTools studyAgentTools
    ) {

        return AiServices.builder(StudyAgentAssistant.class)
                .chatModel(chatModel)
                .tools(studyAgentTools)
                .build();
    }
}