package com.ged.backend.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ged.backend.domain.entity.Document;
import com.ged.backend.domain.enums.DocumentStatusEnum;

@Repository
public interface DocumentRepository extends JpaRepository<Document, UUID> {

	@Query("""
			    SELECT DISTINCT d FROM Document d
			    WHERE (
			            (:title IS NULL OR LOWER(d.title) LIKE LOWER(CONCAT(:title, '%')))
			            OR
			            (:title IS NULL OR LOWER(d.description) LIKE LOWER(CONCAT(:title, '%')))
			          )
			    AND (:owner IS NULL OR d.owner = :owner)
			    AND (:status IS NULL OR d.status = :status)
			    AND (:createdAfter IS NULL OR d.createdAt >= :createdAfter)
			    AND (:createdBefore IS NULL OR d.createdAt <= :createdBefore)
			    AND (:updatedAfter IS NULL OR d.updatedAt >= :updatedAfter)
			    AND (:updatedBefore IS NULL OR d.updatedAt <= :updatedBefore)
			    AND (:tags IS NULL OR EXISTS (SELECT t FROM d.tags t WHERE t IN :tags))
			""")
	Page<Document> searchStartingWith(
			@Param("title") String title,
			@Param("status") DocumentStatusEnum status,
			@Param("owner") String owner,
			@Param("createdAfter") LocalDateTime createdAfter,
			@Param("createdBefore") LocalDateTime createdBefore,
			@Param("updatedAfter") LocalDateTime updatedAfter,
			@Param("updatedBefore") LocalDateTime updatedBefore,
			@Param("tags") List<String> tags,
			Pageable pageable);

	@Query("""
			    SELECT DISTINCT d FROM Document d
			    WHERE (
			            (:title IS NULL OR LOWER(d.title) LIKE LOWER(CONCAT('%', :title, '%')))
			            OR
			            (:title IS NULL OR LOWER(d.description) LIKE LOWER(CONCAT('%', :title, '%')))
			          )
			    AND (:status IS NULL OR d.status = :status)
			    AND (:owner IS NULL OR d.owner = :owner)
			    AND (:createdAfter IS NULL OR d.createdAt >= :createdAfter)
			    AND (:createdBefore IS NULL OR d.createdAt <= :createdBefore)
			    AND (:updatedAfter IS NULL OR d.updatedAt >= :updatedAfter)
			    AND (:updatedBefore IS NULL OR d.updatedAt <= :updatedBefore)
			    AND (:tags IS NULL OR EXISTS (SELECT t FROM d.tags t WHERE t IN :tags))
			""")
	Page<Document> searchContaining(
			@Param("title") String title,
			@Param("status") DocumentStatusEnum status,
			@Param("owner") String owner,
			@Param("createdAfter") LocalDateTime createdAfter,
			@Param("createdBefore") LocalDateTime createdBefore,
			@Param("updatedAfter") LocalDateTime updatedAfter,
			@Param("updatedBefore") LocalDateTime updatedBefore,
			@Param("tags") List<String> tags,
			Pageable pageable);
}