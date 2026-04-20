-- ========================================
-- V1__init.sql
-- Initial database schema
-- ========================================

-- =========================
-- TABLE: documents
-- =========================
CREATE TABLE documents (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP(6) NOT NULL,
    description VARCHAR(1000),
    "owner" VARCHAR(100) NOT NULL,
    status VARCHAR(20) NOT NULL,
    title VARCHAR(255) NOT NULL,
    updated_at TIMESTAMP(6),

    CONSTRAINT documents_status_check CHECK (
        status IN ('DRAFT', 'PUBLISHED', 'ARCHIVED')
    )
);

CREATE INDEX documents_idx_owner ON documents(owner);
CREATE INDEX documents_idx_status ON documents(status);
CREATE INDEX documents_idx_title ON documents(title);

-- =========================
-- TABLE: document_tags
-- =========================
CREATE TABLE document_tags (
    document_id UUID NOT NULL,
    tag VARCHAR(255),

    CONSTRAINT document_tags_fk_document
        FOREIGN KEY (document_id)
        REFERENCES documents(id)
        ON DELETE CASCADE
);

-- =========================
-- TABLE: document_versions
-- =========================
CREATE TABLE document_versions (
    id UUID PRIMARY KEY,
    content_type VARCHAR(100) NOT NULL,
    created_at TIMESTAMP(6) NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    "size" BIGINT NOT NULL,
    storage_key VARCHAR(500) NOT NULL,
    "version" INTEGER NOT NULL,
    document_id UUID NOT NULL,

    CONSTRAINT document_versions_fk_document
        FOREIGN KEY (document_id)
        REFERENCES documents(id)
        ON DELETE CASCADE,

    CONSTRAINT document_versions_uk_document_version
        UNIQUE (document_id, version),

    CONSTRAINT document_versions_version_check
        CHECK (version >= 1)
);

CREATE INDEX document_versions_idx_document ON document_versions(document_id);
CREATE INDEX document_versions_idx_storage_key ON document_versions(storage_key);
CREATE INDEX document_versions_idx_version ON document_versions(version);