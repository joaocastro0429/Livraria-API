package br.com.joaocastro.projeto_livraria_api.dto;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados utilizados para cadastrar ou atualizar um livro")
public class livroDto {

    @Schema(description = "Nome do livro", example = "Java para iniciantes")
    private String name;

    @Schema(description = "Descrição do livro", example = "Introdução à linguagem Java")
    private String description;

    @Schema(description = "Duração em minutos", example = "120")
    private Integer duration;

    @Schema(description = "Data de início", example = "2026-09-02")
    private LocalDate startDate;

    public livroDto() {
    }

    public livroDto(
            String name,
            String description,
            Integer duration,
            LocalDate startDate) {
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.startDate = startDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
}
