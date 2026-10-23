package br.com.joaocastro.projeto_livraria_api.dto;

import java.time.LocalDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para cadastrar ou atualizar um livro")
public record LivroDto(
        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 200, message = "O nome deve ter até 200 caracteres")
        @Schema(example = "Java para iniciantes") String name,
        @Size(max = 2000, message = "A descrição deve ter até 2000 caracteres")
        @Schema(example = "Introdução à linguagem Java") String description,
        @NotBlank(message = "O autor é obrigatório")
        @Size(max = 200, message = "O autor deve ter até 200 caracteres")
        @Schema(example = "João Castro") String author,
        @Pattern(regexp = "[0-9]{13}", message = "O ISBN deve conter exatamente 13 dígitos")
        @Schema(description = "ISBN opcional, sem hífens; valida apenas o formato", example = "9780134685991") String isbn,
        @PastOrPresent(message = "A data de publicação não pode ser futura")
        @Schema(example = "2020-01-01") LocalDate publicationDate) {
}
