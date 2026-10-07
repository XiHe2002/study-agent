package com.studyagent.controller;

import com.studyagent.dto.DocumentUploadResponse;
import com.studyagent.dto.KnowledgeDocumentResponse;
import com.studyagent.service.DocumentService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping(
            value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public DocumentUploadResponse upload(
            @RequestParam("file") MultipartFile file
    ) throws IOException {

        return documentService.save(file);
    }
    @GetMapping
    public List<KnowledgeDocumentResponse> getAllDocuments() {
        return documentService.getAllDocuments();
    }

    @DeleteMapping("/{id}")
    public void deleteDocument(
            @PathVariable Long id
    ) throws IOException {

        documentService.deleteDocument(id);
    }
}