package br.com.joaocastro.projeto_livraria_api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import br.com.joaocastro.projeto_livraria_api.repository.LivroRepository;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class LivroApiTests {
    private static final String BOOK = """
            {"name":"Java", "description":"Introdução", "author":"João Castro",
             "isbn":"9780134685991", "publicationDate":"2020-01-01"}
            """;
    @Autowired MockMvc mvc;
    @Autowired LivroRepository repository;
    @BeforeEach void clean() { repository.deleteAll(); }

    private String create() throws Exception {
        return mvc.perform(post("/livro").contentType(MediaType.APPLICATION_JSON).content(BOOK))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andReturn().getResponse().getHeader("Location");
    }

    @Test void createReturnsLocationAndConsistentRepresentation() throws Exception {
        String location = create();
        mvc.perform(get(location)).andExpect(status().isOk())
                .andExpect(jsonPath("$.author").value("João Castro"))
                .andExpect(jsonPath("$.isbn").value("9780134685991"))
                .andExpect(jsonPath("$.publicationDate").value("2020-01-01"))
                .andExpect(jsonPath("$.duration").doesNotExist());
        assertEquals(1, repository.count());
    }

    @Test void listReturnsPersistedBooksWithIds() throws Exception {
        create();
        mvc.perform(get("/livro")).andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").isNumber())
                .andExpect(jsonPath("$[0].name").value("Java"));
    }

    @Test void updatePreservesIdAndPersistsChanges() throws Exception {
        String location = create();
        String id = location.substring(location.lastIndexOf('/') + 1);
        mvc.perform(put(location).contentType(MediaType.APPLICATION_JSON)
                        .content(BOOK.replace("Java", "Spring Boot")))
                .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(Long.parseLong(id)))
                .andExpect(jsonPath("$.name").value("Spring Boot"));
        mvc.perform(get(location)).andExpect(jsonPath("$.name").value("Spring Boot"));
        assertEquals(1, repository.count());
    }

    @Test void deleteReturnsNoContentAndRemovesBook() throws Exception {
        String location = create();
        mvc.perform(delete(location)).andExpect(status().isNoContent()).andExpect(content().string(""));
        mvc.perform(get(location)).andExpect(status().isNotFound());
        assertEquals(0, repository.count());
    }

    @Test void missingBookReturns404ForReadUpdateAndDelete() throws Exception {
        mvc.perform(get("/livro/999999")).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Livro não encontrado"));
        mvc.perform(put("/livro/999999").contentType(MediaType.APPLICATION_JSON).content(BOOK))
                .andExpect(status().isNotFound());
        mvc.perform(delete("/livro/999999")).andExpect(status().isNotFound());
    }

    @Test void invalidNameAndAuthorAreRejectedWithoutSaving() throws Exception {
        mvc.perform(post("/livro").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"  ","author":""}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.name").exists())
                .andExpect(jsonPath("$.errors.author").exists());
        assertEquals(0, repository.count());
    }

    @Test void invalidIsbnIsRejected() throws Exception {
        mvc.perform(post("/livro").contentType(MediaType.APPLICATION_JSON)
                        .content(BOOK.replace("9780134685991", "123")))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.errors.isbn").exists());
        assertEquals(0, repository.count());
    }

    @Test void futurePublicationIsRejected() throws Exception {
        mvc.perform(post("/livro").contentType(MediaType.APPLICATION_JSON)
                        .content(BOOK.replace("2020-01-01", java.time.LocalDate.now().plusDays(2).toString())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.publicationDate").exists());
    }

    @Test void invalidUpdateDoesNotOverwriteStoredBook() throws Exception {
        String location = create();
        mvc.perform(put(location).contentType(MediaType.APPLICATION_JSON)
                        .content(BOOK.replace("João Castro", " ")))
                .andExpect(status().isBadRequest());
        mvc.perform(get(location)).andExpect(jsonPath("$.author").value("João Castro"));
    }

    @Test void optionalFieldsCanBeOmitted() throws Exception {
        mvc.perform(post("/livro").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Java\",\"author\":\"João Castro\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", startsWith("http://localhost/livro/")))
                .andExpect(jsonPath("$.id").isNumber());
    }

    @Test void malformedBodyAndIdReturn400() throws Exception {
        mvc.perform(post("/livro").contentType(MediaType.APPLICATION_JSON).content("{"))
                .andExpect(status().isBadRequest());
        mvc.perform(post("/livro").contentType(MediaType.APPLICATION_JSON)
                        .content(BOOK.replace("2020-01-01", "not-a-date")))
                .andExpect(status().isBadRequest());
        mvc.perform(get("/livro/abc")).andExpect(status().isBadRequest());
    }
}
