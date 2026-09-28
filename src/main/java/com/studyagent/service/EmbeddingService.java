package com.studyagent.service;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.model.embedding.EmbeddingModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmbeddingService {

    private final EmbeddingModel embeddingModel;

    public EmbeddingService(EmbeddingModel embeddingModel) {
        this.embeddingModel = embeddingModel;
    }

    public List<Embedding> embedAll(List<String> chunks) {

        List<Embedding> embeddings = new ArrayList<>();

        for (String chunk : chunks) {
            Embedding embedding =
                    embeddingModel.embed(chunk).content();

            embeddings.add(embedding);
        }

        return embeddings;
    }
}