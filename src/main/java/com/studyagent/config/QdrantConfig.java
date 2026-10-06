package com.studyagent.config;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.qdrant.QdrantEmbeddingStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QdrantConfig {

    @Bean
    public EmbeddingStore<TextSegment> embeddingStore() {

        return QdrantEmbeddingStore.builder()
                .host("localhost")
                .port(6334)
                .collectionName("studyagent_knowledge")
                .useTls(false)
                .build();
    }
}