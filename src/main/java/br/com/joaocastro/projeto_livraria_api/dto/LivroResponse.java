package br.com.joaocastro.projeto_livraria_api.dto;

import java.time.LocalDate;
import br.com.joaocastro.projeto_livraria_api.entity.LivroEntity;

public record LivroResponse(Long id, String name, String description, String author,
        String isbn, LocalDate publicationDate) {
    public static LivroResponse from(LivroEntity livro) {
        return new LivroResponse(livro.getId(), livro.getName(), livro.getDescription(),
                livro.getAuthor(), livro.getIsbn(), livro.getPublicationDate());
    }
}
