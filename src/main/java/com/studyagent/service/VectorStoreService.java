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

import dev.langchain4j.data.document.Metadata;
import java.util.Map;

import com.studyagent.dto.RetrievedChunkResponse;
import dev.langchain4j.data.document.Metadata;

import dev.langchain4j.store.embedding.filter.Filter;

import static dev.langchain4j.store.embedding.filter.MetadataFilterBuilder.metadataKey;

@Service
public class VectorStoreService {

    private final EmbeddingStore<TextSegment> embeddingStore;
    private final EmbeddingService embeddingService;

    public VectorStoreService(
            EmbeddingStore<TextSegment> embeddingStore,
            EmbeddingService embeddingService
    ) {
        this.embeddingStore = embeddingStore;
        this.embeddingService = embeddingService;
    }

    public void store(
            Long documentId,
            String originalFilename,
            List<String> chunks,
            List<Embedding> embeddings
    ) {

        List<TextSegment> segments = new ArrayList<>();

        for (int i = 0; i < chunks.size(); i++) {

            String chunk = chunks.get(i);

            Metadata metadata = Metadata.from(
                    Map.of(
                            "documentId", documentId,
                            "filename", originalFilename,
                            "chunkIndex", i
                    )
            );

            TextSegment segment =
                    TextSegment.from(chunk, metadata);

            segments.add(segment);
        }

        embeddingStore.addAll(
                embeddings,
                segments
        );


    }

    public List<RetrievedChunkResponse> search(String query) {

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

        List<RetrievedChunkResponse> results =
                new ArrayList<>();

        for (EmbeddingMatch<TextSegment> match
                : result.matches()) {

            TextSegment segment = match.embedded();
            Metadata metadata = segment.metadata();

            RetrievedChunkResponse response =
                    new RetrievedChunkResponse(
                            metadata.getLong("documentId"),
                            metadata.getString("filename"),
                            metadata.getInteger("chunkIndex"),
                            match.score(),
                            segment.text()
                    );

            results.add(response);
        }

        return results;
    }

    public void removeByDocumentId(Long documentId) {

        Filter filter =
                metadataKey("documentId")
                        .isEqualTo(documentId);

        embeddingStore.removeAll(filter);

    }
}