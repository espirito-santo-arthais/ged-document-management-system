package com.ged.backend.specification;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.ged.backend.domain.entity.Document;
import com.ged.backend.domain.enums.DocumentStatusEnum;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;

public class DocumentSpecification {

    private DocumentSpecification() {
        // classe utilitária
    }

    public static Specification<Document> titleOrDescriptionStartsWith(String value) {
        return (root, query, cb) -> {
            if (value == null || value.isBlank()) {
                return null;
            }

            String pattern = value.trim().toLowerCase() + "%";

            return cb.or(
                    cb.like(cb.lower(root.get("title")), pattern),
                    cb.like(cb.lower(root.get("description")), pattern));
        };
    }

    public static Specification<Document> titleOrDescriptionContains(String value) {
        return (root, query, cb) -> {
            if (value == null || value.isBlank()) {
                return null;
            }

            String pattern = "%" + value.trim().toLowerCase() + "%";

            return cb.or(
                    cb.like(cb.lower(root.get("title")), pattern),
                    cb.like(cb.lower(root.get("description")), pattern));
        };
    }

    public static Specification<Document> ownerEquals(String owner) {
        return (root, query, cb) -> {
            if (owner == null || owner.isBlank()) {
                return null;
            }

            return cb.equal(
                    cb.lower(root.get("owner")),
                    owner.trim().toLowerCase());
        };
    }

    public static Specification<Document> statusEquals(DocumentStatusEnum status) {
        return (root, query, cb) -> {
            if (status == null) {
                return null;
            }

            return cb.equal(root.get("status"), status);
        };
    }

    public static Specification<Document> createdBefore(LocalDateTime date) {
        return (root, query, cb) -> {
            if (date == null) {
                return null;
            }

            return cb.lessThanOrEqualTo(root.get("createdAt"), date);
        };
    }

    public static Specification<Document> createdAfter(LocalDateTime date) {
        return (root, query, cb) -> {
            if (date == null) {
                return null;
            }

            return cb.greaterThanOrEqualTo(root.get("createdAt"), date);
        };
    }

    public static Specification<Document> updatedBefore(LocalDateTime date) {
        return (root, query, cb) -> {
            if (date == null) {
                return null;
            }

            return cb.lessThanOrEqualTo(root.get("updatedAt"), date);
        };
    }

    public static Specification<Document> updatedAfter(LocalDateTime date) {
        return (root, query, cb) -> {
            if (date == null) {
                return null;
            }

            return cb.greaterThanOrEqualTo(root.get("updatedAt"), date);
        };
    }

    public static Specification<Document> hasTags(List<String> tags) {
        return (root, query, cb) -> {
            if (tags == null || tags.isEmpty()) {
                return null;
            }

            query.distinct(true);

            Join<Object, Object> tagJoin = root.join("tags", JoinType.LEFT);

            List<String> lowerTags = tags.stream()
                    .filter(tag -> tag != null && !tag.isBlank())
                    .map(tag -> tag.trim().toLowerCase())
                    .toList();

            return cb.lower(tagJoin).in(lowerTags);
        };
    }
}