package com.studyagent.service;

import com.studyagent.dto.DocumentUploadResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

@Service
public class DocumentService {

    private final Path uploadDirectory = Paths.get("uploads");

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

        return new DocumentUploadResponse(
                originalFilename,
                storedFilename,
                file.getSize(),
                file.getContentType(),
                content
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
}