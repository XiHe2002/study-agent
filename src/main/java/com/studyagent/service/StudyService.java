package com.studyagent.service;

import com.studyagent.dto.LearningRecordResponse;
import com.studyagent.dto.StudyResponse;
import com.studyagent.entity.LearningRecord;
import com.studyagent.mapper.LearningRecordMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudyService {

    private final LearningRecordMapper learningRecordMapper;

    public StudyService(LearningRecordMapper learningRecordMapper) {
        this.learningRecordMapper = learningRecordMapper;
    }

    public StudyResponse diagnose(String topic, String description) {

        LearningRecord record = new LearningRecord();

        record.setTopic(topic);
        record.setDescription(description);

        learningRecordMapper.insert(record);

        String message = "StudyAgent 已收到你的学习诊断请求";

        return new StudyResponse(
                topic,
                description,
                message
        );
    }

    public List<LearningRecordResponse> getAllRecords() {

        List<LearningRecord> records =
                learningRecordMapper.selectList(null);

        List<LearningRecordResponse> responses = new ArrayList<>();

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
}