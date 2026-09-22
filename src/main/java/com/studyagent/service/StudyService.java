package com.studyagent.service;

import com.studyagent.dto.StudyResponse;
import org.springframework.stereotype.Service;

@Service
public class StudyService {

    public StudyResponse diagnose(String topic, String description) {

        String message = "StudyAgent 已收到你的学习诊断请求";

        return new StudyResponse(
                topic,
                description,
                message
        );
    }
}