package com.studyagent.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController

//这个 Controller 里面所有接口的 URL 都先以 /api 开头。
@RequestMapping("/api")
public class HealthController {

    //当有人发送 GET 请求访问 /health 时，执行下面这个 Java 方法。
    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of(
                "status", "ok",
                "project", "StudyAgent"
        );
    }
}