package com.studyagent.controller;

import com.studyagent.dto.SearchRequest;
import com.studyagent.service.VectorStoreService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    private final VectorStoreService vectorStoreService;

    public SearchController(
            VectorStoreService vectorStoreService
    ) {
        this.vectorStoreService = vectorStoreService;
    }

    @PostMapping
    public List<String> search(
            @RequestBody SearchRequest request
    ) {
        return vectorStoreService.search(
                request.getQuery()
        );
    }
}