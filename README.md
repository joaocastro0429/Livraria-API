# Projeto Livraria API

API REST para cadastrar, consultar, atualizar e excluir livros. O projeto foi desenvolvido com Java, Spring Boot, Spring Data JPA e PostgreSQL, com documentação interativa por meio do Swagger/OpenAPI.

## Tecnologias

- Java 17
- Spring Boot 4
- Spring Web MVC
- Spring Data JPA
- PostgreSQL 16
- Docker Compose
- Maven Wrapper
- Springdoc OpenAPI e Swagger UI

## Como a aplicação funciona

O fluxo básico de uma requisição é:

```text
Cliente HTTP
    ↓
Controller → recebe a requisição
    ↓
Service → aplica a lógica da aplicação
    ↓
Repository → consulta ou modifica o banco
    ↓
PostgreSQL
```

As principais classes estão organizadas assim:

```text
src/main/java/br/com/joaocastro/projeto_livraria_api/
├── config/       # Configuração do OpenAPI
├── controller/   # Endpoints HTTP
├── dto/          # Dados recebidos pela API
├── entity/       # Entidades mapeadas no banco
├── repository/   # Acesso ao banco com Spring Data JPA
└── service/      # Regras e operações da aplicação
```

## Pré-requisitos

Antes de executar o projeto, instale:

- Java 17
- Docker e Docker Compose

Não é necessário instalar o Maven, pois o projeto possui o Maven Wrapper (`mvnw`).

## Executando o projeto

### 1. Inicie o PostgreSQL

Na raiz do projeto, execute:

```bash
docker compose up -d
```

O Docker criará um banco com estas configurações:

| Configuração | Valor |
|---|---|
| Banco | `livraria` |
| Usuário | `postgres` |
| Senha | `admin` |
| Porta | `5432` |

Para verificar se o container está ativo:

```bash
docker compose ps
```

### 2. Inicie a API

No Linux, macOS ou WSL:

```bash
./mvnw spring-boot:run
```

No Windows (Prompt de Comando ou PowerShell):

```powershell
.\mvnw.cmd spring-boot:run
```

A aplicação estará disponível em:

```text
http://localhost:8080
```

## Documentação Swagger

Com a aplicação em execução, abra no navegador:

```text
http://localhost:8080/swagger-ui.html
```

O documento OpenAPI em JSON fica disponível em:

```text
http://localhost:8080/v3/api-docs
```

Na tela do Swagger, expanda um endpoint, clique em **Try it out**, preencha os dados e clique em **Execute**.

## Formato do livro

O corpo usado para cadastrar ou atualizar um livro possui este formato:

```json
{
  "name": "Java para iniciantes",
  "description": "Introdução à linguagem Java",
  "duration": 120,
  "startDate": "2026-09-02"
}
```

| Campo | Tipo | Descrição |
|---|---|---|
| `name` | texto | Nome do livro |
| `description` | texto | Descrição do livro |
| `duration` | número inteiro | Duração em minutos |
| `startDate` | data | Data no formato `AAAA-MM-DD` |

O campo `id` é gerado automaticamente pelo banco de dados.

## Endpoints

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/livro` | Cadastra um livro |
| `GET` | `/livro` | Lista todos os livros |
| `GET` | `/livro/{id}` | Busca um livro pelo ID |
| `PUT` | `/livro/{id}` | Atualiza um livro |
| `DELETE` | `/livro/{id}` | Exclui um livro |

### Cadastrar um livro

```bash
curl -X POST http://localhost:8080/livro \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Java para iniciantes",
    "description": "Introdução à linguagem Java",
    "duration": 120,
    "startDate": "2026-09-02"
  }'
```

### Listar todos os livros

```bash
curl http://localhost:8080/livro
```

### Buscar um livro pelo ID

```bash
curl http://localhost:8080/livro/1
```

Se o ID não existir, a API responde com o status HTTP `404 Not Found`.

### Atualizar um livro

```bash
curl -X PUT http://localhost:8080/livro/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Java e Spring Boot",
    "description": "Livro atualizado",
    "duration": 180,
    "startDate": "2026-09-03"
  }'
```

### Excluir um livro

```bash
curl -X DELETE http://localhost:8080/livro/1
```

## Compilação e testes

Para verificar a compilação:

```bash
./mvnw compile
```

Para executar os testes, mantenha o PostgreSQL ativo e execute:

```bash
./mvnw test
```

## Configuração do banco

A conexão está definida em `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/livraria
spring.datasource.username=postgres
spring.datasource.password=admin
spring.jpa.hibernate.ddl-auto=update
```

O valor `update` faz o Hibernate criar ou atualizar as tabelas automaticamente durante o desenvolvimento.

## Encerrando o banco

Para parar o container sem excluir os dados:

```bash
docker compose stop
```

Para parar e remover o container, preservando o volume do banco:

```bash
docker compose down
```
