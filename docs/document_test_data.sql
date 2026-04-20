
-- DOCUMENT TEST DATA

-- Assuming table: documents (id, title, description, owner, status, created_at, updated_at)
-- Assuming tags in separate table: document_tags (document_id, tag)

-- Clear tables (optional)
-- DELETE FROM document_tags;
-- DELETE FROM documents;

-- Insert Documents

INSERT INTO documents (id, title, description, owner, status, created_at, updated_at)
VALUES ('f662db0b-6f13-455f-9cd6-bb38ecc909b8', 'Contrato 1', 'Descricao do documento 1', 'admin', 'DRAFT', '2025-12-31 14:42:24.389090', '2026-01-09 14:42:24.389090');

INSERT INTO document_tags (document_id, tag)
VALUES ('f662db0b-6f13-455f-9cd6-bb38ecc909b8', 'juridico');

INSERT INTO documents (id, title, description, owner, status, created_at, updated_at)
VALUES ('78e9b350-f394-4412-b246-8fa90fc925e0', 'Resumo 2', 'Descricao do documento 2', 'user1', 'DRAFT', '2025-12-25 14:42:24.389090', '2026-01-23 14:42:24.389090');

INSERT INTO document_tags (document_id, tag)
VALUES ('78e9b350-f394-4412-b246-8fa90fc925e0', 'financeiro');

INSERT INTO documents (id, title, description, owner, status, created_at, updated_at)
VALUES ('5b74d110-5498-42be-aa96-d159d66853ee', 'Contrato 3', 'Descricao do documento 3', 'admin', 'DRAFT', '2026-02-12 14:42:24.389090', '2026-03-01 14:42:24.389090');

INSERT INTO document_tags (document_id, tag)
VALUES ('5b74d110-5498-42be-aa96-d159d66853ee', 'cliente');

INSERT INTO document_tags (document_id, tag)
VALUES ('5b74d110-5498-42be-aa96-d159d66853ee', 'rh');

INSERT INTO documents (id, title, description, owner, status, created_at, updated_at)
VALUES ('b4e979e2-e711-4934-bc77-969962c0cde8', 'Documento 4', 'Descricao do documento 4', 'empresaB', 'DRAFT', '2026-03-20 14:42:24.389090', '2026-03-30 14:42:24.389090');

INSERT INTO documents (id, title, description, owner, status, created_at, updated_at)
VALUES ('700d775e-1eb5-4f5a-a9ba-304af0402b46', 'Relatorio 5', 'Descricao do documento 5', 'empresaB', 'DRAFT', '2026-04-07 14:42:24.389090', '2026-04-27 14:42:24.389090');

INSERT INTO document_tags (document_id, tag)
VALUES ('700d775e-1eb5-4f5a-a9ba-304af0402b46', 'urgente');

INSERT INTO documents (id, title, description, owner, status, created_at, updated_at)
VALUES ('64861177-a397-4a35-a067-b79d8733154a', 'Resumo 6', 'Descricao do documento 6', 'user2', 'DRAFT', '2026-03-24 14:42:24.389090', '2026-04-13 14:42:24.389090');

INSERT INTO documents (id, title, description, owner, status, created_at, updated_at)
VALUES ('b2998c18-8591-412b-8606-681f4e478b96', 'Contrato 7', 'Descricao do documento 7', 'admin', 'DRAFT', '2025-11-03 14:42:24.389090', '2025-11-05 14:42:24.389090');

INSERT INTO document_tags (document_id, tag)
VALUES ('b2998c18-8591-412b-8606-681f4e478b96', 'urgente');

INSERT INTO documents (id, title, description, owner, status, created_at, updated_at)
VALUES ('ad86d0fa-3077-486c-a758-4fad79f7a80b', 'Relatorio 8', 'Descricao do documento 8', 'user2', 'DRAFT', '2026-03-11 14:42:24.389090', '2026-03-27 14:42:24.389090');

INSERT INTO document_tags (document_id, tag)
VALUES ('ad86d0fa-3077-486c-a758-4fad79f7a80b', 'urgente');

INSERT INTO document_tags (document_id, tag)
VALUES ('ad86d0fa-3077-486c-a758-4fad79f7a80b', 'juridico');

INSERT INTO document_tags (document_id, tag)
VALUES ('ad86d0fa-3077-486c-a758-4fad79f7a80b', 'financeiro');

INSERT INTO documents (id, title, description, owner, status, created_at, updated_at)
VALUES ('0ada9386-c0eb-4841-8b96-58ad05a17512', 'Documento 9', 'Descricao do documento 9', 'admin', 'DRAFT', '2026-02-23 14:42:24.389090', '2026-03-05 14:42:24.389090');

INSERT INTO document_tags (document_id, tag)
VALUES ('0ada9386-c0eb-4841-8b96-58ad05a17512', 'fiscal');

INSERT INTO documents (id, title, description, owner, status, created_at, updated_at)
VALUES ('97f5b9f6-cdc1-4702-9ab5-e07f0a585b3c', 'Contrato 10', 'Descricao do documento 10', 'user1', 'DRAFT', '2026-02-15 14:42:24.389090', '2026-02-15 14:42:24.389090');

INSERT INTO documents (id, title, description, owner, status, created_at, updated_at)
VALUES ('f67e00cb-bc5f-4887-a52e-be80cce60a1a', 'Relatorio 11', 'Descricao do documento 11', 'user1', 'DRAFT', '2025-12-30 14:42:24.389090', '2026-01-13 14:42:24.389090');

INSERT INTO document_tags (document_id, tag)
VALUES ('f67e00cb-bc5f-4887-a52e-be80cce60a1a', 'financeiro');

INSERT INTO document_tags (document_id, tag)
VALUES ('f67e00cb-bc5f-4887-a52e-be80cce60a1a', 'contrato');

INSERT INTO documents (id, title, description, owner, status, created_at, updated_at)
VALUES ('3adc58dc-4fa7-4c66-b549-205d51577c59', 'Documento 12', 'Descricao do documento 12', 'empresaB', 'DRAFT', '2026-01-20 14:42:24.389090', '2026-02-08 14:42:24.389090');

INSERT INTO document_tags (document_id, tag)
VALUES ('3adc58dc-4fa7-4c66-b549-205d51577c59', 'interno');

INSERT INTO document_tags (document_id, tag)
VALUES ('3adc58dc-4fa7-4c66-b549-205d51577c59', 'juridico');

INSERT INTO documents (id, title, description, owner, status, created_at, updated_at)
VALUES ('00077b36-715b-4a83-a2b7-0084ad32dbeb', 'Contrato 13', 'Descricao do documento 13', 'empresaB', 'DRAFT', '2026-02-21 14:42:24.389090', '2026-03-08 14:42:24.389090');

INSERT INTO document_tags (document_id, tag)
VALUES ('00077b36-715b-4a83-a2b7-0084ad32dbeb', 'cliente');

INSERT INTO documents (id, title, description, owner, status, created_at, updated_at)
VALUES ('ba7cd63e-a2a7-4956-9863-299eb0518b3c', 'Documento 14', 'Descricao do documento 14', 'user2', 'DRAFT', '2025-11-10 14:42:24.389090', '2025-11-30 14:42:24.389090');

INSERT INTO documents (id, title, description, owner, status, created_at, updated_at)
VALUES ('2edb9bcf-c99e-4a7e-8cb6-a52a34605912', 'Documento 15', 'Descricao do documento 15', 'empresaB', 'DRAFT', '2025-11-23 14:42:24.389090', '2025-12-09 14:42:24.389090');

INSERT INTO document_tags (document_id, tag)
VALUES ('2edb9bcf-c99e-4a7e-8cb6-a52a34605912', 'financeiro');

INSERT INTO document_tags (document_id, tag)
VALUES ('2edb9bcf-c99e-4a7e-8cb6-a52a34605912', 'urgente');

INSERT INTO document_tags (document_id, tag)
VALUES ('2edb9bcf-c99e-4a7e-8cb6-a52a34605912', 'fiscal');

INSERT INTO documents (id, title, description, owner, status, created_at, updated_at)
VALUES ('7bb3c3e9-f698-41b3-8376-af3bbb1f0071', 'Resumo 16', 'Descricao do documento 16', 'empresaB', 'DRAFT', '2026-01-26 14:42:24.389090', '2026-02-04 14:42:24.389090');

INSERT INTO documents (id, title, description, owner, status, created_at, updated_at)
VALUES ('9f5cf513-8c57-41c1-9fbb-91b5c1c6abad', 'Resumo 17', 'Descricao do documento 17', 'empresaA', 'DRAFT', '2026-01-04 14:42:24.389090', '2026-02-03 14:42:24.389090');

INSERT INTO documents (id, title, description, owner, status, created_at, updated_at)
VALUES ('694a1a59-8ae6-4101-8bff-a819b4fef419', 'Documento 18', 'Descricao do documento 18', 'admin', 'DRAFT', '2025-11-26 14:42:24.389090', '2025-12-06 14:42:24.389090');

INSERT INTO document_tags (document_id, tag)
VALUES ('694a1a59-8ae6-4101-8bff-a819b4fef419', 'urgente');

INSERT INTO document_tags (document_id, tag)
VALUES ('694a1a59-8ae6-4101-8bff-a819b4fef419', 'financeiro');

INSERT INTO document_tags (document_id, tag)
VALUES ('694a1a59-8ae6-4101-8bff-a819b4fef419', 'contrato');

INSERT INTO documents (id, title, description, owner, status, created_at, updated_at)
VALUES ('d5240074-aa47-486b-864a-c3e83e739869', 'Contrato 19', 'Descricao do documento 19', 'empresaA', 'DRAFT', '2026-03-04 14:42:24.389090', '2026-04-01 14:42:24.389090');

INSERT INTO document_tags (document_id, tag)
VALUES ('d5240074-aa47-486b-864a-c3e83e739869', 'interno');

INSERT INTO documents (id, title, description, owner, status, created_at, updated_at)
VALUES ('ed39560d-ca35-43a7-b3b1-3f971ce45d26', 'Resumo 20', 'Descricao do documento 20', 'admin', 'DRAFT', '2026-04-11 14:42:24.389090', '2026-05-02 14:42:24.389090');

INSERT INTO document_tags (document_id, tag)
VALUES ('ed39560d-ca35-43a7-b3b1-3f971ce45d26', 'contrato');

INSERT INTO document_tags (document_id, tag)
VALUES ('ed39560d-ca35-43a7-b3b1-3f971ce45d26', 'cliente');

INSERT INTO documents (id, title, description, owner, status, created_at, updated_at)
VALUES ('bf495d74-3491-45cd-acd0-77d8b4a108d0', 'Documento 21', 'Descricao do documento 21', 'user1', 'DRAFT', '2026-02-21 14:42:24.389090', '2026-02-21 14:42:24.389090');

INSERT INTO document_tags (document_id, tag)
VALUES ('bf495d74-3491-45cd-acd0-77d8b4a108d0', 'rh');

INSERT INTO document_tags (document_id, tag)
VALUES ('bf495d74-3491-45cd-acd0-77d8b4a108d0', 'contrato');

INSERT INTO documents (id, title, description, owner, status, created_at, updated_at)
VALUES ('193d665e-e720-4d39-a18c-370a36520a42', 'Resumo 22', 'Descricao do documento 22', 'admin', 'DRAFT', '2026-03-20 14:42:24.389090', '2026-04-12 14:42:24.389090');

INSERT INTO document_tags (document_id, tag)
VALUES ('193d665e-e720-4d39-a18c-370a36520a42', 'financeiro');

INSERT INTO document_tags (document_id, tag)
VALUES ('193d665e-e720-4d39-a18c-370a36520a42', 'contrato');

INSERT INTO documents (id, title, description, owner, status, created_at, updated_at)
VALUES ('9a32a2d0-254f-4428-ac9c-d723bc082271', 'Relatorio 23', 'Descricao do documento 23', 'user1', 'DRAFT', '2026-03-18 14:42:24.389090', '2026-04-10 14:42:24.389090');

INSERT INTO documents (id, title, description, owner, status, created_at, updated_at)
VALUES ('0bb5c136-f75b-40c2-94a5-9750923470aa', 'Relatorio 24', 'Descricao do documento 24', 'empresaA', 'DRAFT', '2026-01-13 14:42:24.389090', '2026-01-21 14:42:24.389090');

INSERT INTO document_tags (document_id, tag)
VALUES ('0bb5c136-f75b-40c2-94a5-9750923470aa', 'fiscal');

INSERT INTO documents (id, title, description, owner, status, created_at, updated_at)
VALUES ('1142a046-6d70-4d53-bcd7-c280cca7df3f', 'Documento 25', 'Descricao do documento 25', 'user1', 'DRAFT', '2025-12-29 14:42:24.389090', '2026-01-25 14:42:24.389090');

INSERT INTO document_tags (document_id, tag)
VALUES ('1142a046-6d70-4d53-bcd7-c280cca7df3f', 'rh');

INSERT INTO document_tags (document_id, tag)
VALUES ('1142a046-6d70-4d53-bcd7-c280cca7df3f', 'interno');

INSERT INTO documents (id, title, description, owner, status, created_at, updated_at)
VALUES ('6e4f8724-8540-4ea5-b143-f9111ef7676a', 'Contrato 26', 'Descricao do documento 26', 'empresaA', 'DRAFT', '2026-01-05 14:42:24.389090', '2026-01-15 14:42:24.389090');

INSERT INTO document_tags (document_id, tag)
VALUES ('6e4f8724-8540-4ea5-b143-f9111ef7676a', 'juridico');

INSERT INTO document_tags (document_id, tag)
VALUES ('6e4f8724-8540-4ea5-b143-f9111ef7676a', 'urgente');

INSERT INTO document_tags (document_id, tag)
VALUES ('6e4f8724-8540-4ea5-b143-f9111ef7676a', 'interno');

INSERT INTO documents (id, title, description, owner, status, created_at, updated_at)
VALUES ('88df351a-6e16-4db3-b487-4419697a602d', 'Contrato 27', 'Descricao do documento 27', 'admin', 'DRAFT', '2026-01-24 14:42:24.389090', '2026-02-22 14:42:24.389090');

INSERT INTO documents (id, title, description, owner, status, created_at, updated_at)
VALUES ('3e7a5fe9-1438-4b79-bb6b-af9958cfc416', 'Documento 28', 'Descricao do documento 28', 'user2', 'DRAFT', '2025-10-23 14:42:24.389090', '2025-11-11 14:42:24.389090');

INSERT INTO document_tags (document_id, tag)
VALUES ('3e7a5fe9-1438-4b79-bb6b-af9958cfc416', 'contrato');

INSERT INTO document_tags (document_id, tag)
VALUES ('3e7a5fe9-1438-4b79-bb6b-af9958cfc416', 'urgente');

INSERT INTO document_tags (document_id, tag)
VALUES ('3e7a5fe9-1438-4b79-bb6b-af9958cfc416', 'fiscal');

INSERT INTO documents (id, title, description, owner, status, created_at, updated_at)
VALUES ('68a1c17c-55f5-4f56-8bd2-f768d1f363b5', 'Relatorio 29', 'Descricao do documento 29', 'user2', 'DRAFT', '2026-02-15 14:42:24.389090', '2026-02-28 14:42:24.389090');

INSERT INTO documents (id, title, description, owner, status, created_at, updated_at)
VALUES ('1dfe35ed-adbb-410b-a93c-17b9870e6314', 'Contrato 30', 'Descricao do documento 30', 'empresaA', 'DRAFT', '2026-02-19 14:42:24.389090', '2026-03-04 14:42:24.389090');

INSERT INTO document_tags (document_id, tag)
VALUES ('1dfe35ed-adbb-410b-a93c-17b9870e6314', 'financeiro');

INSERT INTO document_tags (document_id, tag)
VALUES ('1dfe35ed-adbb-410b-a93c-17b9870e6314', 'cliente');

INSERT INTO documents (id, title, description, owner, status, created_at, updated_at)
VALUES ('369b9ca8-fa5d-46ba-ad4d-bf5363a208d9', 'Resumo 31', 'Descricao do documento 31', 'admin', 'DRAFT', '2025-12-11 14:42:24.389090', '2026-01-05 14:42:24.389090');

INSERT INTO document_tags (document_id, tag)
VALUES ('369b9ca8-fa5d-46ba-ad4d-bf5363a208d9', 'juridico');

INSERT INTO document_tags (document_id, tag)
VALUES ('369b9ca8-fa5d-46ba-ad4d-bf5363a208d9', 'cliente');

INSERT INTO document_tags (document_id, tag)
VALUES ('369b9ca8-fa5d-46ba-ad4d-bf5363a208d9', 'fiscal');

INSERT INTO documents (id, title, description, owner, status, created_at, updated_at)
VALUES ('9ac17ed4-c115-4709-8a12-9218783ae4c9', 'Relatorio 32', 'Descricao do documento 32', 'admin', 'DRAFT', '2026-04-08 14:42:24.389090', '2026-04-18 14:42:24.389090');

INSERT INTO document_tags (document_id, tag)
VALUES ('9ac17ed4-c115-4709-8a12-9218783ae4c9', 'rh');

INSERT INTO document_tags (document_id, tag)
VALUES ('9ac17ed4-c115-4709-8a12-9218783ae4c9', 'fiscal');

INSERT INTO documents (id, title, description, owner, status, created_at, updated_at)
VALUES ('0699275d-7479-419d-818d-8a9549c631a5', 'Resumo 33', 'Descricao do documento 33', 'user1', 'DRAFT', '2026-03-28 14:42:24.389090', '2026-04-01 14:42:24.389090');

INSERT INTO document_tags (document_id, tag)
VALUES ('0699275d-7479-419d-818d-8a9549c631a5', 'cliente');

INSERT INTO document_tags (document_id, tag)
VALUES ('0699275d-7479-419d-818d-8a9549c631a5', 'contrato');

INSERT INTO document_tags (document_id, tag)
VALUES ('0699275d-7479-419d-818d-8a9549c631a5', 'fiscal');

INSERT INTO documents (id, title, description, owner, status, created_at, updated_at)
VALUES ('e989d99d-6928-420b-b3a6-da29a6436a69', 'Documento 34', 'Descricao do documento 34', 'empresaA', 'DRAFT', '2026-01-12 14:42:24.389090', '2026-01-28 14:42:24.389090');

INSERT INTO document_tags (document_id, tag)
VALUES ('e989d99d-6928-420b-b3a6-da29a6436a69', 'financeiro');

INSERT INTO document_tags (document_id, tag)
VALUES ('e989d99d-6928-420b-b3a6-da29a6436a69', 'cliente');

INSERT INTO documents (id, title, description, owner, status, created_at, updated_at)
VALUES ('bf09ccce-6110-43f8-81ec-12dec77fb02c', 'Relatorio 35', 'Descricao do documento 35', 'user1', 'DRAFT', '2025-12-29 14:42:24.389090', '2026-01-06 14:42:24.389090');

INSERT INTO documents (id, title, description, owner, status, created_at, updated_at)
VALUES ('c058656d-6ecb-449b-ba79-16ffce1b91f6', 'Contrato 36', 'Descricao do documento 36', 'user2', 'DRAFT', '2025-12-18 14:42:24.389090', '2025-12-18 14:42:24.389090');

INSERT INTO document_tags (document_id, tag)
VALUES ('c058656d-6ecb-449b-ba79-16ffce1b91f6', 'financeiro');

INSERT INTO documents (id, title, description, owner, status, created_at, updated_at)
VALUES ('eff3fa78-b4cf-40ab-a813-a53a42ca3f9d', 'Documento 37', 'Descricao do documento 37', 'empresaB', 'DRAFT', '2026-02-06 14:42:24.389090', '2026-02-26 14:42:24.389090');

INSERT INTO document_tags (document_id, tag)
VALUES ('eff3fa78-b4cf-40ab-a813-a53a42ca3f9d', 'financeiro');

INSERT INTO document_tags (document_id, tag)
VALUES ('eff3fa78-b4cf-40ab-a813-a53a42ca3f9d', 'urgente');

INSERT INTO document_tags (document_id, tag)
VALUES ('eff3fa78-b4cf-40ab-a813-a53a42ca3f9d', 'contrato');

INSERT INTO documents (id, title, description, owner, status, created_at, updated_at)
VALUES ('b6d4eb62-e9ab-436b-afb7-0355d278fa4a', 'Resumo 38', 'Descricao do documento 38', 'empresaA', 'DRAFT', '2025-11-21 14:42:24.389090', '2025-12-19 14:42:24.389090');

INSERT INTO documents (id, title, description, owner, status, created_at, updated_at)
VALUES ('679952fc-0ddd-49e6-b3c2-bc30f001f0a3', 'Contrato 39', 'Descricao do documento 39', 'admin', 'DRAFT', '2026-01-28 14:42:24.389090', '2026-02-24 14:42:24.389090');

INSERT INTO document_tags (document_id, tag)
VALUES ('679952fc-0ddd-49e6-b3c2-bc30f001f0a3', 'financeiro');

INSERT INTO document_tags (document_id, tag)
VALUES ('679952fc-0ddd-49e6-b3c2-bc30f001f0a3', 'urgente');

INSERT INTO document_tags (document_id, tag)
VALUES ('679952fc-0ddd-49e6-b3c2-bc30f001f0a3', 'fiscal');

INSERT INTO documents (id, title, description, owner, status, created_at, updated_at)
VALUES ('939b0fe2-0655-4371-9c3a-346e4705d311', 'Resumo 40', 'Descricao do documento 40', 'empresaB', 'DRAFT', '2026-02-13 14:42:24.389090', '2026-03-13 14:42:24.389090');

INSERT INTO document_tags (document_id, tag)
VALUES ('939b0fe2-0655-4371-9c3a-346e4705d311', 'juridico');

INSERT INTO document_tags (document_id, tag)
VALUES ('939b0fe2-0655-4371-9c3a-346e4705d311', 'rh');

INSERT INTO document_tags (document_id, tag)
VALUES ('939b0fe2-0655-4371-9c3a-346e4705d311', 'cliente');

INSERT INTO documents (id, title, description, owner, status, created_at, updated_at)
VALUES ('71f360a4-26e8-40bd-8d3e-88ea889b41cd', 'Documento 41', 'Descricao do documento 41', 'user2', 'DRAFT', '2026-02-14 14:42:24.389090', '2026-02-26 14:42:24.389090');

INSERT INTO document_tags (document_id, tag)
VALUES ('71f360a4-26e8-40bd-8d3e-88ea889b41cd', 'urgente');

INSERT INTO document_tags (document_id, tag)
VALUES ('71f360a4-26e8-40bd-8d3e-88ea889b41cd', 'interno');

INSERT INTO documents (id, title, description, owner, status, created_at, updated_at)
VALUES ('c27c827f-cfa4-4c30-a0fe-1d0a09ac3bed', 'Relatorio 42', 'Descricao do documento 42', 'empresaA', 'DRAFT', '2026-02-18 14:42:24.389090', '2026-03-05 14:42:24.389090');

INSERT INTO document_tags (document_id, tag)
VALUES ('c27c827f-cfa4-4c30-a0fe-1d0a09ac3bed', 'financeiro');

INSERT INTO document_tags (document_id, tag)
VALUES ('c27c827f-cfa4-4c30-a0fe-1d0a09ac3bed', 'urgente');

INSERT INTO documents (id, title, description, owner, status, created_at, updated_at)
VALUES ('0ba0bce9-2bb6-46d6-bece-eb2c4233ca6c', 'Resumo 43', 'Descricao do documento 43', 'user1', 'DRAFT', '2026-01-05 14:42:24.389090', '2026-01-07 14:42:24.389090');

INSERT INTO document_tags (document_id, tag)
VALUES ('0ba0bce9-2bb6-46d6-bece-eb2c4233ca6c', 'cliente');

INSERT INTO document_tags (document_id, tag)
VALUES ('0ba0bce9-2bb6-46d6-bece-eb2c4233ca6c', 'fiscal');

INSERT INTO document_tags (document_id, tag)
VALUES ('0ba0bce9-2bb6-46d6-bece-eb2c4233ca6c', 'financeiro');

INSERT INTO documents (id, title, description, owner, status, created_at, updated_at)
VALUES ('f9c28fcb-26df-4065-8e6f-50eb4ef8b9e2', 'Resumo 44', 'Descricao do documento 44', 'empresaB', 'DRAFT', '2026-01-28 14:42:24.389090', '2026-01-31 14:42:24.389090');

INSERT INTO document_tags (document_id, tag)
VALUES ('f9c28fcb-26df-4065-8e6f-50eb4ef8b9e2', 'cliente');

INSERT INTO document_tags (document_id, tag)
VALUES ('f9c28fcb-26df-4065-8e6f-50eb4ef8b9e2', 'juridico');

INSERT INTO documents (id, title, description, owner, status, created_at, updated_at)
VALUES ('b0a269ce-3402-4512-a633-9d1effdb4f86', 'Resumo 45', 'Descricao do documento 45', 'empresaB', 'DRAFT', '2026-01-15 14:42:24.389090', '2026-01-18 14:42:24.389090');

INSERT INTO document_tags (document_id, tag)
VALUES ('b0a269ce-3402-4512-a633-9d1effdb4f86', 'financeiro');

INSERT INTO documents (id, title, description, owner, status, created_at, updated_at)
VALUES ('12ae7d9a-d9bc-4307-8e9b-37cfbf5a81cf', 'Contrato 46', 'Descricao do documento 46', 'empresaA', 'DRAFT', '2025-11-18 14:42:24.389090', '2025-12-18 14:42:24.389090');

INSERT INTO document_tags (document_id, tag)
VALUES ('12ae7d9a-d9bc-4307-8e9b-37cfbf5a81cf', 'juridico');

INSERT INTO document_tags (document_id, tag)
VALUES ('12ae7d9a-d9bc-4307-8e9b-37cfbf5a81cf', 'fiscal');

INSERT INTO document_tags (document_id, tag)
VALUES ('12ae7d9a-d9bc-4307-8e9b-37cfbf5a81cf', 'interno');

INSERT INTO documents (id, title, description, owner, status, created_at, updated_at)
VALUES ('005d6287-f01c-45ff-865a-9a1de509558f', 'Resumo 47', 'Descricao do documento 47', 'empresaB', 'DRAFT', '2026-03-14 14:42:24.389090', '2026-03-20 14:42:24.389090');

INSERT INTO document_tags (document_id, tag)
VALUES ('005d6287-f01c-45ff-865a-9a1de509558f', 'contrato');

INSERT INTO documents (id, title, description, owner, status, created_at, updated_at)
VALUES ('d23b0f3a-8fee-4d26-92be-072e2a5f36f0', 'Contrato 48', 'Descricao do documento 48', 'empresaA', 'DRAFT', '2026-01-30 14:42:24.389090', '2026-02-14 14:42:24.389090');

INSERT INTO document_tags (document_id, tag)
VALUES ('d23b0f3a-8fee-4d26-92be-072e2a5f36f0', 'juridico');

INSERT INTO document_tags (document_id, tag)
VALUES ('d23b0f3a-8fee-4d26-92be-072e2a5f36f0', 'urgente');

INSERT INTO document_tags (document_id, tag)
VALUES ('d23b0f3a-8fee-4d26-92be-072e2a5f36f0', 'fiscal');

INSERT INTO documents (id, title, description, owner, status, created_at, updated_at)
VALUES ('7f35d9cc-e362-4113-a47e-b48c9a30a4db', 'Contrato 49', 'Descricao do documento 49', 'admin', 'DRAFT', '2026-01-18 14:42:24.389090', '2026-01-27 14:42:24.389090');

INSERT INTO document_tags (document_id, tag)
VALUES ('7f35d9cc-e362-4113-a47e-b48c9a30a4db', 'urgente');

INSERT INTO documents (id, title, description, owner, status, created_at, updated_at)
VALUES ('0c962459-79ec-4f35-8e44-4871dadfbb5a', 'Documento 50', 'Descricao do documento 50', 'empresaB', 'DRAFT', '2026-02-09 14:42:24.389090', '2026-02-10 14:42:24.389090');

INSERT INTO document_tags (document_id, tag)
VALUES ('0c962459-79ec-4f35-8e44-4871dadfbb5a', 'interno');

INSERT INTO document_tags (document_id, tag)
VALUES ('0c962459-79ec-4f35-8e44-4871dadfbb5a', 'juridico');

INSERT INTO documents (id, title, description, owner, status, created_at, updated_at)
VALUES ('0cda3538-7622-4149-9f8a-2b4ad589caea', 'Resumo 51', 'Descricao do documento 51', 'admin', 'DRAFT', '2026-04-08 14:42:24.389090', '2026-04-24 14:42:24.389090');

INSERT INTO document_tags (document_id, tag)
VALUES ('0cda3538-7622-4149-9f8a-2b4ad589caea', 'cliente');

INSERT INTO document_tags (document_id, tag)
VALUES ('0cda3538-7622-4149-9f8a-2b4ad589caea', 'financeiro');

INSERT INTO documents (id, title, description, owner, status, created_at, updated_at)
VALUES ('47ec0e02-1145-4803-9c3c-536ea15fbb8d', 'Contrato 52', 'Descricao do documento 52', 'empresaA', 'DRAFT', '2025-12-13 14:42:24.389090', '2025-12-22 14:42:24.389090');

INSERT INTO document_tags (document_id, tag)
VALUES ('47ec0e02-1145-4803-9c3c-536ea15fbb8d', 'fiscal');

INSERT INTO documents (id, title, description, owner, status, created_at, updated_at)
VALUES ('c8610748-827d-4c7b-bdc1-67fa60b370da', 'Relatorio 53', 'Descricao do documento 53', 'empresaB', 'DRAFT', '2026-03-09 14:42:24.389090', '2026-03-22 14:42:24.389090');

INSERT INTO document_tags (document_id, tag)
VALUES ('c8610748-827d-4c7b-bdc1-67fa60b370da', 'juridico');

INSERT INTO document_tags (document_id, tag)
VALUES ('c8610748-827d-4c7b-bdc1-67fa60b370da', 'rh');

INSERT INTO documents (id, title, description, owner, status, created_at, updated_at)
VALUES ('76461138-3df1-486c-98f3-9371877e77d2', 'Contrato 54', 'Descricao do documento 54', 'empresaB', 'DRAFT', '2026-04-08 14:42:24.389090', '2026-04-08 14:42:24.389090');

INSERT INTO document_tags (document_id, tag)
VALUES ('76461138-3df1-486c-98f3-9371877e77d2', 'contrato');

INSERT INTO document_tags (document_id, tag)
VALUES ('76461138-3df1-486c-98f3-9371877e77d2', 'urgente');

INSERT INTO documents (id, title, description, owner, status, created_at, updated_at)
VALUES ('9c266b76-6c4c-410b-b506-4bd23977f1b8', 'Documento 55', 'Descricao do documento 55', 'user2', 'DRAFT', '2026-04-02 14:42:24.389090', '2026-04-03 14:42:24.389090');

INSERT INTO document_tags (document_id, tag)
VALUES ('9c266b76-6c4c-410b-b506-4bd23977f1b8', 'financeiro');

INSERT INTO documents (id, title, description, owner, status, created_at, updated_at)
VALUES ('f70bd0b2-75dc-4a7a-ae97-9948e5b41692', 'Resumo 56', 'Descricao do documento 56', 'admin', 'DRAFT', '2026-02-11 14:42:24.389090', '2026-02-12 14:42:24.389090');

INSERT INTO document_tags (document_id, tag)
VALUES ('f70bd0b2-75dc-4a7a-ae97-9948e5b41692', 'fiscal');

INSERT INTO document_tags (document_id, tag)
VALUES ('f70bd0b2-75dc-4a7a-ae97-9948e5b41692', 'contrato');

INSERT INTO document_tags (document_id, tag)
VALUES ('f70bd0b2-75dc-4a7a-ae97-9948e5b41692', 'urgente');

INSERT INTO documents (id, title, description, owner, status, created_at, updated_at)
VALUES ('51d15438-4b54-4474-bc1c-bfbf32003a5b', 'Resumo 57', 'Descricao do documento 57', 'empresaA', 'DRAFT', '2026-03-06 14:42:24.389090', '2026-03-25 14:42:24.389090');

INSERT INTO document_tags (document_id, tag)
VALUES ('51d15438-4b54-4474-bc1c-bfbf32003a5b', 'urgente');

INSERT INTO document_tags (document_id, tag)
VALUES ('51d15438-4b54-4474-bc1c-bfbf32003a5b', 'interno');

INSERT INTO document_tags (document_id, tag)
VALUES ('51d15438-4b54-4474-bc1c-bfbf32003a5b', 'fiscal');

INSERT INTO documents (id, title, description, owner, status, created_at, updated_at)
VALUES ('87713f02-3761-4674-afec-d381238f1b65', 'Resumo 58', 'Descricao do documento 58', 'admin', 'DRAFT', '2026-03-24 14:42:24.389090', '2026-03-29 14:42:24.389090');

INSERT INTO document_tags (document_id, tag)
VALUES ('87713f02-3761-4674-afec-d381238f1b65', 'juridico');

INSERT INTO documents (id, title, description, owner, status, created_at, updated_at)
VALUES ('6f664f38-3659-46c3-8b58-a2bf2a50fd31', 'Contrato 59', 'Descricao do documento 59', 'empresaB', 'DRAFT', '2025-12-10 14:42:24.389090', '2025-12-21 14:42:24.389090');

INSERT INTO document_tags (document_id, tag)
VALUES ('6f664f38-3659-46c3-8b58-a2bf2a50fd31', 'fiscal');

INSERT INTO document_tags (document_id, tag)
VALUES ('6f664f38-3659-46c3-8b58-a2bf2a50fd31', 'interno');

INSERT INTO documents (id, title, description, owner, status, created_at, updated_at)
VALUES ('fa3441f0-4751-4b65-91af-c43c5da927ab', 'Resumo 60', 'Descricao do documento 60', 'user1', 'DRAFT', '2026-02-06 14:42:24.389090', '2026-02-10 14:42:24.389090');

INSERT INTO document_tags (document_id, tag)
VALUES ('fa3441f0-4751-4b65-91af-c43c5da927ab', 'financeiro');

INSERT INTO document_tags (document_id, tag)
VALUES ('fa3441f0-4751-4b65-91af-c43c5da927ab', 'rh');
