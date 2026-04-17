package com.ged.backend.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.ged.backend.domain.entity.Document;
import com.ged.backend.domain.enums.DocumentStatusEnum;
import com.ged.backend.repository.DocumentRepository;

@Service
public class DocumentService {

    private final DocumentRepository repository;

    public DocumentService(DocumentRepository repository) {
        this.repository = repository;
    }

    public Document create(Document doc) {
        doc.setCreatedAt(LocalDateTime.now());
        doc.setUpdatedAt(LocalDateTime.now());
        doc.setStatus(DocumentStatusEnum.DRAFT);
        return repository.save(doc);
    }
}
