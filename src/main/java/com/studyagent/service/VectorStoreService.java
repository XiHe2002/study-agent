package com.studyagent.service;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class VectorStoreService {

    private final InMemoryEmbeddingStore<TextSegment> embeddingStore;
    private final EmbeddingService embeddingService;
    private static final Path STORE_PATH =
            Paths.get("data", "vector-store.json");

    public VectorStoreService(
            InMemoryEmbeddingStore<TextSegment> embeddingStore,
            EmbeddingService embeddingService
    ) {
        this.embeddingStore = embeddingStore;
        this.embeddingService = embeddingService;
    }

    public void store(
            List<String> chunks,
            List<Embedding> embeddings
    ) {

        List<TextSegment> segments = new ArrayList<>();

        for (String chunk : chunks) {
            segments.add(TextSegment.from(chunk));
        }

        embeddingStore.addAll(
                embeddings,
                segments
        );
        embeddingStore.serializeToFile(STORE_PATH);
    }

    public List<String> search(String query) {

        Embedding queryEmbedding =
                embeddingService.embed(query);

        EmbeddingSearchRequest request =
                EmbeddingSearchRequest.builder()
                        .queryEmbedding(queryEmbedding)
                        .maxResults(3)
                        .minScore(0.5)
                        .build();

        EmbeddingSearchResult<TextSegment> result =
                embeddingStore.search(request);

        List<String> matchedTexts =
                new ArrayList<>();

        for (EmbeddingMatch<TextSegment> match
                : result.matches()) {

            matchedTexts.add(
                    match.embedded().text()
            );
        }

        return matchedTexts;
    }
}