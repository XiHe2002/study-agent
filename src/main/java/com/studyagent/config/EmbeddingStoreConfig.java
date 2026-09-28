package com.studyagent.config;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
@Configuration
public class EmbeddingStoreConfig {

    private static final Path STORE_PATH =
            Paths.get("data", "vector-store.json");

    @Bean
    public InMemoryEmbeddingStore<TextSegment> embeddingStore()
            throws IOException {

        Files.createDirectories(STORE_PATH.getParent());

        if (Files.exists(STORE_PATH)) {
            return InMemoryEmbeddingStore.fromFile(STORE_PATH);
        }

        return new InMemoryEmbeddingStore<>();
    }
}