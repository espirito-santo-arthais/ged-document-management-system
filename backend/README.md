# GED - Document Management System

Sistema de gestão de documentos (GED) desenvolvido como parte de um teste técnico para Desenvolvedor Java Sênior.

---

# 📌 Visão Geral

Este projeto implementa um módulo de gestão de documentos com:

- Metadados (título, descrição, tags, status, owner)
- Busca avançada com múltiplos filtros
- Paginação e ordenação
- Tratamento robusto de exceções
- Testes unitários no service layer
- Documentação via Swagger/OpenAPI

A solução foi construída com foco em boas práticas de arquitetura, escalabilidade e clareza de código.

---

# 🏗️ Arquitetura

O projeto segue uma arquitetura em camadas:

- **Controller** → camada de entrada (API)
- **Service** → regras de negócio
- **Repository** → acesso a dados
- **Specification** → construção dinâmica de filtros
- **DTOs** → contratos de entrada/saída
- **Mapper** → conversão entre DTO e Entity

Essa separação facilita manutenção, testes e evolução do sistema.

---

# 📦 Estrutura do Projeto

```text
backend/
├───src
│   ├───main
│   │   ├───java
│   │   │   └───com
│   │   │       └───ged
│   │   │           └───backend
│   │   │               ├───config             # Classes de configuração
│   │   │               ├───controller         # Camada REST (entrada da API)
│   │   │               ├───domain             # Camada de domínio
│   │   │               │   ├───dto            # Objetos de transferência
│   │   │               │   │   └───document   # Objetos de transferência relativos ao Documento
│   │   │               │   ├───entity         # Entidades JPA
│   │   │               │   └───enums          # Enumerações
│   │   │               ├───exception          # Exceções customizadas
│   │   │               │   └───handler        # Tratamento centralizado de exceções
│   │   │               ├───mapper             # Conversão DTO ↔ Entity
│   │   │               ├───repository         # Acesso ao banco (JPA)
│   │   │               ├───security           # Classes relacionadas à segurança
│   │   │               ├───service            # Regras de negócio
│   │   │               ├───specification      # Filtros dinâmicos
│   │   │               ├───storage            # Classes relacionadas ao armazenamento de arquivos
│   │   │               │   └───minio          # Classes específicas do MinIO
│   │   │               └───util               # Classes utilitárias
│   │   └───resources
│   │       ├───static
│   │       └───templates
│   └───test
│       └───java
│           └───com
│               └───ged
│                   └───backend
│                       └───service            # Testes dos serviços
```
---

# 🔍 Busca de Documentos

A busca foi implementada utilizando **Spring Data JPA Specifications**, permitindo filtros dinâmicos e combináveis.

### Filtros disponíveis:

- title (título ou descrição)
- searchType (CONTAINS / STARTS_WITH)
- status
- owner
- createdAfter / createdBefore
- updatedAfter / updatedBefore
- tags (lista)

---

## 📄 Justificativa do uso de Specification

Foi adotado o uso de Specification (Spring Data JPA) para construção dinâmica de filtros, substituindo queries estáticas com `@Query`.

Essa decisão foi motivada pela necessidade de suportar filtros combináveis, como título, status, owner, período de datas e lista de tags, sem gerar complexidade excessiva no código.

A abordagem baseada em queries estáticas exige expressões extensas do tipo:

```text
(:param IS NULL OR ...)
```

o que compromete a legibilidade e dificulta a manutenção conforme novos filtros são adicionados.

Além disso, foi identificado um problema prático ao utilizar listas como parâmetro (ex: tags) em queries estáticas. Quando o valor da lista é `null`, o driver do PostgreSQL pode gerar inconsistências ou falhas na execução da query, especialmente em cláusulas do tipo `IN (:tags)` ou `EXISTS`, exigindo tratamentos adicionais no service ou múltiplas variações da mesma query.

Com o uso de Specification, esses cenários são tratados de forma mais segura e natural, pois cada filtro é adicionado apenas quando necessário, evitando a inclusão de condições inválidas na query final.

Outro ponto relevante é a separação de responsabilidades:

- O **Repository** permanece simples e genérico
- A **Specification** concentra a lógica de filtragem (equivalente ao WHERE)
- O **Service** orquestra a combinação dos filtros

Essa abordagem resulta em um código mais limpo, extensível, testável e resiliente a problemas comuns de parametrização dinâmica, especialmente em bancos como PostgreSQL.

---

## 📄 Justificativa do uso de POST na busca

Embora operações de busca sejam tradicionalmente associadas ao método HTTP GET, neste projeto foi adotado o uso de POST devido à complexidade dos filtros envolvidos.

A busca permite a combinação de múltiplos critérios, incluindo campos simples, intervalos de datas e listas (como tags). Representar essa estrutura utilizando apenas parâmetros de query (GET) gera limitações práticas, tanto na legibilidade quanto na interoperabilidade.

Foi identificado, em especial, um problema com ferramentas de documentação como OpenAPI/Swagger: ao utilizar GET com parâmetros simples para representar listas (ex: `List<String> tags`), a interface gerada não interpreta corretamente múltiplos valores, apresentando apenas um único campo de entrada para a lista. Isso dificulta testes, pode induzir a erros no consumo da API e não representa adequadamente o contrato esperado.

Ao utilizar POST com um corpo JSON (DTO), é possível:

- Representar listas (como tags) corretamente
- Estruturar os filtros de forma clara e tipada
- Melhorar significativamente a experiência de uso no Swagger/OpenAPI
- Evitar limitações e ambiguidades na serialização de parâmetros
- Manter a API mais legível e extensível

Embora o endpoint utilize POST, ele continua semanticamente seguro (não altera o estado do servidor), sendo utilizado apenas como uma solução técnica para suportar a complexidade dos dados de entrada.

Essa abordagem é amplamente adotada em APIs corporativas quando os critérios de busca ultrapassam o uso simples de query parameters.

---

# 🧪 Testes Unitários

Foram implementados testes unitários no Service Layer cobrindo:

- create
- update
- delete
- findById
- search

Incluindo cenários de:

- sucesso
- validação
- erro de banco
- resultados vazios

---

# 🧪 Testes Funcionais

Para facilitar a validação do sistema, o projeto disponibiliza uma massa de dados e uma coleção de requisições prontas.

---

### 📄 Massa de Dados (SQL)

É possível popular o banco de dados com dados de teste executando o script:

```text
\docs\document_test_data.sql
```

Esse script insere registros variados que permitem testar todos os cenários de busca, incluindo:

- diferentes status
- múltiplos owners
- combinações de tags
- variações de datas

---

### 📬 Collection do Postman

Também está disponível uma collection para testes funcionais via Postman:

```text
\docs\ged.postman_collection.json
```

Essa collection contém:

- endpoints principais da API
- exemplos de payloads
- cenários de busca já configurados

---

### ▶️ Como utilizar

1. Execute o script SQL no banco (via DBeaver, psql ou outro client)
2. Importe a collection no Postman
3. Execute os endpoints diretamente

---

### 💡 Benefícios

- agiliza a validação da aplicação
- padroniza os testes funcionais
- facilita a avaliação técnica do projeto

---

# ⚠️ Tratamento de Exceções

O sistema utiliza uma hierarquia de exceções customizadas baseada em `BaseException`, com:

- HttpStatus embutido
- errorCode opcional
- tratamento centralizado via `@RestControllerAdvice`

Isso garante:

- respostas padronizadas
- melhor rastreabilidade
- desacoplamento da lógica de erro

---

# 📊 Swagger / OpenAPI

A documentação da API está disponível em:

http://localhost:8080/swagger-ui/index.html

---

# ⚙️ Execução do Projeto

## 🔧 Pré-requisitos

- Java 21
- Maven
- Docker (opcional)

---

## ▶️ Rodando localmente

mvn clean install  
mvn spring-boot:run  

---

## 🐳 Rodando com Docker

docker compose --env-file .env -f docker/docker-compose.yml up --build  

---

## 🛑 Parar containers

Existem duas formas de parar o ambiente Docker, dependendo do objetivo:

### 🔹 Parar mantendo os dados

```bash
docker compose --env-file .env -f docker/docker-compose.yml down
```

Use quando:

- deseja apenas parar a aplicação
- quer manter os dados do banco e do storage
- pretende subir novamente sem perder estado

### 🔹 Parar e resetar completamente (remover volumes)

```bash
docker compose --env-file .env -f docker/docker-compose.yml down -v
```

Use quando:

- deseja limpar completamente o ambiente
- precisa resetar o banco de dados
- quer remover dados persistidos (volumes)
- está testando migrations ou mudanças estruturais

⚠️ Atenção: este comando remove todos os dados persistidos, incluindo banco e storage.

---

# 🔐 Configuração (.env)

O projeto utiliza variáveis de ambiente para desacoplar configurações do código.

Existem dois arquivos relacionados:

### 📄 `.env.example` (versionado)

Arquivo de exemplo incluído no repositório, contendo apenas placeholders e valores genéricos.

Serve como template para configuração do ambiente local.

Exemplo:

```bash
DB_HOST=localhost
DB_PORT=5432
DB_USER=postgres
DB_PASSWORD=your_password_here
JWT_SECRET=your_secret_here
```

### 🔒 .env (NÃO versionado)

Arquivo que deve conter os valores reais utilizados na execução da aplicação.

Este arquivo não deve ser versionado, pois pode conter informações sensíveis como:

- senhas de banco
- secrets (JWT, tokens)
- URLs internas

### ▶️ Como configurar

Copie o arquivo de exemplo e preencha com valores reais:

```bash
cp .env.example .env
```

### ⚠️ Boas práticas

- Nunca subir o arquivo .env para o repositório
- Garantir que .env esteja listado no .gitignore
- Utilizar .env.example como documentação de configuração

Essa abordagem segue boas práticas de segurança e facilita a execução do projeto em diferentes ambientes.

---

# 💡 Tecnologias utilizadas

- Java 21
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Lombok
- Swagger (OpenAPI)
- JUnit 5 + Mockito
- Docker / Docker Compose

---

# 🚀 Decisões Técnicas

### 🧱 Arquitetura em Camadas

A aplicação foi estruturada em camadas bem definidas (Controller, Service, Repository), promovendo:

- separação de responsabilidades
- maior testabilidade
- facilidade de manutenção e evolução

---

### 📦 Uso de DTOs

DTOs foram utilizados para:

- desacoplar a API do modelo de domínio
- validar entrada com Bean Validation
- evitar exposição direta das entidades JPA
- permitir evolução da API sem impacto no banco

---

### 🔄 Mapper dedicado

A conversão entre Entity e DTO foi isolada em uma camada de mapper, garantindo:

- responsabilidade única
- reaproveitamento de código
- clareza na transformação de dados

---

### 🔍 Specification (Spring Data JPA)

Utilizado para construção dinâmica de queries, permitindo:

- filtros combináveis
- melhor legibilidade comparado a `@Query`
- eliminação de lógica condicional complexa
- tratamento seguro de parâmetros opcionais (ex: listas como tags)

---

### 🧠 Enum SearchType (CONTAINS / STARTS_WITH)

Substitui flags booleanas ambíguas, proporcionando:

- maior clareza semântica
- facilidade de extensão futura (EXACT, ENDS_WITH)
- integração direta com a lógica de Specification

---

### 🌐 Uso de POST para busca

Adotado para suportar filtros complexos, permitindo:

- envio de listas (tags) de forma estruturada
- melhor integração com Swagger/OpenAPI
- maior legibilidade e escalabilidade do contrato da API

---

### ⚠️ Tratamento de Exceções Centralizado

Implementado com `@RestControllerAdvice` e uma hierarquia baseada em `BaseException`, garantindo:

- padronização de respostas
- encapsulamento do HttpStatus nas exceptions
- eliminação de lógica condicional no handler
- melhor rastreabilidade de erros

---

### 🔐 Uso de Variáveis de Ambiente (.env)

Configurações externalizadas para:

- evitar hardcode de credenciais
- permitir execução em múltiplos ambientes
- seguir boas práticas de segurança

Uso de `.env.example` como template versionado e `.env` ignorado via `.gitignore`.

---

### 🧪 Testes Unitários no Service Layer

Cobertura completa dos métodos principais:

- create, update, delete, findById, search

Incluindo cenários de:

- sucesso
- validação
- erro de banco
- retorno vazio

Utilizando JUnit 5 e Mockito.

---

### 📄 Documentação com Swagger/OpenAPI

Utilizado para:

- documentação interativa da API
- validação de contratos
- testes manuais dos endpoints

---

### 🐳 Uso de Docker Compose

Adotado para facilitar execução local com:

- banco de dados
- serviços auxiliares (ex: MinIO no futuro)

Execução centralizada a partir da raiz do monorepo.

---

### 📁 Estratégia Monorepo

Escolhido para este projeto por:

- simplificar setup local
- centralizar CI/CD
- facilitar avaliação técnica

Em cenários maiores, poderia evoluir para multi-repo ou abordagem híbrida.

---

### 🧵 Uso de UUID como Identificador

Adotado para:

- evitar colisões em ambientes distribuídos
- permitir integração futura com múltiplos serviços
- maior segurança em exposição de IDs

---

### 🧩 Uso de @ElementCollection para Tags

Escolhido para simplificar o modelo de dados, permitindo:

- armazenamento direto de listas de tags
- menor complexidade em relação a tabelas relacionais completas
- boa performance para o cenário proposto

---

### 📌 Uso de Pageable no Controller

O `Pageable` é recebido diretamente na camada de controller, mantendo:

- responsabilidade de entrada na camada correta
- service focado apenas em regra de negócio

---

# 🔮 Evolução futura

- Upload de arquivos (MinIO / S3)
- Versionamento de documentos
- Autenticação JWT
- Frontend Angular

---

# 👨‍💻 Autor

Raimundo do Espírito Santo  
**WhatsApp**: 19-996539911  
**E-mail**: espirito.santo.arthais@outlook.com  

