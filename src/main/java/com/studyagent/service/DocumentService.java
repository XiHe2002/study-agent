package com.studyagent.service;

import com.studyagent.dto.DocumentUploadResponse;
import com.studyagent.entity.KnowledgeDocument;
import com.studyagent.mapper.KnowledgeDocumentMapper;

import dev.langchain4j.data.embedding.Embedding;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;
import java.util.UUID;;

@Service
public class DocumentService {

    private final Path uploadDirectory = Paths.get("uploads");
    private final TextChunkService textChunkService;
    private final EmbeddingService embeddingService;
    private final VectorStoreService vectorStoreService;
    private final KnowledgeDocumentMapper knowledgeDocumentMapper;


    public DocumentUploadResponse save(MultipartFile file) throws IOException {

        if (file.isEmpty()) {
            throw new IllegalArgumentException("上传文件不能为空");
        }

        Files.createDirectories(uploadDirectory);

        String originalFilename = file.getOriginalFilename();

        if (originalFilename == null || originalFilename.isBlank()) {
            originalFilename = "unknown-file";
        }

        originalFilename = Paths.get(originalFilename)
                .getFileName()
                .toString();

        String storedFilename =
                UUID.randomUUID() + "_" + originalFilename;

        Path targetPath =
                uploadDirectory.resolve(storedFilename);

        file.transferTo(targetPath);

        String content = extractText(targetPath, originalFilename);

        List<String> chunks = textChunkService.split(content);
        List<Embedding> embeddings = embeddingService.embedAll(chunks);

        KnowledgeDocument document =
                new KnowledgeDocument();

        document.setOriginalFilename(originalFilename);
        document.setStoredFilename(storedFilename);
        document.setFileSize(file.getSize());
        document.setContentType(file.getContentType());

        knowledgeDocumentMapper.insert(document);

        Long documentId = document.getId();


        vectorStoreService.store(
                chunks,
                embeddings
        );
        int embeddingDimension =
                embeddings.isEmpty()
                        ? 0
                        : embeddings.get(0).dimension();

        return new DocumentUploadResponse(
                originalFilename,
                storedFilename,
                file.getSize(),
                file.getContentType(),
                content,
                chunks,
                embeddings.size(),
                embeddingDimension,
                documentId
        );
    }


    private String extractText(
            Path filePath,
            String originalFilename
    ) throws IOException {

        String filename =
                originalFilename.toLowerCase(Locale.ROOT);

        if (filename.endsWith(".txt")
                || filename.endsWith(".md")) {

            return Files.readString(
                    filePath,
                    StandardCharsets.UTF_8
            );
        }

        if (filename.endsWith(".pdf")) {
            return extractPdfText(filePath);
        }

        throw new IllegalArgumentException(
                "暂不支持该文件类型：" + originalFilename
        );
    }

    private String extractPdfText(Path filePath)
            throws IOException {

        try (PDDocument document =
                     Loader.loadPDF(filePath.toFile())) {

            PDFTextStripper stripper =
                    new PDFTextStripper();

            return stripper.getText(document);
        }
    }

    public DocumentService(
            TextChunkService textChunkService,
            EmbeddingService embeddingService,
            VectorStoreService vectorStoreService,
            KnowledgeDocumentMapper knowledgeDocumentMapper

    ) {
        this.textChunkService = textChunkService;
        this.embeddingService = embeddingService;
        this.vectorStoreService = vectorStoreService;
        this.knowledgeDocumentMapper = knowledgeDocumentMapper;

    }

}