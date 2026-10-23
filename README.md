# Livraria API

Projeto pessoal de estudo: API REST para cadastrar, consultar, atualizar e excluir livros, com Java 17, Spring Boot, Spring Data JPA e PostgreSQL. O objetivo é praticar persistência, contratos HTTP, validação e testes de integração.

## Tecnologias e organização

- Java 17, Spring Boot 4, Maven Wrapper.
- PostgreSQL 16 e Docker Compose no desenvolvimento.
- Spring Data JPA, Bean Validation e Swagger/OpenAPI.
- JUnit, MockMvc e H2 nos testes.

Fluxo: Controller -> Service -> Repository -> PostgreSQL. DTOs de entrada e saída separam o contrato HTTP da entidade persistida. O tratamento de erros fica em `exception/ApiExceptionHandler.java`.

## Executar localmente

Pré-requisitos: JDK 17 e Docker com Compose. Não é necessário instalar Maven.

```bash
git clone https://github.com/joaocastro0429/Livraria-API.git
cd Livraria-API
docker compose up -d
# Linux, macOS ou WSL
bash mvnw spring-boot:run
# Windows: .\mvnw.cmd spring-boot:run
```

A API usa `http://localhost:8080`. O PostgreSQL local usa banco `livraria`, usuário `postgres`, senha de demonstração `admin` e porta `5432`. Esses valores estão em `docker-compose.yml` e `src/main/resources/application.properties`; não são uma configuração de produção.

Swagger: `http://localhost:8080/swagger-ui.html`.
OpenAPI JSON: `http://localhost:8080/v3/api-docs`.

Para encerrar o banco preservando os dados: `docker compose down`. Não utilize `down -v` se quiser preservar o volume.

## Contrato HTTP (versão 2)

| Método | Rota | Resultado |
| --- | --- | --- |
| POST | `/livro` | `201 Created`, livro com ID e cabeçalho `Location` |
| GET | `/livro` | `200 OK`, lista de livros |
| GET | `/livro/{id}` | `200 OK` ou `404 Not Found` |
| PUT | `/livro/{id}` | `200 OK`, livro atualizado, ou `404` |
| DELETE | `/livro/{id}` | `204 No Content` ou `404` |

POST e PUT usam o mesmo formato. PUT substitui os dados editáveis, incluindo os opcionais; campos opcionais omitidos ficam nulos.

```json
{
  "name": "Java para iniciantes",
  "description": "Introdução à linguagem Java",
  "author": "João Castro",
  "isbn": "9780134685991",
  "publicationDate": "2020-01-01"
}
```

| Campo | Regra |
| --- | --- |
| `name` | Obrigatório, não pode ser vazio; até 200 caracteres |
| `author` | Obrigatório, não pode ser vazio; até 200 caracteres |
| `description` | Opcional; até 2000 caracteres |
| `isbn` | Opcional; exatamente 13 dígitos, sem hífens (validação de formato, sem cálculo de dígito verificador) |
| `publicationDate` | Opcional; data no formato `AAAA-MM-DD`, sem datas futuras |

O ID é gerado pelo banco e retornado nas respostas de cadastro, consulta e atualização. Nome e autor têm espaços das extremidades removidos após a validação. ISBN não é único neste escopo.

### Exemplo de uso

```bash
curl -i -X POST http://localhost:8080/livro \
  -H 'Content-Type: application/json' \
  -d '{"name":"Java","author":"João Castro","publicationDate":"2020-01-01"}'
```

Use o ID retornado (o valor `1` abaixo é apenas um exemplo):

```bash
curl http://localhost:8080/livro
curl http://localhost:8080/livro/1
curl -X PUT http://localhost:8080/livro/1 \
  -H 'Content-Type: application/json' \
  -d '{"name":"Java e Spring Boot","author":"João Castro"}'
curl -i -X DELETE http://localhost:8080/livro/1
```

### Erros de entrada

Dados inválidos retornam `400`, com o campo que precisa de correção:

```json
{
  "status": 400,
  "message": "Dados inválidos",
  "errors": {"author": "O autor é obrigatório"}
}
```

Livro inexistente retorna `404`, mensagem `Livro não encontrado` e `errors: {}`. JSON, data ou identificador malformado retornam `400`.

## Testes e compilação

```bash
bash mvnw verify
# Windows: .\mvnw.cmd verify
```

Não é necessário iniciar PostgreSQL ou Docker para testar. O perfil `test` usa H2 em memória e não altera o banco de desenvolvimento. Os testes de integração exercitam controller, validação, service e repository: CRUD, ID inexistente, entradas inválidas e preservação dos dados após atualização rejeitada. O H2 não substitui uma validação específica de compatibilidade com PostgreSQL.

O workflow `.github/workflows/ci.yml` executa `verify` em pushes e pull requests. O resultado da execução fica na aba Actions.

## Mudança em relação ao modelo anterior

Esta versão remove `duration` e `startDate` do contrato e adiciona `author`, `isbn` e `publicationDate`, para representar livros. Clientes que enviavam o formato anterior precisam ser atualizados. Não há conversão de duração/data de início em dados bibliográficos.

No ambiente de estudo, `spring.jpa.hibernate.ddl-auto=update` adiciona as novas colunas sem apagar as antigas. Livros já existentes mantêm ID, nome e descrição; os campos novos ficam nulos até serem preenchidos por PUT. Não exclua o volume do PostgreSQL para atualizar o projeto. Uma implantação de produção exige backup e migração de schema versionada e revisada.

## Escopo e evolução

Projeto de estudo, sem autenticação, paginação, empréstimos ou controle de estoque. Não exponha esta API publicamente como serviço de produção. Próximas evoluções possíveis: paginação, migrações versionadas e testes com PostgreSQL.
