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
import java.util.UUID;
import com.studyagent.dto.KnowledgeDocumentResponse;
import java.util.ArrayList;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

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

        String fileHash = calculateSha256(file);

        KnowledgeDocument existingDocument =
                knowledgeDocumentMapper.selectOne(
                        new LambdaQueryWrapper<KnowledgeDocument>()
                                .eq(
                                        KnowledgeDocument::getFileHash,
                                        fileHash
                                )
                );

        if (existingDocument != null) {
            throw new IllegalArgumentException(
                    "该文件已经存在于知识库中，documentId="
                            + existingDocument.getId()
            );
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
        document.setFileHash(fileHash);

        knowledgeDocumentMapper.insert(document);

        Long documentId = document.getId();

        vectorStoreService.store(
                documentId,
                originalFilename,
                chunks,
                embeddings
        );
        int embeddingDimension =
                embeddings.isEmpty()
                        ? 0
                        : embeddings.get(0).dimension();

        return new DocumentUploadResponse(
                documentId,
                originalFilename,
                file.getSize(),
                file.getContentType(),
                chunks.size(),
                embeddingDimension
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

    public List<KnowledgeDocumentResponse> getAllDocuments() {

        List<KnowledgeDocument> documents =
                knowledgeDocumentMapper.selectList(null);

        List<KnowledgeDocumentResponse> responses =
                new ArrayList<>();

        for (KnowledgeDocument document : documents) {

            KnowledgeDocumentResponse response =
                    new KnowledgeDocumentResponse(
                            document.getId(),
                            document.getOriginalFilename(),
                            document.getFileSize(),
                            document.getContentType(),
                            document.getCreatedAt()
                    );

            responses.add(response);
        }

        return responses;
    }

    private String calculateSha256(MultipartFile file)
            throws IOException {

        try {
            MessageDigest digest =
                    MessageDigest.getInstance("SHA-256");

            try (InputStream inputStream =
                         file.getInputStream()) {

                byte[] buffer = new byte[8192];
                int bytesRead;

                while ((bytesRead =
                        inputStream.read(buffer)) != -1) {

                    digest.update(
                            buffer,
                            0,
                            bytesRead
                    );
                }
            }

            return HexFormat.of()
                    .formatHex(digest.digest());

        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(
                    "当前 Java 环境不支持 SHA-256",
                    e
            );
        }
    }

    public void deleteDocument(Long documentId)
            throws IOException {

        KnowledgeDocument document =
                knowledgeDocumentMapper.selectById(documentId);

        if (document == null) {
            throw new IllegalArgumentException(
                    "知识库文档不存在，documentId="
                            + documentId
            );
        }

        vectorStoreService.removeByDocumentId(
                documentId
        );

        Path filePath =
                uploadDirectory.resolve(
                        document.getStoredFilename()
                );

        Files.deleteIfExists(filePath);

        knowledgeDocumentMapper.deleteById(
                documentId
        );
    }
}