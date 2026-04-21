# 📄 GED - Document Management System (Backend)

Backend da aplicação de Gestão Eletrônica de Documentos (GED), desenvolvido como parte de um teste técnico para Desenvolvedor Java Sênior.

---

# 📌 Visão Geral

Este módulo é responsável por:

- Gerenciamento de documentos (metadados)
- Busca avançada com filtros dinâmicos
- Versionamento de documentos (upload)
- Controle de acesso (JWT)
- Armazenamento de arquivos (MinIO)
- Exposição de API REST documentada via Swagger
- Testes automatizados (unitários e integração)
- Pipeline CI com GitHub Actions

A solução foi construída com foco em **boas práticas de arquitetura, escalabilidade e manutenibilidade**.

---

# 🏗️ Arquitetura

O projeto segue arquitetura em camadas:

- **Controller** → entrada da API (REST)
- **Service** → regras de negócio
- **Repository** → acesso a dados (JPA)
- **Specification** → filtros dinâmicos
- **DTOs** → contratos da API
- **Mapper** → conversão DTO ↔ Entity
- **Storage** → abstração de armazenamento (MinIO)

Essa estrutura promove:

- separação de responsabilidades
- testabilidade
- facilidade de evolução

---

# 📦 Estrutura do Projeto

```
backend/
├── src
│   ├── main
│   │   ├── java/com/ged/backend
│   │   │   ├── config
│   │   │   ├── controller
│   │   │   ├── domain
│   │   │   ├── exception
│   │   │   ├── mapper
│   │   │   ├── repository
│   │   │   ├── security
│   │   │   ├── service
│   │   │   ├── specification
│   │   │   ├── storage
│   │   │   └── util
│   │   └── resources
│   └── test
│       └── java/com/ged/backend
│           ├── controller
│           └── service
```

---

# 🔍 Busca de Documentos

A busca foi implementada utilizando Spring Data JPA Specifications, permitindo filtros dinâmicos e combináveis.

Supported filters:

- title / description
- searchType (CONTAINS / STARTS_WITH)
- status
- owner
- date ranges (created/updated)
- tags (list)

OBS: Também resolve problemas de drive VS JPA existente quando se usa PostgreSQL. O drive do PostgreSQL não trabalha bem com listas nulas. 

---

# 📄 Porque POST para busca

Foi utilizado POST ao invés de GET para suportar:

- filtros complexos
- listas (tags)
- melhor integração com Swagger/OpenAPI
- payload estruturado via JSON

Essa abordagem é comum em APIs corporativas com filtros avançados.

---

# 🧪 Estratégia de Testes

## Testes Unitários

Os testes unitários cobrem principalmente a camada de serviço ligada ao domínio funcional da aplicação, com foco em regras de negócio e persistência.

Foram contemplados, principalmente, os serviços responsáveis por:

- criação de documentos
- atualização de documentos
- remoção de documentos
- busca por identificador
- busca com filtros dinâmicos
- versionamento de documentos
- regras de validação de negócio

Esses testes validam cenários de:

- sucesso
- validação de entrada
- recurso não encontrado
- erro de banco
- retorno vazio
- regras de versionamento e armazenamento

### Observação sobre a camada de segurança

Os componentes da camada de segurança, como autenticação JWT, filtro JWT e regras de autorização, não foram priorizados em testes unitários isolados.

Isso ocorreu porque, neste projeto, esses comportamentos foram validados de forma mais aderente ao uso real por meio de **testes de integração**, cobrindo o fluxo completo de autenticação e autorização nos endpoints protegidos.

Essa decisão permitiu validar de forma mais efetiva:

- geração e uso do token JWT
- acesso autenticado aos endpoints
- restrições por perfil (`ADMIN` e `USER`)
- respostas HTTP esperadas, como `401 Unauthorized` e `403 Forbidden`

---

## Testes de Integração

Os testes de integração validam o comportamento da aplicação de ponta a ponta, incluindo:

- autenticação e geração de token JWT
- autorização por perfil (`ADMIN` e `USER`)
- acesso aos endpoints REST
- fluxo de criação, consulta e versionamento de documentos

Para garantir isolamento e previsibilidade, os testes utilizam:

- Profile: `test`
- Banco: **H2 em memória**
- MinIO: **desabilitado/mockado**

Essa abordagem evita dependência de infraestrutura externa e garante execução rápida e consistente, tanto localmente quanto no pipeline de CI.

---

# ⚠️ Tratamento de Exceções

O tratamento de exceções foi implementado de forma centralizada através de um `GlobalExceptionHandler`, utilizando `@RestControllerAdvice`.

Essa abordagem permite:

- padronização das respostas de erro
- controle explícito dos códigos HTTP
- desacoplamento entre regras de negócio e tratamento de erro
- melhor rastreabilidade e logging

As exceções de negócio estendem uma `BaseException`, que encapsula o `HttpStatus` correspondente.

---

# 🔐 Segurança

A aplicação utiliza autenticação baseada em **JWT (JSON Web Token)** para controle de acesso.

Após a autenticação, um token é gerado e deve ser enviado no header das requisições:

```text
Authorization: Bearer <token>
```

O controle de acesso é realizado por perfil (**roles**), sendo:

- `ADMIN` → acesso completo aos recursos
- `USER` → acesso restrito conforme regras de negócio

A validação do token e a extração das informações de autenticação são realizadas por um filtro customizado integrado ao Spring Security.

---

# 📊 Swagger (auto-documentação da API)

http://localhost:8080/swagger-ui.html

---

# ⚙️ Execução

A aplicação pode ser executada via Docker Compose a partir da raiz do monorepo.

> ⚠️ Importante: o projeto depende de variáveis de ambiente definidas no arquivo `.env`.  
> Certifique-se de criar esse arquivo a partir do `.env.example` antes de subir o ambiente.

---

## ▶️ Subir ambiente completo

```bash
docker compose --env-file .env -f docker/docker-compose.yml up --build -d
```

Este comando irá iniciar automaticamente:

- PostgreSQL
- MinIO
- Backend (Spring Boot)
- Frontend (Angular)

Além disso, o processo de inicialização do MinIO cria automaticamente o bucket configurado no `.env`, eliminando a necessidade de criação manual.

---

## 🛑 Parar o ambiente

### 🔹 Parar mantendo os dados

```bash
docker compose --env-file .env -f docker/docker-compose.yml down
```

---

### 🔹 Parar removendo dados (reset completo)

```bash
docker compose --env-file .env -f docker/docker-compose.yml down -v
```

Use quando:

- precisar resetar o banco de dados
- limpar storage (MinIO)
- testar migrations do zero

---

### 🔹 Parar removendo containers órfãos

```bash
docker compose --env-file .env -f docker/docker-compose.yml down --remove-orphans
```
---

# 🚀 CI Pipeline

O projeto possui um pipeline de Integração Contínua (CI) implementado com **GitHub Actions**, garantindo validação automática do código a cada alteração.

## ⚙️ O que o pipeline executa

- build do backend (`mvn clean verify`)
- execução de testes unitários e de integração
- build do frontend (Angular)
- cache de dependências:
  - Maven
  - npm
- execução condicional por path (monorepo):
  - alterações em `/backend` disparam apenas o job do backend
  - alterações em `/frontend` disparam apenas o job do frontend

## 🎯 Objetivo

O pipeline garante que:

- o backend está compilando corretamente
- os testes estão passando
- o frontend está buildando sem erros

Caso alguma etapa falhe, o merge do código é bloqueado.

---

## 💡 Observação

O ambiente de testes do backend é isolado de infraestrutura externa:

- uso de banco em memória (H2)
- dependências como MinIO desabilitadas/mockadas

Isso garante execução rápida, confiável e consistente tanto localmente quanto no CI.

---

# 🧪 Testes Funcionais

## 📄 Massa de Dados

Arquivo disponível em:

```
/docs/document_test_data.sql
```

Contém dados de exemplo para facilitar a validação dos principais fluxos da aplicação, permitindo testar rapidamente:

- criação de documentos
- buscas com filtros
- cenários com diferentes status e owners
- todos os documentos estão com o status DRAFT

---

## 📬 Collection Postman

Arquivo disponível em:

```
/docs/ged.postman_collection.json
```

A collection inclui:

- endpoints do sistema organizados
- exemplos de payloads
- cenários completos de teste

Permite validar de forma prática:

- autenticação (login e uso de JWT)
- autorização por perfil (ADMIN / USER)
- operações de documentos
- versionamento de arquivos

---

## 💡 Observação

Os testes também podem ser realizados via Swagger, porém a collection do Postman foi estruturada para simular fluxos reais de uso da aplicação de forma mais completa.

---

# 💡 Tecnologias

- Java 21
- Spring Boot
- Spring Security
- Spring Data JPA
- PostgreSQL
- H2 (tests)
- MinIO
- Flyway
- Swagger (OpenAPI)
- JUnit 5 + Mockito
- Docker / Docker Compose
- GitHub Actions (CI)

---

# 🧠 Destaques Técnicos

- Arquitetura limpa e modular, com separação clara de responsabilidades
- Uso de **Spring Data JPA Specifications** para construção de queries dinâmicas e combináveis
- Aplicação do padrão **DTO + Mapper**, desacoplando a API do modelo de domínio
- Tratamento de exceções centralizado via `GlobalExceptionHandler`
- Testes desacoplados de infraestrutura externa (banco em memória e storage mockado)
- Pipeline de CI completo com validação automática de build e testes
- Estrutura em monorepo, facilitando setup, integração e avaliação do projeto

---

# 👨‍💻 Autor

Raimundo do Espírito Santo
Email: espirito.santo.arthais@outlook.com
LinkedIn: https://www.linkedin.com/in/raimundo-do-esp%C3%ADrito-santo-37306b282/
