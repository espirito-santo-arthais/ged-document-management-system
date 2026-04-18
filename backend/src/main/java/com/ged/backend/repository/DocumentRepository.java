package com.ged.backend.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.ged.backend.domain.entity.Document;

@Repository
public interface DocumentRepository
		extends JpaRepository<Document, UUID>, JpaSpecificationExecutor<Document> {

}