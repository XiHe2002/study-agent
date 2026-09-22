package com.studyagent.controller;

import com.studyagent.dto.StudyRequest;
import com.studyagent.dto.StudyResponse;
import com.studyagent.service.StudyService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/study")
public class StudyController {

    private final StudyService studyService;

    public StudyController(StudyService studyService) {
        this.studyService = studyService;
    }

    @PostMapping("/diagnose")
    public StudyResponse diagnose(@RequestBody StudyRequest request) {

        return studyService.diagnose(
                request.getTopic(),
                request.getDescription()
        );
    }
}