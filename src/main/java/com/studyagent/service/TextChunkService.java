package com.studyagent.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TextChunkService {

    private static final int CHUNK_SIZE = 500;
    private static final int OVERLAP_SIZE = 100;

    public List<String> split(String content) {

        List<String> chunks = new ArrayList<>();

        if (content == null || content.isBlank()) {
            return chunks;
        }

        int start = 0;

        while (start < content.length()) {

            int end = Math.min(
                    start + CHUNK_SIZE,
                    content.length()
            );

            String chunk = content.substring(start, end);
            chunks.add(chunk);

            if (end == content.length()) {
                break;
            }

            start = end - OVERLAP_SIZE;
        }

        return chunks;
    }
}